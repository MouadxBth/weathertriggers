package me.hanane.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static me.hanane.utilities.Utilities.*;

@Service
public class GeolocationService {

    public Optional<Geolocation> get(String city) {
        return get(city, "", "");
    }

    public Optional<Geolocation> get(String city, String state) {
        return get(city, state, "");
    }

    public Optional<Geolocation> get(String city, String state, String country) {
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            final BoundRequestBuilder requestBuilder = client.prepare("GET", geolocationApiUrl())
                    .setHeader("X-Api-Key", geolocationApiKey())
                    .addQueryParam("city", city);

            if (state != null && !state.isBlank())
                requestBuilder.addQueryParam("state", state);
            if (country != null && !country.isBlank())
                requestBuilder.addQueryParam("country", country);

            final JsonArray results = requestBuilder.execute()
                    .toCompletableFuture()
                    .thenApply(response -> GSON.fromJson(response.getResponseBody(), JsonArray.class))
                    .join();
            if (results == null || results.isEmpty())
                return Optional.empty();

            return Optional.of(GSON.fromJson(results.get(0).getAsJsonObject(), Geolocation.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }


}
