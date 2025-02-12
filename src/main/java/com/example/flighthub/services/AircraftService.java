package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Aircraft;
import com.example.flighthub.models.Flight;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AircraftService {

    private Connection connection;
    private static final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    public AircraftService() {
        try {
            this.connection = databaseConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // NEW METHOD: Get available aircraft for a given departure airport
    public List<Aircraft> getAvailableAircraftForDepartureAirport(int departureAirportId) {
        //the same code
        List<Aircraft> availableAircraft = new ArrayList<>();

        // Get aircraft that are NOT assigned to any flight (flightId = 0)
        String unassignedQuery = "SELECT * FROM aircraft WHERE flightId = 0";
        try (PreparedStatement psUnassigned = connection.prepareStatement(unassignedQuery);
             ResultSet rsUnassigned = psUnassigned.executeQuery()) {

            while (rsUnassigned.next()) {
                Aircraft aircraft = getAircraftById(rsUnassigned.getInt("aircraftId")); // Get full aircraft details
                if (aircraft != null) {
                    availableAircraft.add(aircraft);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving unassigned aircraft: " + e.getMessage());
        }

        // Get aircraft assigned to flights, and check arrival airport and time
        String assignedQuery = "SELECT * FROM aircraft WHERE flightId != 0"; // Get aircraft assigned to flights
        try (PreparedStatement psAssigned = connection.prepareStatement(assignedQuery);
             ResultSet rsAssigned = psAssigned.executeQuery()) {

            while (rsAssigned.next()) {
                int aircraftId = rsAssigned.getInt("aircraftId");
                int flightId = rsAssigned.getInt("flightId");

                // Get the associated Flight
                FlightService flightService = new FlightService();
                Flight flight = flightService.getFlightById(flightId);

                if (flight != null && flight.getArrivalAirportId() == departureAirportId) {
                    // Check if the flight has ended
                    LocalDateTime arrivalTime = LocalDateTime.parse(flight.getArrivalTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    if (arrivalTime.isBefore(LocalDateTime.now())) {
                        Aircraft aircraft = getAircraftById(aircraftId);
                        if (aircraft != null) {
                            availableAircraft.add(aircraft);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving assigned aircraft: " + e.getMessage());
        }

        return availableAircraft;
    }
    // CREATE - Add a new aircraft + Initialize flightID
    public boolean createAircraft(Aircraft aircraft) {
        if (aircraft.getModel() == null || aircraft.getModel().trim().isEmpty()) {
            showAlert("Input Error", "Model field cannot be empty.");
            return false;
        }

        String query = "INSERT INTO aircraft (aircraftId, model, capacity, isAvailable, flightId) VALUES (?, ?, ?, ?, 0)"; // Set flightId to 0
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, aircraft.getAircraftId());
            ps.setString(2, aircraft.getModel());
            ps.setInt(3, aircraft.getCapacity());
            ps.setBoolean(4, aircraft.isAvailable());
            // flightId is set to 0 in the query itself

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating aircraft: " + e.getMessage());
            return false;
        }
    }


    // UPDATE - Update an existing aircraft
    public boolean updateAircraft(Aircraft aircraft) {
        //the same code
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
        //the same code
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
                aircraft.setFlightId(rs.getInt("flightId")); // Load flightId
                aircrafts.add(aircraft);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving aircraft list: " + e.getMessage());
        }
        return aircrafts;
    }

    // Get an aircraft by ID
    public Aircraft getAircraftById(int aircraftId) {
        //the same code
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
                    aircraft.setFlightId(rs.getInt("flightId")); // Load flightId
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving aircraft with ID " + aircraftId + ": " + e.getMessage());
        }
        return aircraft;
    }


    // Delete an aircraft by ID
    public boolean deleteAircraft(int aircraftId) {
        //the same code
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

    //  Update the flightId of an aircraft
    public boolean updateAircraftFlightId(int aircraftId, int flightId) {
        //the same code
        String query = "UPDATE aircraft SET flightId = ? WHERE aircraftId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, flightId);
            ps.setInt(2, aircraftId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating aircraft flightId: " + e.getMessage());
            return false;
        }
    }

    // Reset the flightId of an aircraft (set to 0, indicating no assigned flight)
    public boolean resetAircraftFlightId(int aircraftId) {
        //the same code
        String query = "UPDATE aircraft SET flightId = 0 WHERE aircraftId = ?"; // Setting to 0
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, aircraftId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error resetting aircraft flightId: " + e.getMessage());
            return false;
        }
    }


    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //In AircraftService
    public int getAvailableAircraftCount() {
        // This is the same logic as getAvailableAircraftForDepartureAirport,
        // but we just count instead of returning the list.  This is more efficient.
        int availableCount = 0;
        String unassignedQuery = "SELECT COUNT(*) FROM aircraft WHERE flightId = 0";
        try (PreparedStatement psUnassigned = connection.prepareStatement(unassignedQuery);
             ResultSet rsUnassigned = psUnassigned.executeQuery()) {

            if (rsUnassigned.next()) {
                availableCount += rsUnassigned.getInt(1); // Get the count directly
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving unassigned aircraft count: " + e.getMessage());
            return 0; // Or throw, depending on error handling.
        }

        // Get aircraft assigned to *completed* flights.
        String assignedQuery = "SELECT a.aircraftId, f.arrivalTime FROM aircraft a JOIN flight f ON a.flightId = f.flightId WHERE f.flightId != 0";
        try (PreparedStatement psAssigned = connection.prepareStatement(assignedQuery);
             ResultSet rsAssigned = psAssigned.executeQuery()) {

            while (rsAssigned.next()) {
                String arrivalTimeString = rsAssigned.getString("arrivalTime"); // Get as String
                LocalDateTime arrivalTime = LocalDateTime.parse(arrivalTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (arrivalTime.isBefore(LocalDateTime.now())) {
                    availableCount++;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving assigned aircraft count: " + e.getMessage());
            return 0; //Or throw

        } catch (DateTimeParseException e) {
            System.err.println("Error parsing arrival time: " + e.getMessage()); // Handle parsing errors!
            return 0;
        }

        return availableCount;
    }
}