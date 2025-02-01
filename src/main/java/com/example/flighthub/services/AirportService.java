package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Airport;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportService {

    private static DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // CREATE - Ajouter un nouvel aéroport
    public void createAirport(Airport airport) {
        String query = "INSERT INTO airport (airportId, name, location, code) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, airport.getAirportId());
            pstmt.setString(2, airport.getName());
            pstmt.setString(3, airport.getLocation());
            pstmt.setString(4, airport.getCode());

            pstmt.executeUpdate();
            System.out.println("✅ Aéroport ajouté avec succès : " + airport);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Obtenir un aéroport par ID
    public Airport getAirportById(int airportId) {
        String query = "SELECT * FROM airports WHERE airportId = ?";
        Airport airport = null;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

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
        String query = "UPDATE airports SET name = ?, location = ?, code = ? WHERE airportId = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

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
        String query = "DELETE FROM airports WHERE airportId = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

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
        String query = "SELECT * FROM airports";

        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement();
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
        Airport airport = new Airport();
        airport.setAirportId(1);
        airport.setCode("eya");

        airportService.createAirport(airport);
    }
}

