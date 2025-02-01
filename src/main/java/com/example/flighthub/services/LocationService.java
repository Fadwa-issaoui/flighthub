package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Car;
import com.example.flighthub.models.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationService {
    private List<Location> locations = new ArrayList<>();

    DatabaseConnection databaseConnection =DatabaseConnection.getInstance();

    public LocationService()  {
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public void removeLocation(int id) {
        locations.removeIf(loc -> loc.getId() == id);
    }

    public void updateLocation(int id, String newCustomerName, int newRentalDays) {
        for (Location loc : locations) {
            if (loc.getId() == id) {
                loc.returnCar();  // You may want to do something here based on your logic for returning a car
                loc.setCustomerName(newCustomerName); // Update the customer name
                loc.setRentalDays(newRentalDays); // Update the rental days
                // You can add more logic if you need to update other fields as well
            }
        }
    }

    public List<Location> getAllLocations() throws SQLException {
        List<Location> locations = new ArrayList<>();
        PreparedStatement ps  = databaseConnection.getConnection().prepareStatement("SELECT * FROM location");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String customerName = rs.getString("customer_name");
            int rentalDays = rs.getInt("rental_days");
            Location location = new Location();
            location.setCustomerName(customerName);
            location.setRentalDays(rentalDays);
            locations.add(location);
        }
        return locations;
    }
}
