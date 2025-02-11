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
                        Parent root = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
                        javafx.application.Platform.runLater(() -> openLoginScreen(new Stage()));
                        new Thread(() -> {
                            try {
                                Thread.sleep(5000);  // Wait for 5 seconds
                                javafx.application.Platform.runLater(() -> openLoginScreen(new Stage()));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    private void openLoginScreen(Stage primaryStage) {
        try {
            // Load the Login screen (Login.fxml)
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
}
