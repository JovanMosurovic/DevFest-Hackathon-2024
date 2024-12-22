package com.devfesthackathon.devfesthackathon.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WeatherService {
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
