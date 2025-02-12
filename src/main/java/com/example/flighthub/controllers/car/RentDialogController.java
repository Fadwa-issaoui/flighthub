package com.example.flighthub.controllers.car;

import com.example.flighthub.models.Car;
import com.example.flighthub.models.Location;
import com.example.flighthub.services.CarRentalService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.util.Optional;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.time.LocalDate;
import java.util.List;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class RentDialogController {
    @FXML private TextField customerNameField;
    @FXML private DatePicker rentalDatePicker;
    @FXML private TextField rentalDaysField;
    @FXML private ComboBox<Car> carComboBox;

    private RentCarController rentCarController;
    private Location location;

    public void setRentCarController(RentCarController rentCarController) {
        this.rentCarController = rentCarController;
        loadAvailableCars(); // Load cars after rentCarController is set
    }

    public void setLocation(Location location) {
        this.location = location;
        if (location != null) {
            customerNameField.setText(location.getCustomerName());
            rentalDatePicker.setValue(location.getRentalDate());
            rentalDaysField.setText(String.valueOf(location.getRentalDays()));
            carComboBox.getSelectionModel().select(location.getCar());
        }
    }

    private void loadAvailableCars() {
        if (rentCarController == null) {
            throw new IllegalStateException("RentCarController is not set.");
        }

        CarRentalService carRentalService = rentCarController.getCarRentalService();
        List<Car> availableCars = carRentalService.getAvailableCars();
        carComboBox.getItems().addAll(availableCars);
        carComboBox.setCellFactory(param -> new ListCell<Car>() {
            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);
                if (empty || car == null) {
                    setText(null);
                } else {
                    setText(car.getBrand() + " " + car.getModel() + " (" + car.getLicensePlate() + ")");
                }
            }
        });
        carComboBox.setButtonCell(new ListCell<Car>() {
            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);
                if (empty || car == null) {
                    setText(null);
                } else {
                    setText(car.getBrand() + " " + car.getModel() + " (" + car.getLicensePlate() + ")");
                }
            }
        });
    }

    @FXML
    public void handleSave() {
        System.out.println("Save button clicked!"); // Debugging statement
        String customerName = customerNameField.getText();
        LocalDate rentalDate = rentalDatePicker.getValue();
        int rentalDays = Integer.parseInt(rentalDaysField.getText());
        Car selectedCar = carComboBox.getSelectionModel().getSelectedItem();

        if (selectedCar != null && rentalDate != null && !customerName.isEmpty()) {
            // Calculate the total cost
            BigDecimal totalCost = selectedCar.getRentalPricePerDay().multiply(BigDecimal.valueOf(rentalDays));

            if (location == null) {
                // Add new rent
                Location newLocation = new Location(0, selectedCar, customerName, rentalDate, rentalDays);
                rentCarController.getCarRentalService().rentCar(newLocation);
            } else {
                // Update existing rent
                location.setCustomerName(customerName);
                location.setRentalDate(rentalDate);
                location.setRentalDays(rentalDays);
                location.setCar(selectedCar);
                rentCarController.getCarRentalService().updateLocation(location.getId(), customerName, rentalDays);
            }

            // Show the total cost in an alert
            showTotalCostAlert(totalCost);

            // Notify the RentCarController to refresh the UI
            rentCarController.refreshRentList();
            closeDialog();
        } else {
            System.err.println("Validation failed: Please fill all fields and select a car."); // Debugging statement
        }
    }

    private void showTotalCostAlert(BigDecimal totalCost) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rental Confirmation");
        alert.setHeaderText("Rental Successful!");
        alert.setContentText("Total Cost: $" + totalCost); // Display the total cost
        alert.showAndWait();
    }

    @FXML
    public void handleCancel() {
        System.out.println("Cancel button clicked!"); // Debugging statement
        closeDialog();
    }

//    @FXML
//    public void handleSaveAndSendEmail() {
//        // First, save the rental information
//        handleSave();
//
//        // Show a dialog to input the recipient's email address
//        TextInputDialog dialog = new TextInputDialog();
//        dialog.setTitle("Send Email");
//        dialog.setHeaderText("Enter the recipient's email address:");
//        dialog.setContentText("Email:");
//
//        // Force focus on the input field after the dialog is shown
//        dialog.setOnShown(event -> {
//            TextField inputField = dialog.getEditor();
//            inputField.requestFocus();
//        });
//
//        Optional<String> result = dialog.showAndWait();
//        result.ifPresent(email -> {
//            // Send the email
//            sendEmail(email);
//        });
//    }

    @FXML
    public void handleSaveAndSendEmail() {
        handleSave(); // First, save the rental details

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/EmailDialog.fxml"));
            Parent root = loader.load();

            EmailDialogController emailDialogController = loader.getController();
            emailDialogController.setRentDialogController(this);

            Stage stage = new Stage();
            stage.setTitle("Send Email");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Wait until the user submits the email

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void sendEmail(String recipientEmail) {
        final String username = "norchenegarouachi@gmail.com"; // Your Gmail address
        final String password = "uqjirvjjgdvgjarw"; // Your Gmail password or App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Rental Confirmation");
            message.setText("Dear " + customerNameField.getText() + ",\n\n"
                    + "Your rental has been confirmed.\n"
                    + "Car: " + carComboBox.getSelectionModel().getSelectedItem().getBrand() + " " + carComboBox.getSelectionModel().getSelectedItem().getModel() + "\n"
                    + "Rental Date: " + rentalDatePicker.getValue() + "\n"
                    + "Rental Days: " + rentalDaysField.getText() + "\n\n"
                    + "Thank you for choosing our service!");

            Transport.send(message);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email Sent");
            alert.setHeaderText(null);
            alert.setContentText("Email sent successfully to " + recipientEmail);
            alert.showAndWait();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    private void closeDialog() {
        customerNameField.getScene().getWindow().hide();
    }
}