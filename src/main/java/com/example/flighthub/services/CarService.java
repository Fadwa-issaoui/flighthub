package com.example.flighthub.services;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Car;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CarService {
    // This will hold the cars fetched from the database
    private ObservableList<Car> carList = FXCollections.observableArrayList();

    // Add car to the database
    public void addCar(Car car) {
        // Validate and format the price
        if (car.getRentalPricePerDay().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Invalid price. The price must be a positive value.");
            return; // Exit if the price is invalid
        }

        String query = "INSERT INTO car (brand, model, license_plate, rental_price_per_day, is_available) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getModel());
            preparedStatement.setString(3, car.getLicensePlate());
            preparedStatement.setBigDecimal(4, car.getRentalPricePerDay()); // Use BigDecimal here
            preparedStatement.setBoolean(5, car.isAvailable());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    car = new Car(generatedKeys.getInt(1), car.getBrand(), car.getModel(), car.getLicensePlate(), car.getRentalPricePerDay());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean licensePlateExists(String licensePlate) {
        ObservableList<Car> allCars = getAllCars();
        for (Car car : allCars) {
            if (car.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                return true;
            }
        }
        return false;
    }


    // Update car in the database
    public void updateCar(Car car) {
        // Validate and format the price
        if (car.getRentalPricePerDay().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Invalid price. The price must be a positive value.");
            return; // Exit if the price is invalid
        }

        String query = "UPDATE car SET brand=?, model=?, license_plate=?, rental_price_per_day=?, is_available=? WHERE id=?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getModel());
            preparedStatement.setString(3, car.getLicensePlate());
            preparedStatement.setBigDecimal(4, car.getRentalPricePerDay()); // Use BigDecimal here
            preparedStatement.setBoolean(5, car.isAvailable());
            preparedStatement.setInt(6, car.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Car updated successfully!");
            } else {
                System.out.println("❌ No car found with ID: " + car.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all cars from the database
    public ObservableList<Car> getAllCars() {
        ObservableList<Car> cars = FXCollections.observableArrayList();
        String query = "SELECT * FROM car";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                String licensePlate = resultSet.getString("license_plate");
                BigDecimal rentalPricePerDay = resultSet.getBigDecimal("rental_price_per_day"); // Get BigDecimal
                boolean isAvailable = resultSet.getBoolean("is_available");

                Car car = new Car(id, brand, model, licensePlate, rentalPricePerDay);
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    // Delete car from the database
    public void deleteCar(int carId) {
        String query = "DELETE FROM car WHERE id=?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, carId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Car deleted successfully!");
            } else {
                System.out.println("❌ No car found with ID: " + carId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}