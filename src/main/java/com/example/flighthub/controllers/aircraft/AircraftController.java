package com.example.flighthub.controllers.aircraft;
import com.example.flighthub.models.Aircraft;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

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

    // Method for adding an aircraft
    @FXML
    public void handleAddAircraft() {
        System.out.println("Adding aircraft");
    }

    // Method for updating an aircraft
    @FXML
    public void handleUpdateAircraft() {
        System.out.println("Updating aircraft");
    }

    // Method for deleting an aircraft
    @FXML
    public void handleDeleteAircraft() {
        System.out.println("Deleting aircraft");
    }
}