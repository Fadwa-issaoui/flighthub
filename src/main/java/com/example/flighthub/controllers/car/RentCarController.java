package com.example.flighthub.controllers.car;

import com.example.flighthub.models.Car;
import com.example.flighthub.models.Location;
import com.example.flighthub.services.CarRentalService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class RentCarController {
    @FXML private TextField customerNameField;
    @FXML private DatePicker rentalDatePicker;
    @FXML private TextField rentalDaysField;
    @FXML private ComboBox<Car> carComboBox;
    @FXML private Label statusLabel;

    private final CarRentalService carRentalService;

    public RentCarController() {
        this.carRentalService = new CarRentalService(); // Initialize CarRentalService
    }

    @FXML
    public void initialize() {
        // Fetch available cars using CarRentalService
        List<Car> availableCars = carRentalService.getAvailableCars();

        // Populate the ComboBox with available cars
        carComboBox.getItems().addAll(availableCars);

        // Optional: Customize how cars are displayed in the ComboBox
        carComboBox.setCellFactory(param -> new ListCell<Car>() {
            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);
                if (empty || car == null) {
                    setText(null);
                } else {
                    setText(car.getBrand() + " " + car.getModel() + " (" + car.getLicensePlate() + ")");
                }
            }
        });

        // Optional: Customize how the selected car is displayed in the ComboBox button
        carComboBox.setButtonCell(new ListCell<Car>() {
            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);
                if (empty || car == null) {
                    setText(null);
                } else {
                    setText(car.getBrand() + " " + car.getModel() + " (" + car.getLicensePlate() + ")");
                }
            }
        });
    }

    @FXML
    public void handleRentCar() {
        String customerName = customerNameField.getText();
        LocalDate rentalDate = rentalDatePicker.getValue();
        int rentalDays = Integer.parseInt(rentalDaysField.getText());
        Car selectedCar = carComboBox.getSelectionModel().getSelectedItem();

        if (selectedCar != null && rentalDate != null && !customerName.isEmpty()) {
            // Create a new Location object
            Location location = new Location(0, selectedCar, customerName, rentalDate, rentalDays);

            // Rent the car using CarRentalService
            carRentalService.rentCar(location);

            // Update the UI
            statusLabel.setText("Car rented successfully!");

            // Refresh the ComboBox to reflect the updated availability
            carComboBox.getItems().clear();
            carComboBox.getItems().addAll(carRentalService.getAvailableCars());
        } else {
            statusLabel.setText("Please fill all fields and select a car.");
        }
    }
}