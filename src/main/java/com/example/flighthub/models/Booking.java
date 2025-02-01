package com.example.flighthub.models;

import lombok.Builder;

public class Booking {

    private int bookingId;
    private String status; // e.g., Confirmed, Canceled, Pending
    private String bookingDate;

    private int passengerId; // Reference to a user
    private int flightId; // Reference to a flight

    public Booking(){}
    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int userId) {
        this.passengerId = userId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + passengerId +
                ", flightId=" + flightId +
                ", bookingDate='" + bookingDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
