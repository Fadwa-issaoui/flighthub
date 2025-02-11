module Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.net.http;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    exports com.example.flighthub.controllers.dashboard to javafx.fxml;
    exports com.example.flighthub.controllers.login to javafx.fxml;

    exports com.example.flighthub.controllers.Airport to javafx.fxml; // Export Airport package to javafx.fxml
 opens Main to javafx.fxml;
    exports Main;


     exports com.example.flighthub.controllers.booking;
    opens com.example.flighthub.models to javafx.base;
   opens com.example.flighthub.controllers.booking to javafx.fxml, javafx.graphics;
    opens com.example.flighthub.controllers.car to javafx.fxml;
    opens com.example.flighthub.controllers.user to javafx.fxml;
    opens com.example.flighthub.controllers.flight to javafx.fxml;
    opens com.example.flighthub.controllers.aircraft to javafx.fxml;
    opens com.example.flighthub.controllers.dashboard to javafx.fxml;
    opens com.example.flighthub.controllers.login to javafx.fxml;
    opens com.example.flighthub.controllers.Airport to javafx.fxml;
    exports com.example.flighthub.models.weatherstack to com.fasterxml.jackson.databind;

}
