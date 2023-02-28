package me.hanane.location;

import com.google.gson.JsonArray;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.atmosphere.util.analytics.HTTPGetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static me.hanane.utilities.Utilities.*;

@Service
public class GeolocationService {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(GeolocationService.class);

    @Async
    public CompletableFuture<Geolocation> get(String city) {
        return get(city, "", "");
    }

    @Async
    public CompletableFuture<Geolocation> get(String city, String state) {
        return get(city, state, "");
    }

    @Async
    public CompletableFuture<Geolocation> get(String city, String state, String country) {
        try {
            final URIBuilder uriBuilder = new URIBuilder(geolocationApiUrl())
                    .addParameter("city", city);

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(uriBuilder.build())
                    .header("X-Api-Key", geolocationApiKey())
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            final HttpResponse<String> result = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return CompletableFuture.supplyAsync(() -> result)
                    .thenApply(response -> {
                        if (response.statusCode() != 200) {
                            LOGGER.error("Could not fetch geolocation data!");
                            return null;
                        }
                        try {
                            return GSON.fromJson(GSON.fromJson(response.body(), JsonArray.class)
                                            .get(0).getAsJsonObject(),
                                    Geolocation.class);
                        } catch (Exception e) {
                            LOGGER.error("Could not transform geolocation data to json!");
                            return null;
                        }
                    });

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
