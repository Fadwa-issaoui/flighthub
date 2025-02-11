package com.example.flighthub.controllers.booking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class FlightSearchTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_search.fxml"));
        System.out.println(getClass().getResource("/FlightHub/SceneBuilder/booking_search.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("Flight Search Test");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
