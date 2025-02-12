package com.example.flighthub.controllers.car;

import com.example.flighthub.models.Car;
import com.example.flighthub.services.CarService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;



public class CarController {
    private final CarService carService = new CarService();

    @FXML
    private BorderPane root; // The root BorderPane in your FXML

    @FXML
    private ImageView backgroundImage; // The ImageView for the background

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, Integer> idColumn;
    @FXML private TableColumn<Car, String> brandColumn;
    @FXML private TableColumn<Car, String> modelColumn;
    @FXML private TableColumn<Car, String> licensePlateColumn;
    @FXML private TableColumn<Car, BigDecimal> priceColumn;
    @FXML private TableColumn<Car, Boolean> availableColumn;

    @FXML
    public void initialize() {
        //VBox.setVgrow(carTable, Priority.ALWAYS);


        carTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Set cell value factories to bind the columns with car object properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        licensePlateColumn.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("rentalPricePerDay"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        // Set table data from the carService (retrieve all cars)
        carTable.setItems(carService.getAllCars());


    }



    @FXML
    private void addCar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/add-car-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Car");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(carTable.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            AddCarDialogController controller = loader.getController();

            dialogStage.showAndWait();

            Car newCar = controller.getNewCar();
            if (newCar != null) {
                // Vérifier si l'immatriculation existe déjà
                if (carService.licensePlateExists(newCar.getLicensePlate())) {
                    showError("A car with the same license plate already exists!");
                    return;
                }

                // Ajouter la voiture et rafraîchir la table
                carService.addCar(newCar);
                carTable.setItems(carService.getAllCars());
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the add dialog.");
        }
    }


    @FXML
    private void editCar() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showError("No car selected");
            return;
        }

        try {
            // Load the FXML file for the pop-up window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/edit-car-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Car");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(carTable.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            // Get the controller and set the selected car
            EditCarDialogController controller = loader.getController();
            controller.setCar(selectedCar);

            // Show the dialog and wait for it to close
            dialogStage.showAndWait();

            // Update the car in the database and refresh the table
            Car updatedCar = controller.getUpdatedCar();
            if (updatedCar != null) {
                carService.updateCar(updatedCar);
                carTable.setItems(carService.getAllCars());
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the edit dialog.");
        }
    }

    @FXML
    private void deleteCar() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            carService.deleteCar(selectedCar.getId()); // Delete car from the database
            carTable.setItems(carService.getAllCars()); // Refresh table
        } else {
            showError("No car selected");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}