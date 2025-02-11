package com.example.flighthub.controllers.car;

import com.example.flighthub.models.Car;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.math.BigDecimal;

public class EditCarDialogController {
    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField licensePlateField;
    @FXML private TextField priceField;

    private Car car;

    public void setCar(Car car) {
        this.car = car;
        brandField.setText(car.getBrand());
        modelField.setText(car.getModel());
        licensePlateField.setText(car.getLicensePlate());
        priceField.setText(car.getRentalPricePerDay().toString());
    }

    @FXML
    private void handleSave() {
        car.setBrand(brandField.getText());
        car.setModel(modelField.getText());
        car.setLicensePlate(licensePlateField.getText());
        car.setRentalPricePerDay(new BigDecimal(priceField.getText().trim().replace(",", ".")));

        // Close the dialog
        brandField.getScene().getWindow().hide();
    }

    public Car getUpdatedCar() {
        return car;
    }
}