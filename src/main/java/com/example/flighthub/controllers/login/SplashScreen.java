package com.example.flighthub.controllers.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreen implements Initializable {

    @FXML
    private StackPane stackPaneSplashScreen;

    @FXML
    private ImageView splashScreenImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SplashScreenThread().start();
    }

    class SplashScreenThread extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(300); // Splash screen stays for 5 seconds
                Platform.runLater(() -> {
                    try {
                        //Parent root = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
                        Parent root = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Flights.fxml"));
                        Stage loginStage = new Stage();
                        loginStage.setScene(new Scene(root));
                        loginStage.show();

                        // Close the splash screen
                        Stage splashStage = (Stage) stackPaneSplashScreen.getScene().getWindow();
                        splashStage.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }
    }
}
