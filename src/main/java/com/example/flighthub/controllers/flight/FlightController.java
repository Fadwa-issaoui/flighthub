package com.example.flighthub.controllers.flight;

import com.example.flighthub.models.Flight;
import com.example.flighthub.services.FlightService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class FlightController {

    private final FlightService flightService = new FlightService();

    @FXML private TableView<Flight> flightTable;
    @FXML private TableColumn<Flight, Integer> idColumn;
    @FXML private TableColumn<Flight, String> flightNumberColumn;
    @FXML private TableColumn<Flight, String> departureTimeColumn;
    @FXML private TableColumn<Flight, String> arrivalTimeColumn;
    @FXML private TableColumn<Flight, Double> priceColumn;
    @FXML private TableColumn<Flight, Void> detailsColumn;
    @FXML private TableColumn<Flight, Void> functionsColumn;
    @FXML private Button addButton;
    @FXML private TextField searchFlightNumberField;
    @FXML private Button clearSearchButton;

    private ObservableList<Flight> observableFlights = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        detailsColumn.setCellFactory(param -> new DetailsButtonTableCell<>()); // Use the updated cell factory
        functionsColumn.setCellFactory(param -> new FunctionsButtonTableCell());

        flightTable.setRowFactory(new FlightRowFactory());

        loadFlights();

        addButton.setOnAction(event -> handleAddButton());

        searchFlightNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterFlights(newValue);
        });

        clearSearchButton.setOnAction(event -> {
            searchFlightNumberField.clear();
        });
    }

    private void loadFlights() {
        List<Flight> flightList = flightService.getAllFlights();
        observableFlights = FXCollections.observableArrayList(flightList);
        flightTable.setItems(observableFlights);
    }

    private void filterFlights(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            flightTable.setItems(observableFlights);
        } else {
            List<Flight> filteredList = observableFlights.stream()
                    .filter(flight -> flight.getFlightNumber().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());

            flightTable.setItems(FXCollections.observableArrayList(filteredList));
        }
    }

    private class FlightRowFactory implements Callback<TableView<Flight>, TableRow<Flight>> {
        @Override
        public TableRow<Flight> call(TableView<Flight> tableView) {
            return new TableRow<Flight>() {
                @Override
                protected void updateItem(Flight flight, boolean empty) {
                    super.updateItem(flight, empty);

                    if (flight == null || empty) {
                        setStyle("");
                    } else {
                        try {
                            LocalDateTime arrivalDateTime = LocalDateTime.parse(flight.getArrivalTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                            if (arrivalDateTime.isBefore(LocalDateTime.now())) {
                                setStyle("-fx-background-color: #b3ffb3;");
                            } else {
                                setStyle("-fx-background-color: #ffddb3;");
                            }
                        } catch (DateTimeParseException e) {
                            setStyle("");
                            System.err.println("Error parsing arrival time: " + e.getMessage());
                        }
                    }
                }
            };
        }
    }


    // Inner class for the custom cell factory for Details button
    private class DetailsButtonTableCell<S, T> extends TableCell<S, T> {
        private final Button detailsButton = new Button("Details");

        public DetailsButtonTableCell() {
            detailsButton.setOnAction(event -> {
                S item = getTableView().getItems().get(getIndex()); // Get the item directly
                if (item instanceof Flight) {
                    Flight flight = (Flight) item;
                    showFlightDetailsPopup(flight);
                }
            });
            // Style for details button
            detailsButton.setStyle("-fx-background-color: #5067e9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 3;");

            // Style when the button is hovered (optional)
            detailsButton.setOnMouseEntered(event -> detailsButton.setStyle("-fx-background-color: #3a4ea0; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 3;"));
            detailsButton.setOnMouseExited(event -> detailsButton.setStyle("-fx-background-color: #5067e9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 3;"));
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(detailsButton);
            }
        }
    }


    private void showFlightDetailsPopup(Flight flight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/FlightDetailsPopup.fxml"));
            Parent root = loader.load();

            FlightDetailsPopupController controller = loader.getController();
            controller.setFlightData(flight); // Pass the flight data

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED); // Remove window decorations
            stage.setScene(new Scene(root));
            stage.setTitle("Flight Details"); // Set a title (optional)

            // Add a close button to your popup's FXML and set its action
            // OR
            // Make the popup close when clicking outside of it:
            stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    stage.close();
                }
            });


            stage.showAndWait(); // Show the popup and wait for it to close
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    private class FunctionsButtonTableCell extends TableCell<Flight, Void> {
        //the same code
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
        flightService.deleteFlight(flight.getFlightId());
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
            loadFlights();
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