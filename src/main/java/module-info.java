module Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires static lombok;


    opens Main to javafx.fxml;
    // Allow JavaFX to access controllers
    opens com.example.flighthub.controllers.login to javafx.fxml;
    opens com.example.flighthub.controllers.flight to javafx.fxml;
    opens com.example.flighthub.models to javafx.base;

    exports Main;
    exports com.example.flighthub.controllers.flight to javafx.fxml;
}