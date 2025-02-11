package com.example.flighthub.controllers.flight;

import com.example.flighthub.models.Aircraft;
import com.example.flighthub.models.Airport;
import com.example.flighthub.models.Flight;
import com.example.flighthub.models.weatherstack.WeatherResponse;
import com.example.flighthub.services.AircraftService;
import com.example.flighthub.services.AirportService;
import com.example.flighthub.services.WeatherService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FlightDetailsPopupController {

    @FXML private Label flightIdLabel;
    @FXML private Label flightNumberLabel;
    @FXML private Label aircraftIdLabel;
    @FXML private Label departureTimeLabel;
    @FXML private Label arrivalTimeLabel;
    @FXML private Label priceLabel;
    @FXML private Label aircraftModelLabel;
    @FXML private Label departureAirportNameLabel;
    @FXML private Label arrivalAirportNameLabel;
    @FXML private HBox departureHbox;
    @FXML private HBox arrivalHbox;
    @FXML private Button closeButton;
    @FXML private Label departureWeatherLabel;
    @FXML private Label arrivalWeatherLabel;
    @FXML private ImageView departureWeatherIcon;
    @FXML private ImageView arrivalWeatherIcon;

    private final AirportService airportService = new AirportService();
    private final AircraftService aircraftService = new AircraftService();
    private final WeatherService weatherService = new WeatherService();


    @FXML
    public void initialize() {
        if (closeButton != null) {
            closeButton.setOnAction(event -> {
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            });
        }
    }

    public void setFlightData(Flight flight) {
        if (flight != null) {
            flightIdLabel.setText(String.valueOf(flight.getFlightId()));
            flightNumberLabel.setText(flight.getFlightNumber());
            aircraftIdLabel.setText(String.valueOf(flight.getAircraftId()));
            departureTimeLabel.setText(flight.getDepartureTime());
            arrivalTimeLabel.setText(flight.getArrivalTime());
            priceLabel.setText(String.format("%.2f", flight.getPrice()));

            Aircraft aircraft = aircraftService.getAircraftById(flight.getAircraftId());
            if (aircraft != null) {
                aircraftModelLabel.setText(aircraft.getModel());
            }

            Airport departureAirport = airportService.getAirportById(flight.getDepartureAirportId());
            Airport arrivalAirport = airportService.getAirportById(flight.getArrivalAirportId());

            if (departureAirport != null) {
                departureAirportNameLabel.setText(departureAirport.getName() + ", " + departureAirport.getLocation());

                // Fetch and display *current* weather for departure airport
                WeatherService.WeatherResult departureWeather = weatherService.getCurrentWeather(departureAirport.getLocation());
                displayWeatherData(departureWeather, departureWeatherLabel, departureWeatherIcon); // Pass ImageView
            }

            if (arrivalAirport != null) {
                arrivalAirportNameLabel.setText(arrivalAirport.getName() + ", " + arrivalAirport.getLocation());

                // Fetch and display *current* weather for arrival airport
                WeatherService.WeatherResult arrivalWeather = weatherService.getCurrentWeather(arrivalAirport.getLocation());
                displayWeatherData(arrivalWeather, arrivalWeatherLabel, arrivalWeatherIcon); // Pass ImageView
            }
        }
    }
    private ImageView createIcon(String imagePath){
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);
        return imageView;
    }

    // Helper method to display weather data (Modified)
    private void displayWeatherData(WeatherService.WeatherResult weatherResult, Label weatherLabel, ImageView weatherIcon) {
        if (weatherResult != null) {
            if (weatherResult.errorMessage != null) {
                // Display error message
                weatherLabel.setText("Error: " + weatherResult.errorMessage);
                weatherIcon.setImage(null); // Clear any previous icon
            } else if (weatherResult.weatherResponse != null && weatherResult.weatherResponse.getCurrent() != null) {
                // Display weather information
                String description = weatherResult.weatherResponse.getCurrent().getWeatherDescriptions().get(0);
                double temperature = weatherResult.weatherResponse.getCurrent().getTemperature();
                String iconUrl = weatherResult.weatherResponse.getCurrent().getWeatherIcons().get(0);

                weatherLabel.setText(String.format("%s, %.1f°C", description, temperature));

                // Load and display the weather icon
                try {
                    Image iconImage = new Image(iconUrl);
                    weatherIcon.setImage(iconImage);
                } catch (Exception e) {
                    System.err.println("Error loading weather icon: " + e.getMessage());
                    weatherIcon.setImage(null); // Clear icon on error
                }
            } else {
                weatherLabel.setText("Weather data unavailable.");
                weatherIcon.setImage(null);
            }
        } else {
            weatherLabel.setText("Weather data unavailable.");
            weatherIcon.setImage(null);
        }
    }
}