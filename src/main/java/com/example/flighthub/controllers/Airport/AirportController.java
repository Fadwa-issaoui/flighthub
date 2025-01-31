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

import java.util.List;

import static com.example.flighthub.services.AirportService.connection;

public class AirportController {

    public ImageView iconAirport;
    public ImageView imageViewAirport;
    public Pane paneAirport;
    public Separator separatorAirport;
    @FXML
    private TextField textfieldAirportId;

    @FXML
    private TextField textfieldLocalisation;

    @FXML
    private TextField textfieldSearchAirport;

    @FXML
    private Button buttonAddAirport;

    @FXML
    private Button buttonSearchAirport;

    @FXML
    private TableView<Airport> tableViewAirport;

    @FXML
    private TableColumn<Airport, Integer> tableColumnAirportId;

    @FXML
    private TableColumn<Airport, String> tableColumnLocalisation;

    private AirportService airportService = new AirportService();

    @FXML
    private void initialize() {
        tableColumnAirportId.setCellValueFactory(new PropertyValueFactory<>("airportId"));
        tableColumnLocalisation.setCellValueFactory(new PropertyValueFactory<>("location"));

        buttonAddAirport.setOnAction(event -> addAirport());
        buttonSearchAirport.setOnAction(event -> searchAirport());

        loadAirportData();
    }

    private void addAirport() {
        try {
            int airportId = Integer.parseInt(textfieldAirportId.getText());
            String location = textfieldLocalisation.getText();

            Airport airport = new Airport();
            airport.setAirportId(airportId);
            airport.setLocation(location);

            airportService.addAirport(airport);
            loadAirportData();
        } catch (NumberFormatException e) {
            System.out.println("Invalid Airport ID");
        }
    }

    private void searchAirport() {
        try {
            String searchText = textfieldSearchAirport.getText().trim();
            ObservableList<Airport> airportList = FXCollections.observableArrayList();

            if (!searchText.isEmpty()) {
                airportList.addAll(airportService.getAirportsByPartialId(searchText));
            }

            tableViewAirport.setItems(airportList);
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }


    private void loadAirportData() {
        List<Airport> airports = List.of(); // Fetch all airports if needed
        ObservableList<Airport> airportList = FXCollections.observableArrayList(airports);
        tableViewAirport.setItems(airportList);
    }
    

}
