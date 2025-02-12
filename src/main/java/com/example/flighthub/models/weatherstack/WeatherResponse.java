package com.example.flighthub.models.weatherstack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true) // Important: Ignore extra fields
public class WeatherResponse {
    @JsonProperty("current") // Maps the "current" field in JSON to this field
    private CurrentWeather current;

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }
}