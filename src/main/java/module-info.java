module Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires static lombok;
    requires java.mail;
    requires java.desktop;
    requires activation;

    opens Main to javafx.fxml;
    exports Main;

    // Allow JavaFX to access controllers
    opens com.example.flighthub.controllers.login to javafx.fxml;
    opens com.example.flighthub.controllers.booking to javafx.fxml, javafx.graphics;

    exports com.example.flighthub.controllers.booking;
}
