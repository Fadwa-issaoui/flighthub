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

    /* @FXML
        private ProgressBar splashScreenProgressBar;*/
    @FXML
    private StackPane stackPaneSplashScreen;
    @FXML
    private ImageView splashScreenImage;

    @FXML
    /*public void initialize() {
        // Load the splash screen image
        Image image = new Image(getClass().getResource("/images/FlightBlue.png").toExternalForm());
        splashScreenImage = new ImageView();
        splashScreenImage.setImage(image);*/

        // Simulate loading progress
        /*new Thread(() -> {
            try {
                for (double i = 0; i <= 1; i += 0.1) {
                    Thread.sleep(500); // Simulate loading delay
                    double progress = i;
                    Platform.runLater(() -> splashScreenProgressBar.setProgress(progress));
                }
                Platform.runLater(this::onLoadingComplete);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/
   // }

    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("stackPane: " + stackPaneSplashScreen); // Debugging
        new SplashScreenThread().start();
    }

    /*public void initialize(URL url, ResourceBundle rb) {


        new SplashScreenThread().start();
    }*/

    /*private void onLoadingComplete() {
        System.out.println("Loading complete. Transitioning to next scene...");
        // Add logic to transition to the next scene when the splash screen finishes loading
    }*/

    class SplashScreenThread extends Thread{

        @Override
        public void run() {
            try {

                Thread.sleep(5000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        Parent root = null;
                        try {
                            /*URL url = getClass().getResource("/FlightHub/SceneBuilder/Login.fxml");
                            if (url == null) {
                                System.out.println("FXML file not found!");
                            }*/ // i added this block cause i had a prblm with the stackpane, but it is resolved, i just added fx:id= "stackpane" in fxml
                            root = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Stage loginStage = new Stage();
                        loginStage.setScene(new Scene(root));
                        loginStage.show();
                        /*Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(scene);
                        stage.show();
                        stackPane.getScene().getWindow().hide();*/ // this works
                        /*Stage stage = (Stage) stackPane.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();*/// this method also works
                        Stage splashStage = (Stage) stackPaneSplashScreen.getScene().getWindow();
                        splashStage.close();

                    }
                });

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
