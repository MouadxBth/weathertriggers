package me.hanane.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vaadin.flow.component.Component;
import me.hanane.weather.info.MainInfo;
import me.hanane.weather.info.WeatherInfo;
import me.hanane.weather.info.WindInfo;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.util.*;

import static me.hanane.utilities.Utilities.weatherApiKey;
import static me.hanane.utilities.Utilities.weatherApiUrl;

@Service
public class WeatherService {

    private List<Component> weatherComponents = new ArrayList<>();

    private final Gson gson = new GsonBuilder().create();

    public Optional<Weather> weather(String latitude, String longitude) {
        return weather(latitude, longitude, "metric");
    }

    public Optional<Weather> weather(String latitude, String longitude, String units) {
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            final Weather weather = client.prepare("GET", weatherApiUrl())
                    .addQueryParam("lat", latitude)
                    .addQueryParam("lon", longitude)
                    .addQueryParam("units", units)
                    .addQueryParam("appid", weatherApiKey())
                    .execute()
                    .toCompletableFuture()
                    .thenApply(this::processResponse)
                    .join();
            return Optional.ofNullable(weather);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public List<Component> getWeatherComponents() {
        return weatherComponents;
    }

    public void setWeatherComponents(List<Component> weatherComponents) {
        this.weatherComponents = weatherComponents;
    }

    private Weather processResponse(Response response) {
        System.out.println(response.getResponseBody());
        final JsonObject result = gson.fromJson(response.getResponseBody(), JsonObject.class);

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

        final MainInfo mainInfo = gson.fromJson(result.getAsJsonObject("main"), MainInfo.class);

        final WindInfo windInfo = gson.fromJson(result.getAsJsonObject("wind"), WindInfo.class);

        return new Weather(weatherInfo, mainInfo, windInfo);
    }
}
