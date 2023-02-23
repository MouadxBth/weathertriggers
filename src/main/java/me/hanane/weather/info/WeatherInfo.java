package me.hanane.weather.info;


public record WeatherInfo(
        String name,
        String description,
        double rainLastHour,
        int cloudiness,
        int visibility) {


}
