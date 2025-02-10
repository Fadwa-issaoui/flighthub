package com.example.flighthub.controllers.car;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.flighthub.models.Car;
import com.example.flighthub.models.Location;
import com.example.flighthub.services.LocationService;

import java.time.LocalDate;

public class LocationController {

    @FXML private TableView<Location> locationTable;
    @FXML private TableColumn<Location, Integer> idCol;
    @FXML private TableColumn<Location, String> customerCol;
    @FXML private TableColumn<Location, String> carCol;  // Car column (showing the brand)
    @FXML private TableColumn<Location, Integer> daysCol;
    @FXML private TableColumn<Location, Double> totalCol;

    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField plateField;
    @FXML private TextField priceField;
    @FXML private TextField customerField;
    @FXML private TextField daysField;

    private final LocationService locationService = new LocationService();
    private final ObservableList<Location> locationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the table columns to show data
        idCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        customerCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCustomerName()));
        carCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCar().getBrand()));  // Display car brand
        daysCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRentalDays()));
        totalCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalCost()));

        locationTable.setItems(locationList);
        loadLocations();
    }

    private void loadLocations() {
        try {
            locationList.clear();
            locationList.addAll(locationService.getAllLocations());
        } catch (Exception e) {
            System.out.println("Error loading locations: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        try {
            String brand = brandField.getText();
            String model = modelField.getText();
            String plate = plateField.getText();
            double price = Double.parseDouble(priceField.getText());
            String customer = customerField.getText();
            int rentalDays = Integer.parseInt(daysField.getText());

            Car car = new Car(1, brand, model, plate, price);
            Location newLocation = new Location(1, car, customer, LocalDate.now(), rentalDays);

            locationService.addLocation(newLocation);
            locationList.add(newLocation);
            System.out.println("Location Added!");

            clearFields();
        } catch (Exception e) {
            System.out.println("Error adding location: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Location selected = locationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String newCustomerName = customerField.getText();
                int newRentalDays = Integer.parseInt(daysField.getText());

                locationService.updateLocation(selected.getId(), newCustomerName, newRentalDays);
                selected = new Location(selected.getId(), selected.getCar(), newCustomerName, selected.getRentalDate(), newRentalDays);

                locationList.set(locationList.indexOf(selected), selected);
                System.out.println("Location Updated!");

                clearFields();
            } catch (Exception e) {
                System.out.println("Error updating location: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDelete() {
        Location selected = locationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                locationService.removeLocation(selected.getId());
                locationList.remove(selected);
                System.out.println("Location Deleted!");
            } catch (Exception e) {
                System.out.println("Error deleting location: " + e.getMessage());
            }
        }
    }

    private void clearFields() {
        brandField.clear();
        modelField.clear();
        plateField.clear();
        priceField.clear();
        customerField.clear();
        daysField.clear();
    }
}
