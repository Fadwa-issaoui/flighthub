package com.example.flighthub.services;

import com.example.flighthub.models.Airport;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirportService {

    public static final DatabaseConnection connection = DatabaseConnection.getInstance();

    // Add a new airport
    public void addAirport(Airport airport) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("INSERT INTO airport (airportId,location) VALUES (?, ?)");
            ps.setInt(1, airport.getAirportId());
            ps.setString(2, airport.getLocation());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove an existing airport by airportId
    public void removeAirport(int airportId) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("DELETE FROM airport WHERE airportId = ?");
            ps.setInt(1, airportId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing airport's information
    public void updateAirport(Airport airport) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("UPDATE airport SET location = ? WHERE airportId = ?");
            ps.setString(1, airport.getLocation());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get airport by airportId
    public Airport getAirport(int airportId) {
        Airport airport = null;
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT * FROM airport WHERE airportId = ?");
            ps.setInt(1, airportId);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String location = resultSet.getString("location");

                airport = new Airport();
                airport.setAirportId(airportId);
                airport.setLocation(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airport;
    }

    public static void main(String[] args) {
        AirportService airportService = new AirportService();

        // Example of adding a new airport
        Airport newAirport = new Airport();
        newAirport.setAirportId(1);
        newAirport.setLocation("New York, USA");
        airportService.addAirport(newAirport);

        // Example of fetching an airport by ID
        Airport airport = airportService.getAirport(1);
        System.out.println( " located in " + airport.getLocation());
    }


    public List<Airport> getAirportsByPartialId(String searchText) {
        List<Airport> airports = new ArrayList<>();

        try {
            PreparedStatement ps = connection.getConnection().prepareStatement(
                    "SELECT * FROM airport WHERE CAST(airportId AS CHAR) LIKE ? OR location LIKE ?"
            );
            ps.setString(1, searchText + "%");
            ps.setString(2, searchText + "%");

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Airport airport = new Airport();
                airport.setAirportId(resultSet.getInt("airportId"));
                airport.setLocation(resultSet.getString("location"));
                airports.add(airport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return airports;
    }
}
