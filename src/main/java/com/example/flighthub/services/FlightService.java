
package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightService {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // Create a new flight
    public int createFlight(Flight flight) {
        // Modified query to match table name "flight" and return the generated flightId
        String query = "INSERT INTO flight (flightNumber, aircraftId, departureAirportId, arrivalAirportId, departureTime, arrivalTime, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, flight.getFlightNumber());
            pstmt.setInt(2, flight.getAircraftId());
            pstmt.setInt(3, flight.getDepartureAirportId());
            pstmt.setInt(4, flight.getArrivalAirportId());
            pstmt.setString(5, flight.getDepartureTime());
            pstmt.setString(6, flight.getArrivalTime());
            pstmt.setDouble(7, flight.getPrice());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating flight failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated flightId
                } else {
                    throw new SQLException("Creating flight failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Indicate failure
        }
    }

    // Read a flight by flightId
    public Flight getFlightById(int flightId) {
        String query = "SELECT * FROM flight WHERE flightId = ?";
        Flight flight = null;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, flightId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                flight = new Flight();
                flight.setFlightId(rs.getInt("flightId"));
                flight.setFlightNumber(rs.getString("flightNumber"));
                flight.setAircraftId(rs.getInt("aircraftId"));
                flight.setDepartureAirportId(rs.getInt("departureAirportId"));
                flight.setArrivalAirportId(rs.getInt("arrivalAirportId"));
                flight.setDepartureTime(rs.getString("departureTime"));
                flight.setArrivalTime(rs.getString("arrivalTime"));
                flight.setPrice(rs.getDouble("price"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flight;
    }

    // Read all flights
    public List<Flight> getAllFlights() {
        String query = "SELECT * FROM flight";
        List<Flight> flights = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flightId"));
                flight.setFlightNumber(rs.getString("flightNumber"));
                flight.setAircraftId(rs.getInt("aircraftId"));
                flight.setDepartureAirportId(rs.getInt("departureAirportId"));
                flight.setArrivalAirportId(rs.getInt("arrivalAirportId"));
                flight.setDepartureTime(rs.getString("departureTime"));
                flight.setArrivalTime(rs.getString("arrivalTime"));
                flight.setPrice(rs.getDouble("price"));
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    // Update an existing flight
    public void updateFlight(Flight flight) {
        String query = "UPDATE flight SET flightNumber = ?, aircraftId = ?, departureAirportId = ?, arrivalAirportId = ?, departureTime = ?, arrivalTime = ?, price = ? WHERE flightId = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, flight.getFlightNumber());
            pstmt.setInt(2, flight.getAircraftId());
            pstmt.setInt(3, flight.getDepartureAirportId());
            pstmt.setInt(4, flight.getArrivalAirportId());
            pstmt.setString(5, flight.getDepartureTime());
            pstmt.setString(6, flight.getArrivalTime());
            pstmt.setDouble(7, flight.getPrice());
            pstmt.setInt(8, flight.getFlightId());
            pstmt.executeUpdate();
            System.out.println("Flight updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteFlight(int flightId) {
        String query = "DELETE FROM flight WHERE flightId = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, flightId);
            pstmt.executeUpdate();
            System.out.println("Flight deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}