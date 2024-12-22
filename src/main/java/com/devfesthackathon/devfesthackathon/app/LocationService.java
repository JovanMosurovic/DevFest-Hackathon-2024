package com.devfesthackathon.devfesthackathon.app;

import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class LocationService {
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
