package com.example.flighthub.controllers.dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import com.example.flighthub.services.AdminDashboardService;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    public Button buttonUserAdmin;
    @FXML
    public Button buttonAirportAdmin;
    @FXML
    public Button buttonAircraftAdmin;
    @FXML
    public ImageView imgDashAdmin;
    @FXML
    private AnchorPane anchorPaneDashAdmin;
    @FXML
    private StackPane stackPaneDashAdmin;  // This is the container for the central content

    private AdminDashboardService dashboardService;

    @FXML
    private Button buttonLogout;

    public AdminDashboardController() {
        dashboardService = new AdminDashboardService(); // Initialize service
    }

    // Handle the click event for opening the User Interface
    @FXML
    private void openUserInterface(ActionEvent event) {
        loadContent("/FlightHub/SceneBuilder/User.fxml");
        dashboardService.loadUserData();  // Load user data when the user interface is opened
    }

    // Handle the click event for opening the Airport Interface
    @FXML
    private void openAirportInterface(ActionEvent event) {
        loadContent("/FlightHub/SceneBuilder/Airport.fxml");
        dashboardService.loadAirportData();  // Load airport data when the airport interface is opened
    }

    // Handle the click event for opening the Aircraft Interface
    @FXML
    private void openAircraftInterface(ActionEvent event) {
        loadContent("/FlightHub/SceneBuilder/Aircraft.fxml");
        dashboardService.loadAircraftData();  // Load aircraft data when the aircraft interface is opened
    }

    // Method to load the content into the central area of the dashboard
    private void loadContent(String fxmlPath) {
        try {
            Parent newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
            stackPaneDashAdmin.getChildren().clear();  // Clear the current content
            stackPaneDashAdmin.getChildren().add(newContent);  // Add the new content
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Get the current stage (dashboard window) and close it
            Stage stage = (Stage) buttonLogout.getScene().getWindow();
            stage.close();

            // Load the login window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml")); // Adjust path if needed
            Parent root = loader.load();

            // Create new stage for the login window
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Initialize the controller and load initial data
    @FXML
    public void initialize() {
        dashboardService.loadAirportData();
        dashboardService.loadUserData();
        dashboardService.loadAircraftData();
    }
}
