package com.example.flighthub.services;

import com.example.flighthub.models.Car;
import com.example.flighthub.models.Location;
import com.example.flighthub.databaseConnection.DatabaseConnection;


import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarRentalService {
    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        String sql = "SELECT * FROM car WHERE is_available = 1"; // Use "car" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        BigDecimal.valueOf(rs.getDouble("rental_price_per_day"))
                );
                car.setAvailable(rs.getInt("is_available") == 1);
                availableCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Debugging: Print the fetched cars
        System.out.println("Fetched available cars: " + availableCars);
        return availableCars;
    }

    public void rentCar(Location location) {
        String sql = "INSERT INTO location (car_id, customer_name, rental_date, rental_days, total_cost) VALUES (?, ?, ?, ?, ?)"; // Use "location" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, location.getCar().getId());
            pstmt.setString(2, location.getCustomerName());
            pstmt.setString(3, location.getRentalDate().toString());
            pstmt.setInt(4, location.getRentalDays());
            pstmt.setDouble(5, location.getTotalCost().doubleValue());
            pstmt.executeUpdate();

            // Update car availability
            updateCarAvailability(location.getCar().getId(), false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnCar(int locationId) {
        String sql = "SELECT car_id FROM location WHERE id = ?"; // Use "location" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, locationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int carId = rs.getInt("car_id");
                updateCarAvailability(carId, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCarAvailability(int carId, boolean isAvailable) {
        String sql = "UPDATE car SET is_available = ? WHERE id = ?"; // Use "car" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, isAvailable ? 1 : 0);
            pstmt.setInt(2, carId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM location"; // Use "location" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Car car = getCarById(rs.getInt("car_id"));
                Location location = new Location(
                        rs.getInt("id"),
                        car,
                        rs.getString("customer_name"),
                        LocalDate.parse(rs.getString("rental_date")),
                        rs.getInt("rental_days")
                );
                locations.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

    private Car getCarById(int carId) {
        String sql = "SELECT * FROM car WHERE id = ?"; // Use "car" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("license_plate"),
                        BigDecimal.valueOf(rs.getDouble("rental_price_per_day"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLocation(int locationId, String customerName, int rentalDays) {
        String sql = "UPDATE location SET customer_name = ?, rental_days = ?, total_cost = ? WHERE id = ?"; // Use "location" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.setInt(2, rentalDays);

            // Recalculate total cost
            Location location = getLocationById(locationId);
            if (location != null) {
                double totalCost = location.getCar().getRentalPricePerDay().doubleValue() * rentalDays;
                pstmt.setDouble(3, totalCost);
                pstmt.setInt(4, locationId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLocation(int locationId) {
        // Step 1: Fetch the car ID associated with the rent being deleted
        String fetchCarIdSql = "SELECT car_id FROM location WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement fetchCarIdStmt = connection.prepareStatement(fetchCarIdSql)) {
            fetchCarIdStmt.setInt(1, locationId);
            ResultSet rs = fetchCarIdStmt.executeQuery();

            if (rs.next()) {
                int carId = rs.getInt("car_id");

                // Step 2: Delete the rent
                String deleteLocationSql = "DELETE FROM location WHERE id = ?";
                try (PreparedStatement deleteLocationStmt = connection.prepareStatement(deleteLocationSql)) {
                    deleteLocationStmt.setInt(1, locationId);
                    deleteLocationStmt.executeUpdate();
                }

                // Step 3: Update the car's availability to 1 (true)
                updateCarAvailability(carId, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Location getLocationById(int locationId) {
        String sql = "SELECT * FROM location WHERE id = ?"; // Use "location" table
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, locationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Car car = getCarById(rs.getInt("car_id"));
                return new Location(
                        rs.getInt("id"),
                        car,
                        rs.getString("customer_name"),
                        LocalDate.parse(rs.getString("rental_date")),
                        rs.getInt("rental_days")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

