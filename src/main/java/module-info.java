module Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;

    opens Main to javafx.fxml;
    opens SceneBuilder.Login to javafx.fxml; // Allow JavaFX to access controllers

    exports Main;
}
