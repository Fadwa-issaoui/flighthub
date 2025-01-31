package com.example.flighthub.services;

import com.example.flighthub.models.User;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginService {

    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // Method to login a user
    public boolean login(String username, String password) throws SQLException, NoSuchAlgorithmException {
        // Query to get user from the database by username or email
        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null;

        // Check if user exists and fetch user details
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            user = new User();
            user.setUserId(resultSet.getInt("userID"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(storedPassword);

        }

        if (user != null) {
            // Compare the entered password with the hashed password stored in the database using SHA-256
            String hashedPassword = hashPassword(password);  // Hash the entered password
            return hashedPassword.equals(user.getPassword());  // Compare with stored hashed password
        }

        return false;  // User not found or password incorrect
    }

    // Method to hash password using SHA-256
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
}
