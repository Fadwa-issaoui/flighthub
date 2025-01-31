package com.example.flighthub.services;

import com.example.flighthub.models.Passenger;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PassengerService {
    private static DatabaseConnection databaseConnection=DatabaseConnection.getInstance();

    public PassengerService() {}

    public int addPassenger(Passenger passenger) throws SQLException {
        PreparedStatement ps =databaseConnection.getConnection().prepareStatement("");
        ps.setString(1, passenger.getFirstName());
        ps.setString(2, passenger.getLastName());

        return ps.executeUpdate();
    }

}
