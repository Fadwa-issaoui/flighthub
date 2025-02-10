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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FlightController {

    private final FlightService flightService = new FlightService();

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
    private TableColumn<Flight, Void> functionsColumn;
    @FXML
    private Button addButton;

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        detailsColumn.setCellFactory(param -> new DetailsButtonTableCell<>());
        functionsColumn.setCellFactory(param -> new FunctionsButtonTableCell());

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


    private class FunctionsButtonTableCell extends TableCell<Flight, Void> {
        private final Button deleteButton = new Button("Delete");
        private final Button updateButton = new Button("Update");


        public FunctionsButtonTableCell() {

            deleteButton.setOnAction(event -> {
                Flight flight = getTableRow().getItem();
                if (flight != null) {
                    handleDeleteAction(flight);
                }
            });
            updateButton.setOnAction(event -> {
                Flight flight = getTableRow().getItem();
                if (flight != null) {
                    handleUpdateAction(flight);
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox container = new HBox(8); // space of 8 between buttons
                container.getChildren().addAll(deleteButton, updateButton);
                setGraphic(container);
            }
        }
    }

    private void handleDeleteAction(Flight flight) {
        //Delete the item
        flightService.deleteFlight(flight.getFlightId());
        //refresh table
        loadFlights();
        System.out.println("Delete button for Flight ID:" + flight.getFlightId());
    }

    private void handleUpdateAction(Flight flight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/UpdateFlight.fxml"));
            Parent root = loader.load();

            UpdateFlightController controller = loader.getController();
            controller.setFlight(flight);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Update Flight");
            stage.showAndWait();
            loadFlights(); //refresh the list after update
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAddButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/CreateFlight.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Create New Flight");
            stage.showAndWait();
            loadFlights();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}