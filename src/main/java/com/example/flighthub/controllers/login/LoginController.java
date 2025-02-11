package com.example.flighthub.controllers.login;

import com.example.flighthub.services.LoginService;
import com.example.flighthub.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField textfieldSignInUser;

    @FXML
    private PasswordField passwordfieldSignIn;

    @FXML
    private Button buttonSignIn;

    private final LoginService loginService = new LoginService();
    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        buttonSignIn.setOnAction(event -> signIn());
    }

    @FXML
    private void signIn() {
        String username = textfieldSignInUser.getText();
        String password = passwordfieldSignIn.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in both fields.", Alert.AlertType.ERROR);
            return;
        }

        try {
            boolean loginSuccess = loginService.login(username, password);

            if (loginSuccess) {
                showAlert("Success", "Login successful!", Alert.AlertType.INFORMATION);
                switch (userService.getUserRole(username)){
                    case "ADMIN": openAirportDashboard(); break;
                    case "GESTIONNAIRE": openGestDashboard(); break;
                    case "AGENT": openAgentDash(); break;
                }

            } else {
                showAlert("Login Failed", "Invalid username or password.", Alert.AlertType.ERROR);
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openAirportDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/AdminDashboard.fxml"));
        Parent root = loader.load();
        Stage dashboardStage = new Stage();
        dashboardStage.setScene(new Scene(root));
        dashboardStage.setTitle("Admin Dashboard");
        dashboardStage.show();

        // Fermer la fenêtre de connexion
        Stage loginStage = (Stage) textfieldSignInUser.getScene().getWindow();
        loginStage.close();
    }

    private void openGestDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/GestDash.fxml"));
        Parent root = loader.load();
        Stage dashboardStage = new Stage();
        dashboardStage.setScene(new Scene(root));
        dashboardStage.setTitle("Admin Dashboard");
        dashboardStage.show();

        // Fermer la fenêtre de connexion
        Stage loginStage = (Stage) textfieldSignInUser.getScene().getWindow();
        loginStage.close();
    }


    private void openAgentDash() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_fl_dash.fxml"));
        Parent root = loader.load();
        Stage dashboardStage = new Stage();
        dashboardStage.setScene(new Scene(root));
        dashboardStage.setTitle("Admin Dashboard");
        dashboardStage.show();

        Stage loginStage = (Stage) textfieldSignInUser.getScene().getWindow();
        loginStage.close();

    }


}
