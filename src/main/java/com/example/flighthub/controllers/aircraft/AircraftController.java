package com.example.flighthub.controllers.aircraft;

import com.example.flighthub.models.Aircraft;
import com.example.flighthub.services.AircraftService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AircraftController {

    @FXML
    private TextField aircraftIdField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField capacityField;
    @FXML
    private CheckBox isAvailableCheckBox;
    @FXML
    private TableView<Aircraft> aircraftTableView;
    @FXML
    private TableColumn<Aircraft, Integer> colAircraftId;
    @FXML
    private TableColumn<Aircraft, String> colModel;
    @FXML
    private TableColumn<Aircraft, Integer> colCapacity;
    @FXML
    private TableColumn<Aircraft, Boolean> colAvailable;

    private AircraftService aircraftService = new AircraftService();
    private ObservableList<Aircraft> aircraftList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        colAircraftId.setCellValueFactory(new PropertyValueFactory<>("aircraftId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        // Set the data in TableView
        aircraftTableView.setItems(aircraftList);

        // Load data from database
        loadAircraftData();
    }

    private void loadAircraftData() {
        aircraftList.setAll(aircraftService.getAllAircrafts()); // Load from DB
    }

    @FXML
    public void handleAddAircraft() {
        try {
            int id = Integer.parseInt(aircraftIdField.getText());
            String model = modelField.getText().trim();
            int capacity = Integer.parseInt(capacityField.getText());
            boolean isAvailable = isAvailableCheckBox.isSelected();

            if (model.isEmpty()) {
                showAlert("Input Error", "Model field cannot be empty.");
                return;
            }

            Aircraft aircraft = new Aircraft(id, model, capacity, isAvailable);
            if (aircraftService.createAircraft(aircraft)) {
                aircraftList.add(aircraft);  // Add to TableView
                aircraftTableView.refresh(); // Refresh UI
                clearFields();
            } else {
                showAlert("Error", "Failed to add aircraft.");
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for ID and Capacity.");
        }
    }

    @FXML
    public void handleUpdateAircraft() {
        Aircraft selected = aircraftTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String model = modelField.getText().trim();

            if (model.isEmpty()) {
                showAlert("Input Error", "Model field cannot be empty.");
                return;
            }

            try {
                int capacity = Integer.parseInt(capacityField.getText());
                boolean isAvailable = isAvailableCheckBox.isSelected();

                Aircraft updatedAircraft = new Aircraft(selected.getAircraftId(), model, capacity, isAvailable);

                if (aircraftService.updateAircraft(updatedAircraft)) {
                    loadAircraftData();  // Reload the TableView from DB to reflect changes
                    clearFields();
                } else {
                    showAlert("Error", "Failed to update aircraft.");
                }
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Please enter a valid number for capacity.");
            }
        } else {
            showAlert("Selection Error", "Please select an aircraft to update.");
        }
    }

    @FXML
    public void handleDeleteAircraft() {
        Aircraft selected = aircraftTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (aircraftService.deleteAircraft(selected.getAircraftId())) {
                aircraftList.remove(selected);  // Remove from TableView
            } else {
                showAlert("Error", "Failed to delete aircraft.");
            }
        } else {
            showAlert("Selection Error", "Please select an aircraft to delete.");
        }
    }

    private void clearFields() {
        aircraftIdField.clear();
        modelField.clear();
        capacityField.clear();
        isAvailableCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
