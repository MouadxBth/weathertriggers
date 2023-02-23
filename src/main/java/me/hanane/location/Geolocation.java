package me.hanane.location;

import com.google.gson.annotations.SerializedName;

public record Geolocation(double longitude,
                          double latitude,
                          @SerializedName("name")
                          String city,
                          String state,
                          String country) {

}
