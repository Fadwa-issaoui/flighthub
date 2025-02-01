package com.example.flighthub.services;


import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Flight;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightService {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // Create a new flight
    public void createFlight(Flight flight) {
        // Modify query to not include flightId column
        String query = "INSERT INTO flights (flightNumber, aircraftId, departureAirportId, arrivalAirportId, departureTime, arrivalTime, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {


            pstmt.setString(1, flight.getFlightNumber());
            pstmt.setInt(2, flight.getAircraftId());
            pstmt.setInt(3, flight.getDepartureAirportId());
            pstmt.setInt(4, flight.getArrivalAirportId());
            pstmt.setString(5, flight.getDepartureTime());
            pstmt.setString(6, flight.getArrivalTime());
            pstmt.setDouble(7, flight.getPrice());

            pstmt.executeUpdate();
            System.out.println("Flight created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read a flight by flightId
    public Flight getFlightById(int flightId) {
        String query = "SELECT * FROM flights WHERE flightId = ?";
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
        String query = "SELECT * FROM flights";
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
        String query = "UPDATE flights SET flightNumber = ?, aircraftId = ?, departureAirportId = ?, arrivalAirportId = ?, departureTime = ?, arrivalTime = ?, price = ? WHERE flightId = ?";
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

    // Delete a flight by flightId
    public void deleteFlight(int flightId) {
        String query = "DELETE FROM flights WHERE flightId = ?";
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