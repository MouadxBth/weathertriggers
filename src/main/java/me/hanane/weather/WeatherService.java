package me.hanane.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vaadin.flow.component.Component;
import me.hanane.location.Geolocation;
import me.hanane.location.GeolocationService;
import me.hanane.weather.info.MainInfo;
import me.hanane.weather.info.WeatherInfo;
import me.hanane.weather.info.WindInfo;
import org.apache.http.client.utils.URIBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static me.hanane.utilities.Utilities.*;

@Service
public class WeatherService {

    private List<Component> weatherComponents = new ArrayList<>();
    private final static Logger LOGGER = LoggerFactory
            .getLogger(GeolocationService.class);

    public CompletableFuture<String> test() {
        return CompletableFuture.supplyAsync(() -> "Hello Wolrd!");
    }

    @Async
    public CompletableFuture<Weather> weather(Geolocation geolocation) {
        return weather(geolocation.latitude() + "", geolocation.longitude() + "");
    }

    @Async
    public CompletableFuture<Weather> weather(String latitude, String longitude) {
        return weather(latitude, longitude, "metric");
    }

    @Async
    public CompletableFuture<Weather> weather(String latitude, String longitude, String units) {
        try {
            final URIBuilder uriBuilder = new URIBuilder(weatherApiUrl())
                    .addParameter("lat", latitude)
                    .addParameter("lon", longitude)
                    .addParameter("units", units)
                    .addParameter("appid", weatherApiKey());

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(uriBuilder.build())
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            final HttpResponse<String> result = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return CompletableFuture.supplyAsync(() -> result)
                    .thenApply(this::processResponse);

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Weather processResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            LOGGER.error("Could not fetch weather data!");
            return null;
        }
        System.out.println(response.body());
        final JsonObject result = GSON.fromJson(response.body(), JsonObject.class);

        final JsonObject weatherObject = result.getAsJsonArray("weather")
                .get(0)
                .getAsJsonObject();

        final JsonObject rainObject = result.getAsJsonObject("rain");

        final double rain = rainObject == null ? 0.0 : rainObject.get("1h").getAsDouble();

        final int cloudiness = result.getAsJsonObject("clouds")
                .get("all")
                .getAsInt();

        final WeatherInfo weatherInfo = new WeatherInfo(weatherObject.get("main").getAsString(),
                weatherObject.get("description").getAsString(),
                rain,
                cloudiness,
                result.get("visibility").getAsInt());

        final MainInfo mainInfo = GSON.fromJson(result.getAsJsonObject("main"), MainInfo.class);

        final WindInfo windInfo = GSON.fromJson(result.getAsJsonObject("wind"), WindInfo.class);

        return new Weather(weatherInfo, mainInfo, windInfo);
    }

    public List<Component> getWeatherComponents() {
        return weatherComponents;
    }

    public void setWeatherComponents(List<Component> weatherComponents) {
        this.weatherComponents = weatherComponents;
    }

}
