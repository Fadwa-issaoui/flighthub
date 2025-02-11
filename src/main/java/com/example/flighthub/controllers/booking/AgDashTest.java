package com.example.flighthub.controllers.booking;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AgDashTest extends Application {

    @FXML
    private ChoiceBox<String> departureChoiceBox;

    @FXML
    private ChoiceBox<String> destinationChoiceBox;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file and set the correct controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_fl_dash.fxml"));
        Parent root = loader.load();

        // No need to call loadLocations here, it's already handled in the controller

        primaryStage.setTitle("Agent Dashboard Test");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Method to fetch locations from the database and populate ChoiceBoxes
 /*   public void loadLocations() {
        ObservableList<String> locations = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT location FROM airport");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
            departureChoiceBox.getItems().clear();
            destinationChoiceBox.getItems().clear();
            // Set values in ChoiceBoxes
            departureChoiceBox.setItems(locations);
            destinationChoiceBox.setItems(locations);

            System.out.println("Locations Loaded: " + locations);  // Debugging

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching locations from database!");
        }
    }*/
}
