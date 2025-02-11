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
    public void start(Stage primaryStage) {
        try {
            // Load the Splash Screen
            Parent splashScreenRoot = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
            Scene splashScreenScene = new Scene(splashScreenRoot);
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
            e.printStackTrace();
        }
    }



    private void openDashboardScreen(Stage primaryStage) {
        try {
            // Load the Dashboard screen (Dashboard.fxml)
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/AdminDashboard.fxml"));
            Scene dashboardScene = new Scene(dashboardRoot);
            primaryStage.setScene(dashboardScene);
            primaryStage.setTitle("Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
