package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Airport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportService {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // CREATE - Ajouter un nouvel aéroport
    public void createAirport(Airport airport) {
        String query = "INSERT INTO airport (name, location, code) VALUES (?, ?, ?)";
        try(
                PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setString(1, airport.getName());
            pstmt.setString(2, airport.getLocation());
            pstmt.setString(3, airport.getCode());

            pstmt.executeUpdate();
            System.out.println("✅ Aéroport ajouté avec succès : " + airport);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Obtenir un aéroport par ID
    public Airport getAirportById(int airportId) {
        String query = "SELECT * FROM airport WHERE airportId = ?";
        Airport airport = null;

        try(
                PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setInt(1, airportId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                airport = new Airport();
                airport.setAirportId(rs.getInt("airportId"));
                airport.setName(rs.getString("name"));
                airport.setLocation(rs.getString("location"));
                airport.setCode(rs.getString("code"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airport;
    }

    // UPDATE - Mettre à jour un aéroport
    public void updateAirport(Airport airport) {
        String query = "UPDATE airport SET name = ?, location = ?, code = ? WHERE airportId = ?";

        try(
                PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setString(1, airport.getName());
            pstmt.setString(2, airport.getLocation());
            pstmt.setString(3, airport.getCode());
            pstmt.setInt(4, airport.getAirportId());

            pstmt.executeUpdate();
            System.out.println("✅ Aéroport mis à jour : " + airport);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE - Supprimer un aéroport par ID
    public void deleteAirport(int airportId) {
        String query = "DELETE FROM airport WHERE airportId = ?";

        try(
             PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {

            pstmt.setInt(1, airportId);
            pstmt.executeUpdate();
            System.out.println("✅ Aéroport supprimé (ID: " + airportId + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL - Obtenir tous les aéroports
    public List<Airport> getAllAirports() {
        List<Airport> airports = new ArrayList<>();
        String query = "SELECT * FROM airport";

        try (
             Statement stmt = databaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Airport airport = new Airport();
                airport.setAirportId(rs.getInt("airportId"));
                airport.setName(rs.getString("name"));
                airport.setLocation(rs.getString("location"));
                airport.setCode(rs.getString("code"));
                airports.add(airport);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }

    public static void main(String[] args) {
        AirportService airportService = new AirportService();

        // Creating a new airport
        Airport newAirport = new Airport();
        newAirport.setName("Aéroport de Tunis");
        newAirport.setLocation("Tunis, Tunisie");
        newAirport.setCode("TUN");
        airportService.createAirport(newAirport);


        // Retrieving an airport by ID
        Airport retrievedAirport = airportService.getAirportById(1);
        if (retrievedAirport != null) {
            System.out.println("Aéroport récupéré: " + retrievedAirport);

            // Updating airport
            retrievedAirport.setLocation("Tunis, Tunisia");
            airportService.updateAirport(retrievedAirport);
        } else {
            System.out.println("Aéroport non trouvé !");
        }

        // Deleting an airport
        airportService.deleteAirport(1);

        // Retrieve all airports
        List<Airport> allAirports = airportService.getAllAirports();
        System.out.println("Tous les aéroports: ");
        for (Airport airport : allAirports) {
            System.out.println(airport);
        }
    }
}

