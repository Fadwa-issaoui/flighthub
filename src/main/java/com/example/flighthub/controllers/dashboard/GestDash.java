package com.example.flighthub.controllers.dashboard;

import com.example.flighthub.services.AircraftService;
import com.example.flighthub.services.AirportService;
import com.example.flighthub.services.FlightService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GestDash {

    @FXML private Button flightsButton;
    @FXML private Button carsButton;
    @FXML private PieChart departurePieChart;
    @FXML private BarChart<String, Number> flightsPerMonthChart; // Use String for month names
    @FXML private Label totalFlightsLabel;
    @FXML private Label totalAircraftsLabel;
    @FXML private Label totalAirportsLabel;
    @FXML private Label averageFlightPriceLabel;
    @FXML private Label mostPopularDestinationLabel;
    // Remove: @FXML private LineChart<String, Number> flightsPerDayChart;  // No longer needed
    // Remove: @FXML private StackedBarChart<String, String> departureArrivalChart; // No longer needed

    // NEW LABELS:
    @FXML private Label availableAircraftLabel;
    @FXML private Label flightsDoneLabel;
    @FXML private Label flightsNotDoneLabel;


    private FlightService flightService = new FlightService();
    private AirportService airportService = new AirportService();
    private AircraftService aircraftService = new AircraftService();

    @FXML
    public void initialize() {
        flightsButton.setOnAction(event -> loadScene("/FlightHub/SceneBuilder/Flights.fxml", "Flights"));
        carsButton.setOnAction(event -> loadScene("/FlightHub/SceneBuilder/Car.fxml", "Cars"));

        // --- Populate Charts and Statistics ---
        setupDeparturePieChart();
        setupFlightsPerMonthChart();
        setTotalFlightsLabel();
        setTotalAircraftsLabel();
        setTotalAirportsLabel();
        setAverageFlightPriceLabel();
        setMostPopularDestinationLabel();
        //setupFlightsPerDayChart();  //Removed
        //setupDepartureArrivalChart(); //Removed
        setAvailableAircraftLabel(); // New
        setFlightsDoneLabel();       // New
        setFlightsNotDoneLabel();    // New
    }

    private void setTotalFlightsLabel() {
        //the same code
        int totalFlights = flightService.getAllFlights().size();
        totalFlightsLabel.setText(String.valueOf(totalFlights));
    }
    private void setTotalAircraftsLabel(){
        //the same code
        int totalAircrafts = aircraftService.getAllAircrafts().size();
        totalAircraftsLabel.setText(String.valueOf(totalAircrafts));
    }
    private void setTotalAirportsLabel(){
        //the same code
        int totalAirports = airportService.getAllAirports().size();
        totalAirportsLabel.setText(String.valueOf(totalAirports));
    }

    private void setupDeparturePieChart() {
        //the same code
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        Map<String, Integer> departuresByLocation = flightService.getFlightCountsByDepartureLocation();

        for (Map.Entry<String, Integer> entry : departuresByLocation.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        departurePieChart.setData(pieChartData);
        departurePieChart.setTitle("Flights by Departure Location"); // Add a title
    }
    private void setupFlightsPerMonthChart() {
        //the same code
        Map<Month, Long> flightsByMonth = flightService.getFlightCountsByMonth();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Flights"); // Set the series name for the legend

        for (Month month : Month.values()) {
            String monthName = month.toString();
            long count = flightsByMonth.getOrDefault(month, 0L);
            series.getData().add(new XYChart.Data<>(monthName, count));
        }

        flightsPerMonthChart.getData().clear(); // Clear previous data (important!)
        flightsPerMonthChart.getData().add(series);
        flightsPerMonthChart.setTitle("Flights Per Month");
        flightsPerMonthChart.setLegendVisible(true); // Make sure legend is visible
    }


    private void setAverageFlightPriceLabel() {
        //the same code
        double averagePrice = flightService.getAverageFlightPrice();
        averageFlightPriceLabel.setText(String.format("%.2f", averagePrice));
    }

    private void setMostPopularDestinationLabel() {
        //the same code
        String destination = flightService.getMostPopularDestination();
        mostPopularDestinationLabel.setText(destination);
    }
    // New method to set the available aircraft label
    private void setAvailableAircraftLabel() {
        int availableAircraft = aircraftService.getAvailableAircraftCount(); // You'll implement this
        availableAircraftLabel.setText(String.valueOf(availableAircraft));
    }

    // New method to set the flights done label
    private void setFlightsDoneLabel() {
        int flightsDone = flightService.getCompletedFlightsCount(); // You'll implement this
        flightsDoneLabel.setText(String.valueOf(flightsDone));
    }

    // New method to set the flights not done label
    private void setFlightsNotDoneLabel() {
        int flightsNotDone = flightService.getFlightsNotCompletedCount(); // You'll implement this.
        flightsNotDoneLabel.setText(String.valueOf(flightsNotDone));
    }

    private void loadScene(String fxmlPath, String title) {
        //the same code
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) flightsButton.getScene().getWindow(); // Get current stage
            stage.setScene(new Scene(root));
            stage.setTitle(title); // Set the title of the new scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
            System.err.println("Error loading FXML: " + e.getMessage());
        }
    }
}