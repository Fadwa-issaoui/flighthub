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

public class UpdateFlightController {

    private final FlightService flightService = new FlightService();
    private final AircraftService aircraftService = new AircraftService();
    private final AirportService airportService = new AirportService();

    @FXML private TextField flightNumberField;
    @FXML private Label flightNumberErrorLabel;

    @FXML private ComboBox<Aircraft> aircraftComboBox;
    @FXML private Label aircraftErrorLabel;

    @FXML private ComboBox<Airport> departureAirportComboBox;
    @FXML private Label departureAirportErrorLabel; // Keep, even though disabled

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
    @FXML private Label priceErrorLabel;

    @FXML private Button updateButton;
    private Flight flight;
    private int originalAircraftId;
    private boolean isPastFlight = false; // Flag to indicate if the flight is in the past

    private Map<Control, Label> errorLabelMap = new HashMap<>();

    @FXML
    public void initialize() {
        initializeTimeComboBox(departureTimeComboBox);
        initializeTimeComboBox(arrivalTimeComboBox);
        updateButton.setOnAction(event -> updateFlight());

        // Load Aircraft and Airport data
        loadAircraftData();
        loadAirportData();
        //Initialize the error map
        initializeErrorLabelMap();

        // Set up StringConverters
        setupAircraftComboBoxConverter();
        setupAirportComboBoxConverter(departureAirportComboBox);
        setupAirportComboBoxConverter(arrivalAirportComboBox);

        departureAirportComboBox.setDisable(true);

        // Add listeners for real-time validation (except departure airport)
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

    private void loadAircraftData() {
        List<Aircraft> aircrafts = aircraftService.getAllAircrafts();
        aircraftComboBox.setItems(FXCollections.observableArrayList(aircrafts));
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
    public void setFlight(Flight flight) {
        this.flight = flight;
        this.originalAircraftId = flight.getAircraftId();

        flightNumberField.setText(flight.getFlightNumber());

        // Select Aircraft
        Aircraft aircraft = aircraftService.getAircraftById(flight.getAircraftId());
        if (aircraft != null) {
            aircraftComboBox.getSelectionModel().select(aircraft);
        }

        // Select Departure Airport.
        Airport departureAirport = airportService.getAirportById(flight.getDepartureAirportId());
        if (departureAirport != null) {
            departureAirportComboBox.getSelectionModel().select(departureAirport);
        }

        // Select Arrival Airport
        Airport arrivalAirport = airportService.getAirportById(flight.getArrivalAirportId());
        if (arrivalAirport != null) {
            arrivalAirportComboBox.getSelectionModel().select(arrivalAirport);
        }

        LocalDateTime departureDateTime = LocalDateTime.parse(flight.getDepartureTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        departureDatePicker.setValue(departureDateTime.toLocalDate());
        departureTimeComboBox.setValue(departureDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        LocalDateTime arrivalDateTime = LocalDateTime.parse(flight.getArrivalTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        arrivalDatePicker.setValue(arrivalDateTime.toLocalDate());
        arrivalTimeComboBox.setValue(arrivalDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        priceField.setText(String.valueOf(flight.getPrice()));

        // Check if the flight is in the past
        if (departureDateTime.isBefore(LocalDateTime.now())) {
            isPastFlight = true;
            disableFieldsForPastFlight();
        }

        // Call validation to set initial error states, if any.
        validateForm();
    }

    private void disableFieldsForPastFlight() {
        flightNumberField.setDisable(true);
        aircraftComboBox.setDisable(true);
        // Departure Airport is already disabled
        arrivalAirportComboBox.setDisable(true);
        departureDatePicker.setDisable(true);
        departureTimeComboBox.setDisable(true);
        arrivalDatePicker.setDisable(true);
        arrivalTimeComboBox.setDisable(true);
        priceField.setDisable(true);
        updateButton.setDisable(true); // Disable the update button

        // Optionally, show a message to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Past Flight");
        alert.setHeaderText(null);
        alert.setContentText("This flight is in the past and cannot be updated.");
        alert.showAndWait();
    }

    @FXML
    private void updateFlight() {
        //the same code
        // Check if it's a past flight *before* any validation
        if (isPastFlight) {
            // The fields should already be disabled, but this is an extra check.
            return;
        }
        if(validateForm()){
            try {

                flight.setFlightNumber(flightNumberField.getText());

                Aircraft selectedAircraft = aircraftComboBox.getValue();
                if(selectedAircraft == null){
                    setError(aircraftComboBox, "Please select an aircraft.");
                    return;
                }
                flight.setAircraftId(selectedAircraft.getAircraftId());



                Airport selectedArrivalAirport = arrivalAirportComboBox.getValue();
                if(selectedArrivalAirport == null){
                    setError(arrivalAirportComboBox, "Please select an Arrival Airport");
                    return;
                }
                flight.setArrivalAirportId(selectedArrivalAirport.getAirportId());

                LocalDate departureDate = departureDatePicker.getValue();
                String departureTime = departureTimeComboBox.getValue();
                if (departureDate == null || departureTime == null) {
                    setError(departureDatePicker, "Please select both a date and a time for departure");
                    return;
                }
                flight.setDepartureTime(departureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + departureTime);

                LocalDate arrivalDate = arrivalDatePicker.getValue();
                String arrivalTime = arrivalTimeComboBox.getValue();
                if(arrivalDate == null || arrivalTime == null){
                    setError(arrivalDatePicker, "Please select Arrival date and Time");
                    return;
                }
                flight.setArrivalTime(arrivalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + arrivalTime);

                flight.setPrice(Double.parseDouble(priceField.getText()));

                flightService.updateFlight(flight);

                if (selectedAircraft.getAircraftId() != originalAircraftId) {
                    aircraftService.resetAircraftFlightId(originalAircraftId);
                    aircraftService.updateAircraftFlightId(selectedAircraft.getAircraftId(), flight.getFlightId());
                }

                System.out.println("Flight Updated!");
                closeDialog();

            } catch (NumberFormatException e) {
                showAlert("Input Error", "Invalid input format. Please check your entries.");

            }
            catch (Exception e) {
                showAlert("Input Error", "An unexpected error occurred: " + e.getMessage());
            }
        }
    }


    private boolean validateForm() {
        //the same code
        boolean isValid = true;
        isValid &= validateFlightNumber();
        isValid &= validateAircraft();
        isValid &= validateArrivalAirport(); // Don't validate departure airport
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

        // Check for duplicate flight number, *excluding* the current flight
        if (isFlightNumberExistsForOtherFlights(flightNumber, flight.getFlightId())) {
            setError(flightNumberField,"Flight Number already exists for another flight.");
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
    private boolean validateArrivalAirport() {
        //the same code
        Airport selectedDepartureAirport = departureAirportComboBox.getValue();
        Airport selectedArrivalAirport = arrivalAirportComboBox.getValue();

        if (selectedArrivalAirport == null) {
            setError(arrivalAirportComboBox, "Please select an arrival airport.");
            return false;
        }
        //Check if departure airport is the same as arrival
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
            errorLabel.setVisible(true);
        }
    }

    private void clearError(Control control) {
        //the same code
        control.setStyle("");
        Label errorLabel = errorLabelMap.get(control);
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }
    }

    private boolean isFlightNumberExistsForOtherFlights(String flightNumber, int currentFlightId) {
        //the same code
        List<Flight> flights = flightService.getAllFlights();
        for (Flight f : flights) {
            if (f.getFlightNumber().equals(flightNumber) && f.getFlightId() != currentFlightId) {
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
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }
}