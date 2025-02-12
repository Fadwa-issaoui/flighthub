package com.example.flighthub.controllers.flight;

import com.example.flighthub.models.Aircraft;
import com.example.flighthub.models.Airport;
import com.example.flighthub.models.Flight;
import com.example.flighthub.services.AircraftService;
import com.example.flighthub.services.AirportService;
import com.example.flighthub.services.FlightService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateFlightController {

    private final FlightService flightService = new FlightService();
    private final AircraftService aircraftService = new AircraftService();
    private final AirportService airportService = new AirportService();

    @FXML private TextField flightNumberField;
    @FXML private Label flightNumberErrorLabel; // Error label for flight number

    @FXML private ComboBox<Aircraft> aircraftComboBox;
    @FXML private Label aircraftErrorLabel;

    @FXML private ComboBox<Airport> departureAirportComboBox;
    @FXML private Label departureAirportErrorLabel;

    @FXML private ComboBox<Airport> arrivalAirportComboBox;
    @FXML private Label arrivalAirportErrorLabel;

    @FXML private DatePicker departureDatePicker;
    @FXML private Label departureDateErrorLabel;

    @FXML private ComboBox<String> departureTimeComboBox;
    @FXML private Label departureTimeErrorLabel;

    @FXML private DatePicker arrivalDatePicker;
    @FXML private Label arrivalDateErrorLabel;

    @FXML private ComboBox<String> arrivalTimeComboBox;
    @FXML private Label arrivalTimeErrorLabel;

    @FXML private TextField priceField;
    @FXML private Label priceErrorLabel; // Error label for price

    @FXML private Button createButton;

    private Map<Control, Label> errorLabelMap = new HashMap<>(); // Map to store control-error label pairs

    @FXML
    public void initialize() {
        initializeTimeComboBox(departureTimeComboBox);
        initializeTimeComboBox(arrivalTimeComboBox);
        createButton.setOnAction(event -> createFlight());
        //Initialize error label map
        initializeErrorLabelMap();
        // Load initial Airport data
        loadAirportData();

        // Set up StringConverters
        setupAircraftComboBoxConverter();
        setupAirportComboBoxConverter(departureAirportComboBox);
        setupAirportComboBoxConverter(arrivalAirportComboBox);

        // Initially disable the aircraft ComboBox
        aircraftComboBox.setDisable(true);
        aircraftComboBox.setPromptText("Select Departure Airport First");

        // Add a listener to the departure airport ComboBox
        departureAirportComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadAvailableAircraft(newVal.getAirportId());
                aircraftComboBox.setDisable(false);
            } else {
                aircraftComboBox.setItems(FXCollections.observableArrayList());
                aircraftComboBox.setDisable(true);
                aircraftComboBox.setPromptText("Select Departure Airport First");
            }
            // Validate departure airport when selection changes
            validateDepartureAirport();
        });

        // Add listeners for real-time validation
        flightNumberField.textProperty().addListener((obs, oldVal, newVal) -> validateFlightNumber());
        aircraftComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateAircraft());
        arrivalAirportComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateArrivalAirport());
        departureDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime());
        departureTimeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime());
        arrivalDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime());
        arrivalTimeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime());
        priceField.textProperty().addListener((obs, oldVal, newVal) -> validatePrice());
    }
    private void initializeErrorLabelMap() {
        errorLabelMap.put(flightNumberField, flightNumberErrorLabel);
        errorLabelMap.put(aircraftComboBox, aircraftErrorLabel);
        errorLabelMap.put(departureAirportComboBox, departureAirportErrorLabel);
        errorLabelMap.put(arrivalAirportComboBox, arrivalAirportErrorLabel);
        errorLabelMap.put(departureDatePicker, departureDateErrorLabel);
        errorLabelMap.put(departureTimeComboBox, departureTimeErrorLabel);
        errorLabelMap.put(arrivalDatePicker, arrivalDateErrorLabel);
        errorLabelMap.put(arrivalTimeComboBox, arrivalTimeErrorLabel);
        errorLabelMap.put(priceField, priceErrorLabel);
    }

    private void initializeTimeComboBox(ComboBox<String> comboBox) {
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                comboBox.getItems().add(String.format("%02d:%02d", hour, minute));
            }
        }
        comboBox.getSelectionModel().selectFirst();
    }
    private void loadAvailableAircraft(int departureAirportId) {
        List<Aircraft> availableAircraft = aircraftService.getAvailableAircraftForDepartureAirport(departureAirportId);
        aircraftComboBox.setItems(FXCollections.observableArrayList(availableAircraft));
        if (availableAircraft.isEmpty()) {
            aircraftComboBox.setPromptText("No aircraft available");
        } else {
            aircraftComboBox.setPromptText("Select Aircraft");
            aircraftComboBox.getSelectionModel().selectFirst(); //Auto select first item
        }
    }
    private void loadAirportData() {
        List<Airport> airports = airportService.getAllAirports();
        departureAirportComboBox.setItems(FXCollections.observableArrayList(airports));
        arrivalAirportComboBox.setItems(FXCollections.observableArrayList(airports));
    }
    private void setupAircraftComboBoxConverter() {
        aircraftComboBox.setConverter(new StringConverter<Aircraft>() {
            @Override
            public String toString(Aircraft aircraft) {
                return aircraft == null ? null : aircraft.getModel();
            }

            @Override
            public Aircraft fromString(String string) {
                return aircraftComboBox.getItems().stream()
                        .filter(a -> a.getModel().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
    private void setupAirportComboBoxConverter(ComboBox<Airport> comboBox) {
        comboBox.setConverter(new StringConverter<Airport>() {
            @Override
            public String toString(Airport airport) {
                return airport == null ? null : airport.getName();
            }

            @Override
            public Airport fromString(String string) {
                return comboBox.getItems().stream()
                        .filter(a -> a.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    private void createFlight() {
        //the same code
        if (validateForm()) { // Only create if validation passes
            try {
                Flight newFlight = new Flight();
                newFlight.setFlightNumber(flightNumberField.getText());

                Aircraft selectedAircraft = aircraftComboBox.getValue();
                newFlight.setAircraftId(selectedAircraft.getAircraftId());

                Airport selectedDepartureAirport = departureAirportComboBox.getValue();
                newFlight.setDepartureAirportId(selectedDepartureAirport.getAirportId());

                Airport selectedArrivalAirport = arrivalAirportComboBox.getValue();
                newFlight.setArrivalAirportId(selectedArrivalAirport.getAirportId());

                LocalDate departureDate = departureDatePicker.getValue();
                String departureTime = departureTimeComboBox.getValue();
                newFlight.setDepartureTime(departureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + departureTime);

                LocalDate arrivalDate = arrivalDatePicker.getValue();
                String arrivalTime = arrivalTimeComboBox.getValue();
                newFlight.setArrivalTime(arrivalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + arrivalTime);

                newFlight.setPrice(Double.parseDouble(priceField.getText()));

                int flightId = flightService.createFlight(newFlight);
                if (flightId != -1) {
                    if (!aircraftService.updateAircraftFlightId(selectedAircraft.getAircraftId(), flightId)) {
                        System.err.println("Failed to update aircraft with flight ID.");
                    }
                } else {
                    System.err.println("Failed to create flight.");
                }
                System.out.println("Flight Created!");
                closeDialog();

            } catch (NumberFormatException e) {
                showAlert("Input Error", "Invalid input. Please enter numeric values in appropriate fields.");
            } catch (Exception e) {
                showAlert("Input Error", "An unexpected error occurred: " + e.getMessage());
            }
        }
    }


    private boolean validateForm() {
        //the same code
        boolean isValid = true;
        isValid &= validateFlightNumber();
        isValid &= validateAircraft();
        isValid &= validateDepartureAirport();
        isValid &= validateArrivalAirport();
        isValid &= validateDateTime();
        isValid &= validatePrice();
        return isValid;
    }
    private boolean validateFlightNumber() {
        //the same code
        String flightNumber = flightNumberField.getText().trim();
        if (flightNumber.isEmpty()) {
            setError(flightNumberField, "Flight Number cannot be empty.");
            return false;
        }
        try {
            int flightNum = Integer.parseInt(flightNumber);
            if (flightNum < 0) {
                setError(flightNumberField, "Flight Number cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            setError(flightNumberField, "Invalid Flight Number format.");
            return false;
        }
        if (isFlightNumberExists(flightNumber)) {
            setError(flightNumberField, "Flight Number already exists.");
            return false;
        }
        clearError(flightNumberField);
        return true;
    }
    private boolean validateAircraft() {
        //the same code
        if (aircraftComboBox.getValue() == null) {
            setError(aircraftComboBox, "Please select an aircraft.");
            return false;
        }
        clearError(aircraftComboBox);
        return true;
    }
    private boolean validateDepartureAirport() {
        //the same code
        if (departureAirportComboBox.getValue() == null) {
            setError(departureAirportComboBox, "Please select a departure airport.");
            return false;
        }
        clearError(departureAirportComboBox);
        return true;
    }
    private boolean validateArrivalAirport() {
        //the same code
        Airport selectedDepartureAirport = departureAirportComboBox.getValue();
        Airport selectedArrivalAirport = arrivalAirportComboBox.getValue();

        if (selectedArrivalAirport == null) {
            setError(arrivalAirportComboBox, "Please select an arrival airport.");
            return false;
        }

        if (selectedDepartureAirport != null && selectedArrivalAirport != null &&
                selectedDepartureAirport.getAirportId() == selectedArrivalAirport.getAirportId()) {
            setError(arrivalAirportComboBox, "Departure and arrival airports cannot be the same.");
            return false;
        }

        clearError(arrivalAirportComboBox);
        return true;
    }
    private boolean validateDateTime() {
        LocalDate departureDate = departureDatePicker.getValue();
        String departureTime = departureTimeComboBox.getValue();
        LocalDate arrivalDate = arrivalDatePicker.getValue();
        String arrivalTime = arrivalTimeComboBox.getValue();

        if (departureDate == null || departureTime == null || arrivalDate == null || arrivalTime == null) {
            setError(departureDatePicker, "Select date and time.");
            return false;
        }

        try {
            LocalDateTime departureDateTime = LocalDateTime.parse(departureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + departureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + arrivalTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            if (arrivalDateTime.isBefore(departureDateTime)) {
                setError(arrivalDatePicker, "Arrival cannot be before departure.");
                return false;
            }
            if (departureDateTime.isBefore(LocalDateTime.now())) {
                setError(departureDatePicker, "Departure cannot be in the past.");
                return false;
            }
            // Additional check: Departure and arrival cannot be at the same time
            if (departureDateTime.equals(arrivalDateTime)) {
                setError(arrivalDatePicker, "Departure and arrival cannot be at the same time.");
                return false;
            }

        } catch (DateTimeParseException e) {
            setError(departureDatePicker, "Invalid date/time format.");
            return false;
        }
        clearError(departureDatePicker);
        clearError(departureTimeComboBox);
        clearError(arrivalDatePicker);
        clearError(arrivalTimeComboBox);

        return true;
    }
    private boolean validatePrice() {
        //the same code
        try {
            double price = Double.parseDouble(priceField.getText());
            if (price <= 0) {
                setError(priceField, "Price must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            setError(priceField, "Invalid price format.");
            return false;
        }
        clearError(priceField);
        return true;
    }

    private void setError(Control control, String message) {
        //the same code
        control.setStyle("-fx-border-color: red;");
        Label errorLabel = errorLabelMap.get(control);
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true); // Make the error label visible
        }
    }

    private void clearError(Control control) {
        //the same code
        control.setStyle(""); // Reset to default style
        Label errorLabel = errorLabelMap.get(control);
        if (errorLabel != null) {
            errorLabel.setText(""); // Clear the error message
            errorLabel.setVisible(false);  //hide the error label
        }
    }
    private boolean isFlightNumberExists(String flightNumber) {
        //the same code
        List<Flight> flights = flightService.getAllFlights();
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return true;
            }
        }
        return false;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}