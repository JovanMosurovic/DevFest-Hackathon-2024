package com.devfesthackathon.devfesthackathon.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
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
}

class Location {
    private final double latitude;
    private final double longitude;
    private final String city;
    private final String country;

    public Location(double latitude, double longitude, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%.4f, %.4f)", city, country, latitude, longitude);
    }
}

class LocationService {
    private final Gson gson = new Gson();

    public Location getCurrentLocation() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(WeatherTrack.LOCATION_API);
            String jsonResponse = EntityUtils.toString(httpClient.execute(request).getEntity());
            System.out.println("Location API Response: " + jsonResponse);

            Map locationData = gson.fromJson(jsonResponse, Map.class);

            if (locationData.get("status").toString().equals("fail")) {
                System.err.println("Location API failed: " + locationData.get("message"));
                return getDefaultLocation();
            }

            return new Location(
                    Double.parseDouble(locationData.get("lat").toString()),
                    Double.parseDouble(locationData.get("lon").toString()),
                    locationData.get("city").toString(),
                    locationData.get("country").toString()
            );
        } catch (Exception e) {
            System.err.println("Error getting location: " + e.getMessage());
            return getDefaultLocation();
        }
    }

    private Location getDefaultLocation() {
        return new Location(44.8167, 20.4667, "Belgrade", "Serbia");
    }
}

class WeatherService {
    private static final Logger logger = Logger.getLogger(WeatherService.class.getName());

    private final Gson gson = new Gson();

    public List<Double> getDailyAverageTemperatures(Location location) throws IOException {
        String weatherData = fetchWeatherData(location);
        System.out.println("Weather API Response: " + weatherData);

        try {
            return calculateDailyAverages(parseTemperatures(weatherData));
        } catch (Exception e) {
            System.err.println("Error processing weather data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private String fetchWeatherData(Location location) throws IOException {
        String apiUrl = String.format("%s?latitude=%s&longitude=%s&hourly=temperature_2m",
                WeatherTrack.BASE_WEATHER_API, location.getLatitude(), location.getLongitude());

        System.out.println("Fetching weather from: " + apiUrl);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);
            return EntityUtils.toString(httpClient.execute(request).getEntity());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Double> parseTemperatures(String jsonResponse) {
        try {
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseData = gson.fromJson(jsonResponse, mapType);

            if (!responseData.containsKey("hourly")) {
                System.err.println("No 'hourly' data in response");
                return new ArrayList<>();
            }

            Map<String, List<Double>> hourly = (Map<String, List<Double>>) responseData.get("hourly");

            if (!hourly.containsKey("temperature_2m")) {
                System.err.println("No temperature data in hourly data");
                return new ArrayList<>();
            }

            return hourly.get("temperature_2m");
        } catch (Exception e) {
            logger.severe("Error parsing temperature data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Double> calculateDailyAverages(List<Double> hourlyTemperatures) {
        if (hourlyTemperatures.isEmpty()) {
            return new ArrayList<>();
        }

        List<Double> dailyAverages = new ArrayList<>();
        int hoursPerDay = 24;

        for (int i = 0; i < hourlyTemperatures.size(); i += hoursPerDay) {
            double sum = 0;
            int count = 0;

            for (int j = i; j < Math.min(i + hoursPerDay, hourlyTemperatures.size()); j++) {
                if (hourlyTemperatures.get(j) != null) {
                    sum += hourlyTemperatures.get(j);
                    count++;
                }
            }

            if (count > 0) {
                dailyAverages.add(sum / count);
            }
        }

        return dailyAverages;
    }
}