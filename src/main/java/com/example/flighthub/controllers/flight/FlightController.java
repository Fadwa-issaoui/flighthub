package com.example.flighthub.controllers.flight;

import com.example.flighthub.models.Flight;
import com.example.flighthub.services.FlightService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FlightController {

    private final FlightService flightService = new FlightService();

    /* @FXML
     private HBox header;*/
    @FXML
    private TableView<Flight> flightTable;
    @FXML
    private TableColumn<Flight, Integer> idColumn;
    @FXML
    private TableColumn<Flight, String> flightNumberColumn;
    @FXML
    private TableColumn<Flight, String> departureTimeColumn;
    @FXML
    private TableColumn<Flight, String> arrivalTimeColumn;
    @FXML
    private TableColumn<Flight, Double> priceColumn;
    @FXML
    private TableColumn<Flight, Void> detailsColumn;
    @FXML
    private Button addButton;
  /*  @FXML
    private Text flightHubText;*/



    @FXML
    public void initialize() {
      /*  header.setStyle("-fx-background-color: #5067e9; -fx-padding: 15;");
        flightHubText.setFill(javafx.scene.paint.Color.WHITE);*/
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        detailsColumn.setCellFactory(param -> new DetailsButtonTableCell<>());

        // Load initial data
        loadFlights();

        //set action for add button
        addButton.setOnAction(event -> handleAddButton());
    }

    // Method to load flight data into the table
    private void loadFlights() {
        List<Flight> flight = flightService.getAllFlights();
        ObservableList<Flight> observableFlights = FXCollections.observableArrayList(flight);
        flightTable.setItems(observableFlights);
    }

    // Method to handle the detail button action
    private void handleDetailAction(Flight flight) {
        //TODO: will be a future feature
        System.out.println("Details button for Flight ID:" + flight.getFlightId());
    }

    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/CreateFlight.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows
            stage.setScene(new Scene(root));
            stage.setTitle("Create New Flight"); // Optional title
            stage.showAndWait(); // Show and wait for the dialog to close
            loadFlights(); //refresh the flight list

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}