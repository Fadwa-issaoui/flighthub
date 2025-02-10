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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpdateFlightController {

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
    private Button updateButton;
    private Flight flight;


    @FXML
    public void initialize() {
        // Initialize time ComboBoxes
        initializeTimeComboBox(departureTimeComboBox);
        initializeTimeComboBox(arrivalTimeComboBox);

        //Style for create Button moved to fxml
        //  createButton.setStyle("-fx-background-color: #5067e9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        updateButton.setOnAction(event -> updateFlight());
    }


    private void initializeTimeComboBox(ComboBox<String> comboBox) {
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) { // Add every 30 minutes
                comboBox.getItems().add(String.format("%02d:%02d", hour, minute));
            }
        }
        comboBox.getSelectionModel().selectFirst(); // Set a default value
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        // Populate form fields with data
        flightNumberField.setText(flight.getFlightNumber());
        aircraftIdField.setText(String.valueOf(flight.getAircraftId()));
        departureAirportIdField.setText(String.valueOf(flight.getDepartureAirportId()));
        arrivalAirportIdField.setText(String.valueOf(flight.getArrivalAirportId()));

        //set date and time for departure
        LocalDateTime departureDateTime = LocalDateTime.parse(flight.getDepartureTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        departureDatePicker.setValue(departureDateTime.toLocalDate());
        departureTimeComboBox.setValue(departureDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        //set date and time for arrival
        LocalDateTime arrivalDateTime = LocalDateTime.parse(flight.getArrivalTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        arrivalDatePicker.setValue(arrivalDateTime.toLocalDate());
        arrivalTimeComboBox.setValue(arrivalDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));


        priceField.setText(String.valueOf(flight.getPrice()));
    }
    @FXML
    private void updateFlight() {
        try {
            flight.setFlightNumber(flightNumberField.getText());
            flight.setAircraftId(Integer.parseInt(aircraftIdField.getText()));
            flight.setDepartureAirportId(Integer.parseInt(departureAirportIdField.getText()));
            flight.setArrivalAirportId(Integer.parseInt(arrivalAirportIdField.getText()));
            // Get and format departure date and time
            LocalDate departureDate = departureDatePicker.getValue();
            String departureTime = departureTimeComboBox.getValue();

            if (departureDate == null || departureTime == null) {
                System.out.println("Please select both a date and a time for departure");
                return; // Prevent the creation of flight if date or time are not selected
            }
            flight.setDepartureTime(departureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + departureTime);

            // Get and format arrival date and time
            LocalDate arrivalDate = arrivalDatePicker.getValue();
            String arrivalTime = arrivalTimeComboBox.getValue();

            if (arrivalDate == null || arrivalTime == null) {
                System.out.println("Please select both a date and a time for arrival");
                return; // Prevent the creation of flight if date or time are not selected
            }
            flight.setArrivalTime(arrivalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + arrivalTime);
            flight.setPrice(Double.parseDouble(priceField.getText()));
            flightService.updateFlight(flight);
            System.out.println("Flight Updated!");
            closeDialog();
        }   catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values in the appropriate fields");
        }
        catch (Exception e) {
            System.out.println("Error creating flight: " + e.getMessage());
        }
    }

    // Method to close the dialog
    private void closeDialog() {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }
}