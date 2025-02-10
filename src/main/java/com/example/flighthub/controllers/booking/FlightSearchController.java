package com.example.flighthub.controllers.booking;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class FlightSearchController {

    @FXML
    private ChoiceBox<String> departureChoiceBox;
    @FXML
    private ChoiceBox<String> destinationChoiceBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label flightDepHeure;
    @FXML
    private Label flightArrivHeure;
    @FXML
    private Label flightPrice;
    @FXML
    private Label numberFlights;
    @FXML
    private Label noFlightsLabel;
    @FXML
    private Button searchAgainButton;



    private String departure;
    private String destination;
    private LocalDate date;

    public void initialize() {
        reloadLocations(); // Load locations when the page loads
    }

    public void setFlightData(String departure, String destination, LocalDate date) {
        this.departure = departure;
        this.destination = destination;
        this.date = date;

        departureChoiceBox.setValue(departure);
        destinationChoiceBox.setValue(destination);
        datePicker.setValue(date);
        reloadLocations();
        fetchFlightData(); // Fetch flight details based on the input
    }

    public void fetchFlightData() {
        String query = "SELECT TIME(f.departureTime) AS departureTime, " +"TIME(f.arrivalTime) AS arrivalTime, " +"f.price " +"FROM flight f " +"JOIN airport a1 ON f.departureAirportId = a1.airportId " +"JOIN airport a2 ON f.arrivalAirportId = a2.airportId " +"WHERE a1.location = ? AND a2.location = ? " +"AND DATE(f.departureTime) = ?";
        try (
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, departure);
            stmt.setString(2, destination);
            stmt.setString(3, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            try (ResultSet rs = stmt.executeQuery()) {
                int flightCount = 0;

                while (rs.next()) {
                    flightCount++;

                    // If it's the first flight, display the details
                    if (flightCount == 1) {
                        flightDepHeure.setText(rs.getString("departureTime"));
                        flightArrivHeure.setText(rs.getString("arrivalTime"));
                        flightPrice.setText(rs.getString("price") + " DT");
                        noFlightsLabel.setVisible(false);
                    }
                }

                // Update the numberFlights label
                numberFlights.setText(String.valueOf(flightCount));

                // If no flights were found, set "N/A"
                if (flightCount == 0) {
                    flightDepHeure.setText("N/A");
                    flightArrivHeure.setText("N/A");
                    flightPrice.setText("N/A");
                    numberFlights.setText("0");
                    noFlightsLabel.setText("No flights available for this date.");
                    noFlightsLabel.setVisible(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void reloadLocations() {
        ObservableList<String> locations = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT location FROM airport");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("location"));
            }

            // Repopulate the choice boxes with the fetched locations
            departureChoiceBox.setItems(locations);
            destinationChoiceBox.setItems(locations);

            // Set the current values after reloading
            departureChoiceBox.setValue(departure);
            destinationChoiceBox.setValue(destination);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load locations from the database.");
        }

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  // You can change the Alert type based on need (e.g., INFORMATION, WARNING, ERROR)
        alert.setTitle(title);  // Set the title of the alert
        alert.setHeaderText(null);  // No header text
        alert.setContentText(message);  // The message to display

        // Show and wait for the user to close the alert
        alert.showAndWait();
    }

    // Helper method to show error messages
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);  // For error messages, use the ERROR alert type
        alert.setTitle(title);  // Set the title of the error dialog
        alert.setHeaderText("An Error Occurred");  // Optional header text, you can change this if needed
        alert.setContentText(message);  // The error message to display

        // Show and wait for the user to close the alert
        alert.showAndWait();
    }
    @FXML
    private void handleSearchAgainButtonAction() {
        // Get the new selected values from the ChoiceBox and DatePicker
        String departureLocation = departureChoiceBox.getValue();
        String destinationLocation = destinationChoiceBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (departureLocation == null || destinationLocation == null || selectedDate == null) {
            showAlert("Input Error", "Please select all fields before searching!");
            return;
        }

        // Update the flight data with the new values
        setFlightData(departureLocation, destinationLocation, selectedDate);



    }

}