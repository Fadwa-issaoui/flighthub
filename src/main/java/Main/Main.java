//package Main;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/car.fxml"));
//        primaryStage.setScene(new Scene(fxmlLoader.load()));
//        primaryStage.setTitle("Car Management");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//
//    }
//
//
//}


package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override

    public void start(Stage primaryStage) throws IOException {
        try {
            // Load the Splash Screen
            Object splashScreenRoot = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
            //Object splashScreenRoot = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/GestDash.fxml"));
            Scene splashScreenScene = new Scene((Parent) splashScreenRoot);
            primaryStage.setScene(splashScreenScene);
            primaryStage.setTitle("Splash Screen");

            // Make the window borderless and completely transparent (no buttons, no border)
            primaryStage.initStyle(StageStyle.TRANSPARENT); // Remove title bar and window decorations
            primaryStage.setOpacity(1);  // Make sure opacity is fully visible

            // Override the close request behavior (disables close button functionality)
            primaryStage.setOnCloseRequest(event -> event.consume());  // Disables the close button

            // Disable resizing to prevent dragging to resize
            primaryStage.setResizable(false);

            // Show the splash screen
            primaryStage.show();

            // After 5 seconds, switch to the Login screen
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}