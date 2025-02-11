package com.example.flighthub.services;

import com.example.flighthub.models.Passenger;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PassengerService {
    private static DatabaseConnection databaseConnection=DatabaseConnection.getInstance();

    public PassengerService() {}

    public int addPassenger(Passenger passenger) {
        try {
            PreparedStatement ps = databaseConnection.getConnection().prepareStatement(
                    "INSERT INTO passenger (`firstName`, `lastName`, `dateOfBirth`, `passportNumber`, `nationality`) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS // This tells the database to return the generated keys
            );

// Setting values
            ps.setString(1, passenger.getFirstName());
            ps.setString(2, passenger.getLastName());
            ps.setString(3, passenger.getDateOfBirth());
            ps.setString(4, passenger.getPassportNumber());
            ps.setString(5, passenger.getNationality());

// Execute the insert
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retrieve the auto-incremented ID

                }
                generatedKeys.close();
            }

// Close resources
            ps.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

}
