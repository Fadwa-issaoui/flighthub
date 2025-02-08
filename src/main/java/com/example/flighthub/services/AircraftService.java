package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Aircraft;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AircraftService {

    private Connection connection;
    private static final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    public AircraftService() {
        this.connection = databaseConnection.getConnection();
    }

    // CREATE - Add a new aircraft
    public boolean createAircraft(Aircraft aircraft) {
        if (aircraft.getModel() == null || aircraft.getModel().trim().isEmpty()) {
            showAlert("Input Error", "Model field cannot be empty.");
            return false; // Prevent insertion
        }

        String query = "INSERT INTO aircraft (aircraftId, model, capacity, isAvailable) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, aircraft.getAircraftId());
            ps.setString(2, aircraft.getModel());
            ps.setInt(3, aircraft.getCapacity());
            ps.setBoolean(4, aircraft.isAvailable());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating aircraft: " + e.getMessage());
            return false;
        }
    }

    // UPDATE - Update an existing aircraft
    public boolean updateAircraft(Aircraft aircraft) {
        if (aircraft.getModel() == null || aircraft.getModel().trim().isEmpty()) {
            showAlert("Input Error", "Model field cannot be empty.");
            return false; // Prevent update
        }

        String query = "UPDATE aircraft SET model = ?, capacity = ?, isAvailable = ? WHERE aircraftId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, aircraft.getModel());
            ps.setInt(2, aircraft.getCapacity());
            ps.setBoolean(3, aircraft.isAvailable());
            ps.setInt(4, aircraft.getAircraftId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating aircraft: " + e.getMessage());
            return false;
        }
    }


    // Get all aircrafts
    public List<Aircraft> getAllAircrafts() {
        List<Aircraft> aircrafts = new ArrayList<>();
        String query = "SELECT * FROM aircraft";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                Aircraft aircraft = new Aircraft();
                aircraft.setAircraftId(rs.getInt("aircraftId"));
                aircraft.setModel(rs.getString("model"));
                aircraft.setCapacity(rs.getInt("capacity"));
                aircraft.setAvailable(rs.getBoolean("isAvailable"));
                aircrafts.add(aircraft);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving aircraft list: " + e.getMessage());
        }
        return aircrafts;
    }

    // Get an aircraft by ID
    public Aircraft getAircraftById(int aircraftId) {
        String query = "SELECT * FROM aircraft WHERE aircraftId = ?";
        Aircraft aircraft = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, aircraftId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    aircraft = new Aircraft();
                    aircraft.setAircraftId(rs.getInt("aircraftId"));
                    aircraft.setModel(rs.getString("model"));
                    aircraft.setCapacity(rs.getInt("capacity"));
                    aircraft.setAvailable(rs.getBoolean("isAvailable"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving aircraft with ID " + aircraftId + ": " + e.getMessage());
        }
        return aircraft;
    }


    // Delete an aircraft by ID
    public boolean deleteAircraft(int aircraftId) {
        String query = "DELETE FROM aircraft WHERE aircraftId = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, aircraftId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting aircraft: " + e.getMessage());
            return false;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        AircraftService aircraftService = new AircraftService();
        Aircraft aircraft = new Aircraft();

        // Set aircraft details
        aircraft.setAircraftId(2);
        aircraft.setModel("Boeing 737");
        aircraft.setCapacity(150);
        aircraft.setAvailable(true);

        // Create the aircraft in the database
        if (aircraftService.createAircraft(aircraft)) {
            System.out.println("Aircraft created successfully!");
        } else {
            System.out.println("Failed to create aircraft.");
        }

        // Fetch and print all aircrafts
        List<Aircraft> aircrafts = aircraftService.getAllAircrafts();
        for (Aircraft a : aircrafts) {
            System.out.println(a);
        }


    }
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
