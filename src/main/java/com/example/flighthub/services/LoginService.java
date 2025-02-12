package com.example.flighthub.services;

import com.example.flighthub.models.User;
import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginService {
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    // Method to login a user
    public boolean login(String username, String password) throws SQLException, NoSuchAlgorithmException {
        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null;

        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            String roleString = resultSet.getString("role");
            Role role = Role.valueOf(roleString.toUpperCase());

            user = new User();
            user.setUserId(resultSet.getInt("userId"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(storedPassword);
            user.setRole(role);
        }

        if (user != null) {
            String hashedPassword = hashPassword(password);
            return hashedPassword.equals(user.getPassword());
        }

        return false;
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

    public static void main(String[] args) {
        // Test credentials
        String username = "admin";
        String password = "password123";  // This should be hashed in the real world

        LoginService loginService = new LoginService();

        try {
            boolean loginSuccess = loginService.login(username, password);

            if (loginSuccess) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed: Invalid username or password.");
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Error during login process.");
        }
    }
}
