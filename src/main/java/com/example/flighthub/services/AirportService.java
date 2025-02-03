package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Airport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportService {

    private static final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // CREATE - Add a new airport
    public void createAirport(Airport airport) {
        String query = "INSERT INTO airport (airportId, name, location, code) VALUES (?, ?, ?, ?)";
        try (
             PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setInt(1, airport.getAirportId());
            pstmt.setString(2, airport.getName());
            pstmt.setString(3, airport.getLocation());
            pstmt.setString(4, airport.getCode());

            pstmt.executeUpdate();
            System.out.println("✅ Airport added successfully: " + airport);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Get an airport by ID
    public Airport getAirportById(int airportId) {
        String query = "SELECT * FROM airport WHERE airportId = ?";
        Airport airport = null;

        try (
             PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setInt(1, airportId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                airport = new Airport(
                        rs.getInt("airportId"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("code")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airport;
    }

    // UPDATE - Update an airport
    public void updateAirport(Airport airport) {
        String query = "UPDATE airport SET name = ?, location = ?, code = ? WHERE airportId = ?";

        try (
             PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setString(1, airport.getName());
            pstmt.setString(2, airport.getLocation());
            pstmt.setString(3, airport.getCode());
            pstmt.setInt(4, airport.getAirportId());

            pstmt.executeUpdate();
            System.out.println("✅ Airport updated: " + airport);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE - Delete an airport by ID
    public void deleteAirport(int airportId) {
        String query = "DELETE FROM airport WHERE airportId = ?";

        try (
             PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setInt(1, airportId);
            pstmt.executeUpdate();
            System.out.println("✅ Airport deleted (ID: " + airportId + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL - Get all airports
    public List<Airport> getAllAirports() {
        List<Airport> airports = new ArrayList<>();
        String query = "SELECT * FROM airport";

        try (
             Statement stmt = databaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                airports.add(new Airport(
                        rs.getInt("airportId"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("code")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }

    // SEARCH - Get airports by partial match (ID, name, location, or code)
    public List<Airport> getAirportsByPartialSearch(String searchText) {
        List<Airport> airports = new ArrayList<>();
        String query = "SELECT * FROM airport WHERE CAST(airportId AS CHAR) LIKE ? OR name LIKE ? OR location LIKE ? OR code LIKE ?";

        try (
             PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            String likeSearch = searchText + "%";
            pstmt.setString(1, likeSearch);
            pstmt.setString(2, likeSearch);
            pstmt.setString(3, likeSearch);
            pstmt.setString(4, likeSearch);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                airports.add(new Airport(
                        rs.getInt("airportId"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("code")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }

    public static void main(String[] args) {
        AirportService airportService = new AirportService();

        airportService.deleteAirport(1);
        System.out.println("Airport with ID 1 deleted.");
        // 1. Add a new airport

        Airport airport1 = new Airport(1, "John F. Kennedy International Airport", "New York, USA", "JFK");
        Airport airport2 = new Airport(2, "Los Angeles International Airport", "Los Angeles, USA", "LAX");

        airportService.createAirport(airport1);
        airportService.createAirport(airport2);

        // 2. Retrieve and display an airport by ID
        Airport retrievedAirport = airportService.getAirportById(1);
        if (retrievedAirport != null) {
            System.out.println("Retrieved Airport: " + retrievedAirport);
        } else {
            System.out.println("Airport not found.");
        }

        // 3. Update airport information
        airport1.setName("JFK International Airport");
        airportService.updateAirport(airport1);
        System.out.println("Updated Airport: " + airportService.getAirportById(1));

        // 4. List all airports
        System.out.println("All Airports:");
        List<Airport> airports = airportService.getAllAirports();
        for (Airport a : airports) {
            System.out.println(a);
        }

        // 5. Search airports by name or code
        System.out.println("Search Results for 'L':");
        List<Airport> searchResults = airportService.getAirportsByPartialSearch("L");
        for (Airport a : searchResults) {
            System.out.println(a);
        }

        // 6. Delete an airport
        airportService.deleteAirport(2);
        System.out.println("Airport with ID 2 deleted.");

        // 7. List all airports after deletion
        System.out.println("All Airports After Deletion:");
        airports = airportService.getAllAirports();
        for (Airport a : airports) {
            System.out.println(a);
        }
    }
}
