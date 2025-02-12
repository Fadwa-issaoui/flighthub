//package com.example.flighthub.controllers.car;
//
//import com.example.flighthub.models.Car;
//import javafx.fxml.FXML;
//import javafx.scene.control.TextField;
//import java.math.BigDecimal;
//
//public class EditCarDialogController {
//    @FXML private TextField brandField;
//    @FXML private TextField modelField;
//    @FXML private TextField licensePlateField;
//    @FXML private TextField priceField;
//
//    private Car car;
//
//    public void setCar(Car car) {
//        this.car = car;
//        brandField.setText(car.getBrand());
//        modelField.setText(car.getModel());
//        licensePlateField.setText(car.getLicensePlate());
//        priceField.setText(car.getRentalPricePerDay().toString());
//    }
//
//    @FXML
//    private void handleSave() {
//        car.setBrand(brandField.getText());
//        car.setModel(modelField.getText());
//        car.setLicensePlate(licensePlateField.getText());
//        car.setRentalPricePerDay(new BigDecimal(priceField.getText().trim().replace(",", ".")));
//
//        // Close the dialog
//        brandField.getScene().getWindow().hide();
//    }
//
//    public Car getUpdatedCar() {
//        return car;
//    }
//}


package com.example.flighthub.controllers.car;

import com.example.flighthub.models.Car;
import com.example.flighthub.services.CarService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.math.BigDecimal;

public class EditCarDialogController {
    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField licensePlateField;
    @FXML private TextField priceField;

    private Car car;
    private final CarService carService = new CarService(); // Service pour vérifier les doublons

    public void setCar(Car car) {
        this.car = car;
        brandField.setText(car.getBrand());
        modelField.setText(car.getModel());
        licensePlateField.setText(car.getLicensePlate());
        priceField.setText(car.getRentalPricePerDay().toString());

        // Ajouter un listener pour interdire les prix négatifs
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldValue);
            }
        });
    }

    @FXML
    private void handleSave() {
        // Validation de l'immatriculation
        String newLicensePlate = licensePlateField.getText();
        if (!newLicensePlate.equals(car.getLicensePlate()) && carService.licensePlateExists(newLicensePlate)) {
            showError("This license plate already exists!");
            return;
        }

        // Validation du prix (ne doit pas être négatif)
        String priceText = priceField.getText().trim().replace(",", ".");
        try {
            BigDecimal price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                showError("Price cannot be negative.");
                return;
            }
            car.setRentalPricePerDay(price);
        } catch (NumberFormatException e) {
            showError("Invalid price format.");
            return;
        }

        // Mise à jour des autres champs
        car.setBrand(brandField.getText());
        car.setModel(modelField.getText());
        car.setLicensePlate(newLicensePlate);

        // Close the dialog
        brandField.getScene().getWindow().hide();
    }

    public Car getUpdatedCar() {
        return car;
    }

    // Méthode pour afficher les erreurs
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
