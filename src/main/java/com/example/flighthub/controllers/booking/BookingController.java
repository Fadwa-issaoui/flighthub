package com.example.flighthub.controllers.booking;

import com.example.flighthub.models.Booking;
import com.example.flighthub.services.BookingService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BookingController {

    private final BookingService bookingService = new BookingService();

    @FXML private TextField txtBookingId;
    @FXML private TextField txtStatus;
    @FXML private TextField txtBookingDate;
    @FXML private TextField txtPassengerId;
    @FXML private TextField txtFlightId;

    @FXML
    private void createBooking() {
        try {
            Booking booking = new Booking();
            booking.setStatus(txtStatus.getText());
            booking.setBookingDate(txtBookingDate.getText());
            booking.setPassengerId(Integer.parseInt(txtPassengerId.getText()));
            booking.setFlightId(Integer.parseInt(txtFlightId.getText()));
            bookingService.createBooking(booking);
            System.out.println("Booking Created!");
        } catch (Exception e) {
            System.out.println("Error creating booking: " + e.getMessage());
        }
    }

    @FXML
    private void getBooking() {
        try {
            int bookingId = Integer.parseInt(txtBookingId.getText());
            Booking booking = bookingService.getBookingById(bookingId);
            if (booking != null) {
                txtStatus.setText(booking.getStatus());
                txtBookingDate.setText(booking.getBookingDate());
                txtPassengerId.setText(String.valueOf(booking.getPassengerId()));
                txtFlightId.setText(String.valueOf(booking.getFlightId()));
                System.out.println("Booking Retrieved!");
            } else {
                System.out.println("No Booking Found.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving booking: " + e.getMessage());
        }
    }

    @FXML
    private void updateBooking() {
        try {
            Booking booking = bookingService.getBookingById(Integer.parseInt(txtBookingId.getText()));
            booking.setStatus(txtStatus.getText());
            booking.setBookingDate(txtBookingDate.getText());
            booking.setPassengerId(Integer.parseInt(txtPassengerId.getText()));
            booking.setFlightId(Integer.parseInt(txtFlightId.getText()));
            bookingService.createBooking(booking);
            System.out.println("Booking Created!");

            bookingService.updateBooking(booking);
            System.out.println("Booking Updated!");
        } catch (Exception e) {
            System.out.println("Error updating booking: " + e.getMessage());
        }
    }

    @FXML
    private void deleteBooking() {
        try {
            int bookingId = Integer.parseInt(txtBookingId.getText());
            bookingService.deleteBooking(bookingId);
            System.out.println("Booking Deleted!");
            clearFields();
        } catch (Exception e) {
            System.out.println("Error deleting booking: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtBookingId.clear();
        txtStatus.clear();
        txtBookingDate.clear();
        txtPassengerId.clear();
        txtFlightId.clear();
    }
}
