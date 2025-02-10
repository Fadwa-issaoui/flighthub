package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Airport;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportService {

    private static final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // Check if an airport ID already exists
    private boolean isAirportIdExists(int airportId) {
        try {
            PreparedStatement checkStmt = databaseConnection.getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM airport WHERE airportId = ?");
            checkStmt.setInt(1, airportId);
            ResultSet resultSet = checkStmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0; // If count > 0, ID exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // CREATE - Add a new airport
    public int createAirport(Airport airport) {
        if (isAirportIdExists(airport.getAirportId())) {
            showAlert("ID Error", "Airport ID " + airport.getAirportId() + " already exists.");
            return 0; // Prevent duplicate entry
        }

        if (airport.getName().isEmpty() || airport.getLocation().isEmpty() || airport.getCode().isEmpty()) {
            showAlert("Input Error", "Fields Name, Localisation, and Code cannot be empty.");
            return 0; // Prevent inserting empty values
        }

        String query = "INSERT INTO airport (airportId, name, location, code) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, airport.getAirportId());
            pstmt.setString(2, airport.getName());
            pstmt.setString(3, airport.getLocation());
            pstmt.setString(4, airport.getCode());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows; // Return number of rows affected
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // UPDATE - Update an airport
    public void updateAirport(Airport airport) {
        if (airport.getName().isEmpty() || airport.getLocation().isEmpty() || airport.getCode().isEmpty()) {
            showAlert("Input Error", "Fields Name, Localisation, and Code cannot be empty.");
            return; // Prevent updating with empty values
        }

        String query = "UPDATE airport SET name = ?, location = ?, code = ? WHERE airportId = ?";
        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {
            pstmt.setString(1, airport.getName());
            pstmt.setString(2, airport.getLocation());
            pstmt.setString(3, airport.getCode());
            pstmt.setInt(4, airport.getAirportId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ - Get an airport by ID
    public Airport getAirportById(int airportId) {
        String query = "SELECT * FROM airport WHERE airportId = ?";
        Airport airport = null;
        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {
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

    // DELETE - Delete an airport by ID
    public void deleteAirport(int airportId) {
        String query = "DELETE FROM airport WHERE airportId = ?";
        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, airportId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Airport> getAirportsByPartialSearch(String searchText) {
        List<Airport> airports = new ArrayList<>();
        String query = "SELECT * FROM airport WHERE CAST(airportId AS CHAR) LIKE ? OR name LIKE ? OR location LIKE ? OR code LIKE ?";

        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(query)) {
            String likeSearch = "%" + searchText + "%"; // Search anywhere in the text
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

    // READ ALL - Get all airports
    public List<Airport> getAllAirports() {
        List<Airport> airports = new ArrayList<>();
        String query = "SELECT * FROM airport";
        try (Statement stmt = databaseConnection.getConnection().createStatement();
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

    // Show alert for errors
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
