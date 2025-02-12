package com.example.flighthub.controllers.car;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmailDialogController {
    @FXML private TextField emailField;

    private RentDialogController rentDialogController;

    public void setRentDialogController(RentDialogController rentDialogController) {
        this.rentDialogController = rentDialogController;
    }

    @FXML
    private void handleSendEmail() {
        String recipientEmail = emailField.getText().trim();

        if (recipientEmail.isEmpty() || !recipientEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        rentDialogController.sendEmail(recipientEmail);
        closeDialog();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}
