package com.example.flighthub.controllers.booking;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class AgentDashboardController {

    @FXML
    private ChoiceBox<String> departureChoiceBox;

    @FXML
    private ChoiceBox<String> destinationChoiceBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<TableRowData> upcomingFlightsTable;
    @FXML
    private TableColumn<TableRowData, String> flightNumberCol;
    @FXML
    private TableColumn<TableRowData, String> departureCol;
    @FXML
    private TableColumn<TableRowData, String> arrivalCol;

    @FXML
    private TableView<TableRowData> recentBookingsTable;
    @FXML
    private TableColumn<TableRowData, String> passengerNameCol;
    @FXML
    private TableColumn<TableRowData, String> bookingDateCol;
    @FXML
    private TableColumn<TableRowData, String> flightInfoCol;

    @FXML
    private Label totalBookingsLabel;

    @FXML
    public void initialize() {
        loadLocations();
        initializeTables();
        loadUpcomingFlights();
        loadRecentBookings();
        loadTotalBookings();
    }

    private void loadLocations() {
        ObservableList<String> locations = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT location FROM airport");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("location"));
            }

            departureChoiceBox.setItems(locations);
            destinationChoiceBox.setItems(locations);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load locations from the database.");
        }
    }

    private void initializeTables() {
        // Upcoming flights table setup
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("column1"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("column2"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("column3"));

        // Recent bookings table setup
        passengerNameCol.setCellValueFactory(new PropertyValueFactory<>("column1"));
        bookingDateCol.setCellValueFactory(new PropertyValueFactory<>("column2"));
        flightInfoCol.setCellValueFactory(new PropertyValueFactory<>("column3"));
    }

    private void loadUpcomingFlights() {
        ObservableList<TableRowData> flightRows = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT f.flightNumber, a1.location AS departure, a2.location AS arrival " +
                    "FROM flight f " +
                    "JOIN airport a1 ON f.departureAirportId = a1.airportId " +
                    "JOIN airport a2 ON f.arrivalAirportId = a2.airportId " +
                    "WHERE f.departureTime >= NOW()";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flightRows.add(new TableRowData(
                        rs.getString("flightNumber"),
                        rs.getString("departure"),
                        rs.getString("arrival")
                ));
            }
            upcomingFlightsTable.setItems(flightRows);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load upcoming flights.");
        }
    }

    private void loadRecentBookings() {
        ObservableList<TableRowData> bookingRows = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT b.bookingDate, p.firstName, p.lastName, f.flightNumber " +
                    "FROM booking b " +
                    "JOIN passenger p ON b.passengerId = p.passengerId " +
                    "JOIN flight f ON b.flightId = f.flightId " +
                    "ORDER BY b.bookingDate DESC LIMIT 5";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("firstName") + " " + rs.getString("lastName");
                bookingRows.add(new TableRowData(
                        fullName,
                        rs.getString("bookingDate"),
                        rs.getString("flightNumber")
                ));
            }
            recentBookingsTable.setItems(bookingRows);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load recent bookings.");
        }
    }

    private void loadTotalBookings() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT COUNT(*) FROM booking";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalBookingsLabel.setText(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load total bookings.");
        }
    }

    @FXML
    private void handleSearchButtonAction() {
        String departureLocation = departureChoiceBox.getValue();
        String destinationLocation = destinationChoiceBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (departureLocation == null || destinationLocation == null || selectedDate == null) {
            showAlert("Input Error", "Please select all fields before searching!");
            return;
        }
        System.out.println("Searching for flights...");
        System.out.println("Departure: " + departureLocation);
        System.out.println("Destination: " + destinationLocation);
        System.out.println("Date: " + selectedDate);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_search.fxml"));
            Parent root = loader.load();

            // Retrieve the already instantiated controller
            FlightSearchController searchController = loader.getController();
            if (searchController != null) {
                System.out.println("Controller loaded successfully!");
                searchController.setFlightData(departureLocation, destinationLocation, selectedDate);
            } else {
                System.out.println("Error: FlightSearchController is NULL!");
            }

            // Switch scene
            Stage stage = (Stage) searchButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Loading Error", "Could not load the search page!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Custom data holder for rows in the tables
    public static class TableRowData {
        private String column1;
        private String column2;
        private String column3;

        public TableRowData(String column1, String column2, String column3) {
            this.column1 = column1;
            this.column2 = column2;
            this.column3 = column3;
        }

        public String getColumn1() {
            return column1;
        }

        public String getColumn2() {
            return column2;
        }

        public String getColumn3() {
            return column3;
        }
    }
}
