package com.example.flighthub.controllers.flight;
import com.example.flighthub.models.Flight;
import com.example.flighthub.services.FlightService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CreateFlightController {

    private final FlightService flightService = new FlightService();

    @FXML
    private TextField flightNumberField;
    @FXML
    private TextField aircraftIdField;
    @FXML
    private TextField departureAirportIdField;
    @FXML
    private TextField arrivalAirportIdField;
    @FXML
    private DatePicker departureDatePicker;
    @FXML
    private ComboBox<String> departureTimeComboBox;
    @FXML
    private DatePicker arrivalDatePicker;
    @FXML
    private ComboBox<String> arrivalTimeComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private Button createButton;

    @FXML
    public void initialize() {
        // Initialize time ComboBoxes
        initializeTimeComboBox(departureTimeComboBox);
        initializeTimeComboBox(arrivalTimeComboBox);

        //Style for create Button moved to fxml
        //  createButton.setStyle("-fx-background-color: #5067e9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        createButton.setOnAction(event -> createFlight());
    }


    private void initializeTimeComboBox(ComboBox<String> comboBox) {
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) { // Add every 30 minutes
                comboBox.getItems().add(String.format("%02d:%02d", hour, minute));
            }
        }
        comboBox.getSelectionModel().selectFirst(); // Set a default value
    }

    @FXML
    private void createFlight() {
        try {
            Flight newFlight = new Flight();
            newFlight.setFlightNumber(flightNumberField.getText());
            newFlight.setAircraftId(Integer.parseInt(aircraftIdField.getText()));
            newFlight.setDepartureAirportId(Integer.parseInt(departureAirportIdField.getText()));
            newFlight.setArrivalAirportId(Integer.parseInt(arrivalAirportIdField.getText()));

            // Get and format departure date and time
            LocalDate departureDate = departureDatePicker.getValue();
            String departureTime = departureTimeComboBox.getValue();

            if (departureDate == null || departureTime == null) {
                System.out.println("Please select both a date and a time for departure");
                return; // Prevent the creation of flight if date or time are not selected
            }
            newFlight.setDepartureTime(departureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + departureTime);

            // Get and format arrival date and time
            LocalDate arrivalDate = arrivalDatePicker.getValue();
            String arrivalTime = arrivalTimeComboBox.getValue();

            if (arrivalDate == null || arrivalTime == null) {
                System.out.println("Please select both a date and a time for arrival");
                return; // Prevent the creation of flight if date or time are not selected
            }
            newFlight.setArrivalTime(arrivalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+ " " +arrivalTime);

            newFlight.setPrice(Double.parseDouble(priceField.getText()));

            flightService.createFlight(newFlight);

            System.out.println("Flight Created!");
            closeDialog();

        }  catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values in the appropriate fields");
        }
        catch (Exception e) {
            System.out.println("Error creating flight: " + e.getMessage());
        }
    }
    // Method to close the dialog
    private void closeDialog() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}