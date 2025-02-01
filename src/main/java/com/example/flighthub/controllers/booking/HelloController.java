package com.example.flighthub.controllers.booking;

import com.example.flighthub.models.Flight;
import com.example.flighthub.services.FlightService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField flightNumberField;

    @FXML
    private TextField aircraftIdField;

    @FXML
    private TextField departureAirportIdField;

    @FXML
    private TextField arrivalAirportIdField;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField arrivalTimeField;

    @FXML
    private TextField priceField;

    private FlightService flightService = new FlightService();

    @FXML
    public void handleAddFlight() {
        // Récupérer les valeurs des champs de texte
        String flightNumber = flightNumberField.getText();
        int aircraftId = Integer.parseInt(aircraftIdField.getText());
        int departureAirportId = Integer.parseInt(departureAirportIdField.getText());
        int arrivalAirportId = Integer.parseInt(arrivalAirportIdField.getText());
        String departureTime = departureTimeField.getText();
        String arrivalTime = arrivalTimeField.getText();
        double price = Double.parseDouble(priceField.getText());

        // Créer un nouvel objet Flight
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAircraftId(aircraftId);
        flight.setDepartureAirportId(departureAirportId);
        flight.setArrivalAirportId(arrivalAirportId);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setPrice(price);

        // Appeler le service pour ajouter le vol
        boolean success = flightService.createFlight(flight);
        if (success) {
            System.out.println("Vol ajouté avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout du vol.");
        }
    }
}