package com.example.flighthub.controllers.car;

import com.example.flighthub.models.Car;
import com.example.flighthub.models.Location;
import com.example.flighthub.services.CarRentalService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class RentCarController {
    @FXML private ListView<Location> locationListView;
    @FXML private Label statusLabel;

    private final CarRentalService carRentalService;

    public RentCarController() {
        this.carRentalService = new CarRentalService();
    }

    @FXML
    public void initialize() {
        loadAllLocations();
    }

    private void loadAllLocations() {
        List<Location> locations = carRentalService.getAllLocations();
        locationListView.getItems().clear();
        locationListView.getItems().addAll(locations);
        locationListView.setCellFactory(param -> new ListCell<Location>() {
            @Override
            protected void updateItem(Location location, boolean empty) {
                super.updateItem(location, empty);
                if (empty || location == null) {
                    setText(null);
                } else {
                    setText(location.getCustomerName() + " - " + location.getCar().getBrand() + " " + location.getCar().getModel() + " (" + location.getRentalDate() + ")");
                }
            }
        });
    }

    @FXML
    public void handleAddRent() {
        showRentDialog(null);
    }

    @FXML
    public void handleUpdateRent() {
        Location selectedLocation = locationListView.getSelectionModel().getSelectedItem();
        if (selectedLocation != null) {
            showRentDialog(selectedLocation);
        } else {
            statusLabel.setText("Please select a rent to update.");
        }
    }

    @FXML
    public void handleDeleteRent() {
        Location selectedLocation = locationListView.getSelectionModel().getSelectedItem();
        if (selectedLocation != null) {
            carRentalService.deleteLocation(selectedLocation.getId());
            statusLabel.setText("Rent deleted successfully!");
            loadAllLocations();
        } else {
            statusLabel.setText("Please select a rent to delete.");
        }
    }



    private void showRentDialog(Location location) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/rent_dialog.fxml"));
            VBox dialogRoot = loader.load();

            RentDialogController dialogController = loader.getController();
            dialogController.setRentCarController(this);
            dialogController.setLocation(location);

            // Create a new Stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(location == null ? "Add Rent" : "Update Rent");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Make the dialog modal
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.showAndWait(); // Show the dialog and wait for it to close
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshRentList() {
        // Reload all locations from the database
        List<Location> locations = carRentalService.getAllLocations();

        // Clear the ListView and add the updated data
        locationListView.getItems().clear();
        locationListView.getItems().addAll(locations);

        // Optional: Print debug information
        System.out.println("Rent list refreshed. Total rents: " + locations.size());
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_fl_dash.fxml"));
        Parent dashboardRoot = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(dashboardRoot));
        stage.show();
    }




    public CarRentalService getCarRentalService() {
        return carRentalService;
    }
}
