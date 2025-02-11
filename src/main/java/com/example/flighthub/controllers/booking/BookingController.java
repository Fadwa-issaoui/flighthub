package com.example.flighthub.controllers.booking;

import com.example.flighthub.models.Booking;
import com.example.flighthub.models.Passenger;
import com.example.flighthub.services.BookingService;
import com.example.flighthub.services.PassengerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class BookingController {

    private final BookingService bookingService = new BookingService();
    private final PassengerService passengerService = new PassengerService();

    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private DatePicker birthDate;
    @FXML private TextField national;
    @FXML private TextField passNum;

    @FXML private Label fromLabel;
    @FXML private Label toLabel;
    @FXML private Label dateLabel;



    public void setFlightData(LocalDate date, String destination, String departure,int idFlight) {
        this.dateLabel.setText(date.toString());
        this.toLabel.setText(destination);
        this.fromLabel.setText(departure);
    }

    public void handleBooking(ActionEvent event) throws IOException {
        Passenger p = new Passenger();
        p.setFirstName(firstName.getText());
        p.setLastName(lastName.getText());
        p.setDateOfBirth(birthDate.getAccessibleText());
        p.setPassportNumber(passNum.getText());
        p.setNationality(national.getText());

        var passId = passengerService.addPassenger(p);
        if(passId > 0) {
            Booking booking = new Booking();
            booking.setBookingDate((new Date()).toString());
            booking.setPassengerId(passId);
            booking.setFlightId(1);
            bookingService.createBooking(booking);
        }
        handleBack(event);

    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_fl_dash.fxml"));
        Parent root = loader.load();


        // Switch scene
        Stage stage = (Stage) lastName.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}