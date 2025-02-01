module Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires static lombok;

    opens Main to javafx.fxml;
    // Allow JavaFX to access controllers

    exports Main;
    opens com.example.flighthub.controllers.aircraft to javafx.fxml;
    opens com.example.flighthub to javafx.fxml;

}
