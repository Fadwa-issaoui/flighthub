package com.example.flighthub.services;

import com.example.flighthub.models.Booking;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingService {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // Create a new booking
    public void createBooking(Booking booking) {
        String query = "INSERT INTO booking (bookingDate, passengerId, flightId) VALUES (?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1,booking.getBookingDate());
            pstmt.setInt(2, booking.getPassengerId());
            pstmt.setInt(3, booking.getFlightId());

            pstmt.executeUpdate();
            System.out.println("Booking created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read a booking by bookingId
    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM bookings WHERE bookingId = ?";
        Booking booking = null;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                booking = new Booking();
                booking.setBookingId(rs.getInt("bookingId"));
                booking.setStatus(rs.getString("status"));
                booking.setBookingDate(rs.getString("bookingDate"));
                booking.setPassengerId(rs.getInt("passengerId"));
                booking.setFlightId(rs.getInt("flightId"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booking;
    }

    // Update an existing booking
    public void updateBooking(Booking booking) {
        String query = "UPDATE bookings SET status = ?, bookingDate = ?, passengerId = ?, flightId = ? WHERE bookingId = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, booking.getStatus());
            pstmt.setString(2, booking.getBookingDate());
            pstmt.setInt(3, booking.getPassengerId());
            pstmt.setInt(4, booking.getFlightId());
            pstmt.setInt(5, booking.getBookingId());

            pstmt.executeUpdate();
            System.out.println("Booking updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a booking by bookingId
    public void deleteBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE bookingId = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
            System.out.println("Booking deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}