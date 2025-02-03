package com.example.flighthub.controllers.Airport;

import com.example.flighthub.models.Airport;
import com.example.flighthub.services.AirportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AirportController {

    @FXML
    public Pane paneAirport;
    @FXML
    public ImageView imageViewAirport;
    @FXML
    public ImageView iconAirport;
    @FXML
    public Separator separatorAirport;
    @FXML
    private TextField textfieldAirportId;
    @FXML
    private TextField textfieldName;
    @FXML
    private TextField textfieldLocalisation;
    @FXML
    private TextField textfieldCode;
    @FXML
    private TextField textfieldSearchAirport;
    @FXML
    private Button buttonAddAirport;
    @FXML
    private Button buttonSearchAirport;
    @FXML
    private Button buttonModify;
    @FXML
    private Button buttonDelete;
    @FXML
    private TableView<Airport> tableViewAirport;
    @FXML
    private TableColumn<Airport, Integer> tableColumnAirportId;
    @FXML
    private TableColumn<Airport, String> tableColumnName;
    @FXML
    private TableColumn<Airport, String> tableColumnLocalisation;
    @FXML
    private TableColumn<Airport, String> tableColumnCode;

    private final AirportService airportService = new AirportService();

    @FXML
    private void initialize() {
        // Setting up table columns
        tableColumnAirportId.setCellValueFactory(new PropertyValueFactory<>("airportId"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnLocalisation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tableColumnCode.setCellValueFactory(new PropertyValueFactory<>("code"));

        // Setting button actions
        buttonAddAirport.setOnAction(event -> addAirport());
        buttonSearchAirport.setOnAction(event -> searchAirport());
        buttonModify.setOnAction(event -> modifyAirport());
        buttonDelete.setOnAction(event -> deleteAirport());

        // Load all airports into table initially
        loadAirportData();
    }

    // Add a new airport
    private void addAirport() {
        try {
            int airportId = Integer.parseInt(textfieldAirportId.getText());
            String name = textfieldName.getText();
            String location = textfieldLocalisation.getText();
            String code = textfieldCode.getText();

            airportService.createAirport(new Airport(airportId, name, location, code));
            loadAirportData();
            clearFields();
        } catch (NumberFormatException e) {
            System.out.println("Invalid Airport ID");
        }
    }

    // Search airports based on input
    private void searchAirport() {
        String searchText = textfieldSearchAirport.getText().trim();
        ObservableList<Airport> airportList = FXCollections.observableArrayList(airportService.getAirportsByPartialSearch(searchText));
        tableViewAirport.setItems(airportList);
    }

    // Load data into the table from service
    private void loadAirportData() {
        ObservableList<Airport> airportList = FXCollections.observableArrayList(airportService.getAllAirports());
        tableViewAirport.setItems(airportList);
    }

    // Clear the input fields
    private void clearFields() {
        textfieldAirportId.clear();
        textfieldName.clear();
        textfieldLocalisation.clear();
        textfieldCode.clear();
    }

    // Modify an existing airport (example logic, adjust as needed)
    // Modify an existing airport
    private void modifyAirport() {
        Airport selectedAirport = tableViewAirport.getSelectionModel().getSelectedItem();
        if (selectedAirport != null) {
            // Populate fields with the selected airport's details
            textfieldAirportId.setText(String.valueOf(selectedAirport.getAirportId()));
            textfieldName.setText(selectedAirport.getName());
            textfieldLocalisation.setText(selectedAirport.getLocation());
            textfieldCode.setText(selectedAirport.getCode());

            // Add an event listener to the Modify button for saving changes
            buttonModify.setOnAction(event -> {
                try {
                    int airportId = Integer.parseInt(textfieldAirportId.getText());
                    String name = textfieldName.getText();
                    String location = textfieldLocalisation.getText();
                    String code = textfieldCode.getText();

                    Airport updatedAirport = new Airport(airportId, name, location, code);
                    airportService.updateAirport(updatedAirport);
                    loadAirportData(); // Refresh the table
                    clearFields(); // Clear input fields
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Airport ID");
                }
            });
        }
    }


    // Delete the selected airport
    private void deleteAirport() {
        Airport selectedAirport = tableViewAirport.getSelectionModel().getSelectedItem();
        if (selectedAirport != null) {
            airportService.deleteAirport(selectedAirport.getAirportId());
            loadAirportData();
        }
    }
}
