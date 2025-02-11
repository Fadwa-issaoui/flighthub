package com.example.flighthub.services;

import com.example.flighthub.models.weatherstack.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode; // Import for error checking
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private static final String API_KEY = "b43ec96eb3c583d4959bff34a7d504ac"; // Replace with YOUR API KEY!
    private static final String BASE_URL = "http://api.weatherstack.com/current";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherResult getCurrentWeather(String location) {
        String url = String.format("%s?access_key=%s&query=%s", BASE_URL, API_KEY, location);

        //Properly encode URL
        try {
            url = new URI(url).toASCIIString();
        }catch(Exception e){
            System.err.println("Error encoding URL" + e.getMessage());
            return new WeatherResult(null, 0, "Invalid URL"); // Return error result
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode(); // Get the status code

            if (statusCode == 200) {
                // Check for API-level errors *before* parsing as WeatherResponse
                JsonNode root = objectMapper.readTree(response.body());
                if (root.has("error")) {
                    String errorMessage = root.get("error").get("info").asText("API Error");
                    return new WeatherResult(null, statusCode, errorMessage);
                }

                try {
                    WeatherResponse weatherResponse = objectMapper.readValue(response.body(), WeatherResponse.class);
                    return new WeatherResult(weatherResponse, statusCode, null); // Success
                } catch (Exception e) {
                    System.err.println("Error parsing JSON: " + e.getMessage());
                    return new WeatherResult(null, statusCode, "Error parsing JSON: " + e.getMessage()); // JSON parsing error
                }
            } else {
                // Handle non-200 status codes
                return new WeatherResult(null, statusCode, "HTTP Error: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching weather: " + e.getMessage());
            return new WeatherResult(null, 0, "Connection Error: " + e.getMessage()); // Connection error
        }
    }
    //added a getter to use it in my project
    public String getApiKey() {
        return API_KEY;
    }

    // Inner class to hold the result (data + status code + error message)
    public static class WeatherResult {
        public final WeatherResponse weatherResponse;
        public final int statusCode;
        public final String errorMessage;

        public WeatherResult(WeatherResponse weatherResponse, int statusCode, String errorMessage) {
            this.weatherResponse = weatherResponse;
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
        }
    }
}