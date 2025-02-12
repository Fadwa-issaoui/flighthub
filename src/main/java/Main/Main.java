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

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/rent_car.fxml"));
        primaryStage.setTitle("Car Rental System");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}