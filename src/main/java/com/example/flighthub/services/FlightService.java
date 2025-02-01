package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Airport;
import com.example.flighthub.models.Flight;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightService {

    private Connection connection;
    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    public FlightService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Create a new flight record
    public boolean createFlight(Flight flight) {
        String query = "INSERT INTO flight (flight_number, aircraft_id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, flight.getFlightNumber());
            ps.setInt(2, flight.getAircraftId());
            ps.setInt(3, flight.getDepartureAirportId());
            ps.setInt(4, flight.getArrivalAirportId());
            ps.setString(5, flight.getDepartureTime());
            ps.setString(6, flight.getArrivalTime());
            ps.setDouble(7, flight.getPrice());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all flights
    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setAircraftId(rs.getInt("aircraft_id"));
                flight.setDepartureAirportId(rs.getInt("departure_airport_id"));
                flight.setArrivalAirportId(rs.getInt("arrival_airport_id"));
                flight.setDepartureTime(rs.getString("departure_time"));
                flight.setArrivalTime(rs.getString("arrival_time"));
                flight.setPrice(rs.getDouble("price"));
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    // Update a flight
    public boolean updateFlight(Flight flight) {
        String query = "UPDATE flights SET flight_number = ?, aircraft_id = ?, departure_airport_id = ?, arrival_airport_id = ?, departure_time = ?, arrival_time = ?, price = ? WHERE flight_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, flight.getFlightNumber());
            ps.setInt(2, flight.getAircraftId());
            ps.setInt(3, flight.getDepartureAirportId());
            ps.setInt(4, flight.getArrivalAirportId());
            ps.setString(5, flight.getDepartureTime());
            ps.setString(6, flight.getArrivalTime());
            ps.setDouble(7, flight.getPrice());
            ps.setInt(8, flight.getFlightId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a flight by ID
    public boolean deleteFlight(int flightId) {
        String query = "DELETE FROM flights WHERE flight_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, flightId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a flight by ID
    public Flight getFlightById(int flightId) {
        String query = "SELECT * FROM flights WHERE flight_id = ?";
        Flight flight = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                flight = new Flight();
                flight.setFlightId(rs.getInt("flight_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setAircraftId(rs.getInt("aircraft_id"));
                flight.setDepartureAirportId(rs.getInt("departure_airport_id"));
                flight.setArrivalAirportId(rs.getInt("arrival_airport_id"));
                flight.setDepartureTime(rs.getString("departure_time"));
                flight.setArrivalTime(rs.getString("arrival_time"));
                flight.setPrice(rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flight;
    }
        public static void main(String[] args) {
            FlightService flightService = new FlightService();
            Flight flight = new Flight();

            // Set flight details
            flight.setFlightId(23);
            flight.setFlightNumber("FL123");
            flight.setAircraftId(1);
            flight.setDepartureAirportId(101);
            flight.setArrivalAirportId(202);
            flight.setDepartureTime("2023-10-25 10:00:00");
            flight.setArrivalTime("2023-10-25 12:00:00");
            flight.setPrice(199.99);
    }

}