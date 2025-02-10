package com.example.flighthub.controllers.booking;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AgentDashboardController {

    @FXML
    private ChoiceBox<String> departureChoiceBox;

    @FXML
    private ChoiceBox<String> destinationChoiceBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button searchButton;

    @FXML
    public void initialize() {
        loadLocations();
    }

    private void loadLocations() {
        ObservableList<String> locations = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT location FROM airport");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("location"));
            }

            departureChoiceBox.setItems(locations);
            destinationChoiceBox.setItems(locations);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load locations from the database.");
        }
    }

    @FXML
    private void handleSearchButtonAction() {
        String departureLocation = departureChoiceBox.getValue();
        String destinationLocation = destinationChoiceBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (departureLocation == null || destinationLocation == null || selectedDate == null) {
            showAlert("Input Error", "Please select all fields before searching!");
            return;
        }
        System.out.println("Searching for flights...");
        System.out.println("Departure: " + departureLocation);
        System.out.println("Destination: " + destinationLocation);
        System.out.println("Date: " + selectedDate);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_search.fxml"));
            Parent root = loader.load();

            // Retrieve the already instantiated controller
            FlightSearchController searchController = loader.getController();
            if (searchController != null) {
                System.out.println("Controller loaded successfully!");
                searchController.setFlightData(departureLocation, destinationLocation, selectedDate);
            } else {
                System.out.println("Error: FlightSearchController is NULL!");
            }

            // Switch scene
            Stage stage = (Stage) searchButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Loading Error", "Could not load the search page!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
