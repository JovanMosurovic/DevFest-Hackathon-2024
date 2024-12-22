package com.devfesthackathon.devfesthackathon.app;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class WeatherTrack {

    private static final Logger logger = Logger.getLogger(WeatherTrack.class.getName());

    public static final String BASE_WEATHER_API = "https://api.open-meteo.com/v1/forecast";
    public static final String LOCATION_API = "http://ip-api.com/json/";

    private final LocationService locationService;
    private final WeatherService weatherService;

    public WeatherTrack() {
        this.locationService = new LocationService();
        this.weatherService = new WeatherService();
    }

    public static void main(String[] args) {
        WeatherTrack weatherTracker = new WeatherTrack();
        try {
            weatherTracker.displayWeatherData();
        } catch (Exception e) {
            logger.severe("Error displaying weather data: " + e.getMessage());
        }
    }

    private void displayWeatherData() throws IOException {
        Location location = locationService.getCurrentLocation();
        System.out.println("Retrieved location: " + location);

        List<Double> dailyAverages = weatherService.getDailyAverageTemperatures(location);

        if (dailyAverages.isEmpty()) {
            System.out.println("No temperature data available");
            return;
        }

        for (int i = 0; i < dailyAverages.size(); i++) {
            System.out.printf("Day %d Average Temperature: %.2f °C%n", i + 1, dailyAverages.get(i));
        }
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public WeatherService getWeatherService() {
        return weatherService;
    }
}

