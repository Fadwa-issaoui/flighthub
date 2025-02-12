package com.example.flighthub.controllers.car;

import com.example.flighthub.models.Car;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.math.BigDecimal;
import javafx.scene.control.Alert;

public class AddCarDialogController {
    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField licensePlateField;
    @FXML private TextField priceField;

    private Car newCar;

    @FXML
    public void initialize() {
        // Empêcher les caractères invalides dans le champ de prix
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                priceField.setText(oldValue); // Empêche la saisie de caractères non numériques
            }
        });
    }


    @FXML
    private void handleAdd() {
        try {
            String brand = brandField.getText();
            String model = modelField.getText();
            String licensePlate = licensePlateField.getText();
            BigDecimal price = new BigDecimal(priceField.getText().trim().replace(",", "."));

            // Vérifier que le prix est positif
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                showError("Price cannot be negative!");
                return;
            }

            // Create a new car object
            newCar = new Car(0, brand, model, licensePlate, price);

            // Close the dialog
            brandField.getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            // Show error if the price is invalid
            showError("Invalid price format. Please enter a valid number.");
        }
    }

    public Car getNewCar() {
        return newCar;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}