package io.weather.weather_by_ip;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherAppService {

    private final Cache<String, String> ipCache = CacheBuilder.newBuilder().build();
    private final Cache<String, String> weatherCache = CacheBuilder.newBuilder().build();

    public String getIpAddress() {
        String ip = ipCache.getIfPresent("ip");
        if (ip != null) {
            return ip;
        }
        try {
            ip = getResponseFromUrl("https://checkip.amazonaws.com");
            ipCache.put("ip", ip);
            return ip;
        } catch (Exception e) {
            System.out.println("Error getting IP address: " + e.getMessage());
            return null;
        }
    }

    String getWeatherDataForIP(String ip, GeoLocation location) {
        String weather = weatherCache.getIfPresent(ip);
        if (weather != null) {
            return weather;
        }
        try {
            String apiKey = "b7ac11460e074565a01f454656d01bd4&";
            String urlString = "https://api.weatherbit.io/v2.0/current?lat=" + location.getLatitude()
                    + "&lon=" + location.getLongitude() + "&key=" + apiKey + "&include=minutely";
            String response = getResponseFromUrl(urlString);
            JSONObject json = new JSONObject(response);
            JSONArray dataArray = json.getJSONArray("data");
            JSONObject data = dataArray.getJSONObject(0);
            double temperature = data.getDouble("temp");
            int humidity = data.getInt("rh");
            JSONObject weatherData = data.getJSONObject("weather");
            String description = weatherData.getString("description");
            weather = new WeatherData(temperature, humidity, description).toString();
            weatherCache.put(ip, weather);
            return weather;
        } catch (IOException e) {
            System.out.println("Error retrieving weather data for IP: " + ip + ": " + e.getMessage());
            return "An error occurred while retrieving the weather data. Please try again later.";
        }
    }

    public GeoLocation getLocationFromIp(String ip) {
        try {
            String url = "https://ipapi.co/" + ip + "/json/";
            String response = getResponseFromUrl(url);
            JSONObject json = new JSONObject(response);
            double longitude = json.getDouble("longitude");
            double latitude = json.getDouble("latitude");
            String city = json.getString("city");
            String country = json.getString("country");
            return new GeoLocation(latitude, longitude, city, country);
        } catch (IOException e) {
            System.out.println("Error retrieving location for IP: " + ip + ": " + e.getMessage());
            return null;
        }
    }

    private String getResponseFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
