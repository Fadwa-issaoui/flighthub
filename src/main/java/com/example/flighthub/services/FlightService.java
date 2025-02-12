package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Flight;

import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FlightService {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // ... (Other methods remain the same) ...
    public Map<String, Integer> getFlightCountsByDepartureLocation() {
        //the same code
        Map<String, Integer> counts = new HashMap<>();
        String query = "SELECT a.location, COUNT(f.flightId) AS flight_count " +
                "FROM flight f " +
                "JOIN airport a ON f.departureAirportId = a.airportId " +
                "GROUP BY a.location";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String location = rs.getString("location");
                int count = rs.getInt("flight_count");
                counts.put(location, count);
            }
        } catch (SQLException e) {
            System.err.println("Error getting flight counts by departure location: " + e.getMessage());
        }
        return counts;
    }

    public Map<Month, Long> getFlightCountsByMonth() {
        Map<Month, Long> counts = new TreeMap<>(); // Use TreeMap to keep months in order
        // Initialize the map with all months and 0 counts
        for (Month month : Month.values()) {
            counts.put(month, 0L);
        }

        String query = "SELECT DATE(departureTime) AS flight_date, COUNT(flightId) AS flight_count FROM flight GROUP BY DATE(departureTime)"; // Corrected

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String dateString = rs.getString("flight_date"); // Get DATE part as string
                long count = rs.getLong("flight_count");

                try {
                    // Parse the date string to LocalDate
                    LocalDate date = LocalDate.parse(dateString);
                    //Extract the month
                    Month month = date.getMonth();
                    //If the month is existing, sum up the counts, otherwise add it.
                    counts.put(month, counts.getOrDefault(month, 0L) + count);

                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date: " + dateString + " - " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting flight counts by month: " + e.getMessage());
        }
        return counts;
    }

    // NEW METHOD: Get flight counts per day
    public Map<LocalDate, Long> getFlightCountsByDay() {
        Map<LocalDate, Long> counts = new TreeMap<>(); // TreeMap for chronological order
        String query = "SELECT DATE(departureTime) AS flight_date, COUNT(flightId) AS flight_count " +
                "FROM flight GROUP BY DATE(departureTime) ORDER BY flight_date";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LocalDate date = rs.getDate("flight_date").toLocalDate(); // Directly get as LocalDate
                long count = rs.getLong("flight_count");
                counts.put(date, count);
            }
        } catch (SQLException e) {
            System.err.println("Error getting flight counts by day: " + e.getMessage());
        }
        return counts;
    }


    // NEW METHOD: Get flight counts grouped by departure AND arrival location
    public Map<String, Map<String, Integer>> getFlightCountsByDepartureAndArrival() {
        Map<String, Map<String, Integer>> result = new HashMap<>();
        String query = "SELECT dep.location AS departure, arr.location AS arrival, COUNT(f.flightId) AS flight_count " +
                "FROM flight f " +
                "JOIN airport dep ON f.departureAirportId = dep.airportId " +
                "JOIN airport arr ON f.arrivalAirportId = arr.airportId " +
                "GROUP BY dep.location, arr.location";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String departure = rs.getString("departure");
                String arrival = rs.getString("arrival");
                int count = rs.getInt("flight_count");

                // Get the inner map for the departure location. If it doesn't exist, create it.
                Map<String, Integer> arrivalCounts = result.computeIfAbsent(departure, k -> new HashMap<>());

                // Add the arrival location and count to the inner map.
                arrivalCounts.put(arrival, count);
            }
        } catch (SQLException e) {
            System.err.println("Error getting flight counts by departure and arrival: " + e.getMessage());
        }
        return result;
    }

    // NEW METHOD: Calculate the average flight price
    public double getAverageFlightPrice() {
        String query = "SELECT AVG(price) AS average_price FROM flight";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("average_price"); // Returns 0.0 if no flights exist
            }
        } catch (SQLException e) {
            System.err.println("Error calculating average flight price: " + e.getMessage());
        }
        return 0.0; // Return 0 if there's an error or no flights
    }

    // NEW METHOD: Find the most popular destination
    public String getMostPopularDestination() {
        String query = "SELECT a.location, COUNT(f.flightId) AS flight_count " +
                "FROM flight f " +
                "JOIN airport a ON f.arrivalAirportId = a.airportId " +
                "GROUP BY a.location " +
                "ORDER BY flight_count DESC " +
                "LIMIT 1";  // Get only the top result

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("location");
            }
        } catch (SQLException e) {
            System.err.println("Error getting most popular destination: " + e.getMessage());
        }
        return "No flights found"; // Return a message if no flights exist
    }

    // NEW METHOD: Get total number of passengers on completed flights
    public int getTotalPassengersOnCompletedFlights() {
        String query = "SELECT SUM(a.capacity) AS total_passengers " +
                "FROM flight f " +
                "JOIN aircraft a ON f.aircraftId = a.aircraftId " +
                "WHERE f.arrivalTime < CURRENT_TIMESTAMP"; // Only completed flights

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_passengers"); // Returns 0 if no flights exist
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total passengers: " + e.getMessage());
        }
        return 0;
    }
    // Create a new flight
    public int createFlight(Flight flight) {
        //the same code
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
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating flight failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Read a flight by flightId
    public Flight getFlightById(int flightId) {
        //the same code
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
        //the same code
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
        //the same code
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
        //the same code
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
    // Inside FlightService

    public int getCompletedFlightsCount() {
        String query = "SELECT COUNT(*) FROM flight WHERE arrivalTime < CURRENT_TIMESTAMP"; // Completed
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1); // Get the count directly
            }
        } catch (SQLException e) {
            System.err.println("Error getting completed flights count: " + e.getMessage());
        }
        return 0; // Return 0 if error or no flights
    }

    public int getFlightsNotCompletedCount() {
        String query = "SELECT COUNT(*) FROM flight WHERE arrivalTime >= CURRENT_TIMESTAMP"; // Not completed
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting not completed flights count: " + e.getMessage());
        }
        return 0;
    }
}