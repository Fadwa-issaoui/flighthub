module tn.esprit.spring.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens tn.esprit.spring.demo to javafx.fxml;
    exports tn.esprit.spring.demo;
}