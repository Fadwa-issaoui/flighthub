package com.example.flighthub.controllers.aircraft;

import com.example.flighthub.models.Aircraft;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AircraftController {

    @FXML private TextField aircraftIdField;
    @FXML private TextField modelField;
    @FXML private TextField capacityField;
    @FXML private CheckBox isAvailableCheckBox;
    @FXML private TableView<Aircraft> aircraftTableView;
    @FXML private TableColumn<Aircraft, Integer> colAircraftId;
    @FXML private TableColumn<Aircraft, String> colModel;
    @FXML private TableColumn<Aircraft, Integer> colCapacity;
    @FXML private TableColumn<Aircraft, Boolean> colAvailable;

    private ObservableList<Aircraft> aircraftList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialisation des colonnes de la TableView
        colAircraftId.setCellValueFactory(new PropertyValueFactory<>("aircraftId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        // Liaison des données
        aircraftTableView.setItems(aircraftList);
    }

    @FXML
    private void handleAddAircraft() {
        try {
            int id = Integer.parseInt(aircraftIdField.getText());
            String model = modelField.getText();
            int capacity = Integer.parseInt(capacityField.getText());
            boolean available = isAvailableCheckBox.isSelected();

            // Vérification si l'ID existe déjà
            for (Aircraft aircraft : aircraftList) {
                if (aircraft.getAircraftId() == id) {
                    showAlert("Erreur", "Un avion avec cet ID existe déjà !");
                    return;
                }
            }

            // Création d'un nouvel avion et ajout à la liste
            Aircraft newAircraft = new Aircraft();
            newAircraft.setAircraftId(id);
            newAircraft.setModel(model);
            newAircraft.setCapacity(capacity);
            newAircraft.setAvailable(available);

            aircraftList.add(newAircraft);

            // Nettoyage des champs
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides !");
        }
    }

    @FXML
    private void handleUpdateAircraft() {
        Aircraft selectedAircraft = aircraftTableView.getSelectionModel().getSelectedItem();
        if (selectedAircraft != null) {
            try {
                selectedAircraft.setModel(modelField.getText());
                selectedAircraft.setCapacity(Integer.parseInt(capacityField.getText()));
                selectedAircraft.setAvailable(isAvailableCheckBox.isSelected());

                aircraftTableView.refresh(); // Met à jour la TableView
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des valeurs valides !");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un avion à modifier !");
        }
    }

    @FXML
    private void handleDeleteAircraft() {
        Aircraft selectedAircraft = aircraftTableView.getSelectionModel().getSelectedItem();
        if (selectedAircraft != null) {
            aircraftList.remove(selectedAircraft);
            clearFields();
        } else {
            showAlert("Erreur", "Veuillez sélectionner un avion à supprimer !");
        }
    }

    private void clearFields() {
        aircraftIdField.clear();
        modelField.clear();
        capacityField.clear();
        isAvailableCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
