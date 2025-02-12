package com.example.flighthub.services;

import com.example.flighthub.models.Role;
import com.example.flighthub.models.User;
import com.example.flighthub.databaseConnection.DatabaseConnection;
import javafx.scene.control.Alert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserService {

    public static final DatabaseConnection connection = DatabaseConnection.getInstance();

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    // Validate password length (must be at least 8 characters)
    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    // Add a new user with a specified ID (no auto-increment)
    public int addUser(User user) {
        try {
            // Check password length
            if (!isValidPassword(user.getPassword())) {
                showAlert("Password Error", "Password must be at least 8 characters long.");
                return 0;  // Invalid password length
            }

            // Check if the userId already exists
            PreparedStatement checkStmt = connection.getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM user WHERE userId = ?");
            checkStmt.setInt(1, user.getUserId());
            ResultSet resultSet = checkStmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                return 0;  // User ID already exists
            }

            // Insert user with the specified ID
            PreparedStatement ps = connection.getConnection().prepareStatement(
                    "INSERT INTO user (userId, username, email, password, role) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, user.getUserId()); // Use the user-entered ID
            ps.setString(2, user.getUsername());
            var pass = hashPassword(user.getPassword());
            ps.setString(3, user.getMail());
            ps.setString(4, pass);
            ps.setString(5, user.getRole().toString());

            int affectedRows = ps.executeUpdate();

            return affectedRows;
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Remove an existing user by userId
    public void removeUser(int userId) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("DELETE FROM user WHERE userId = ?");
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Update an existing user's information
    public void updateUser(User user) {
        try {
            // Check password length before updating
            if (!isValidPassword(user.getPassword())) {
                showAlert("Password Error", "Password must be at least 8 characters long.");
                return;  // Invalid password length, stop the update
            }

            PreparedStatement ps = connection.getConnection().prepareStatement(
                    "UPDATE user SET username = ?, email = ?, password = ?, role = ? WHERE userId = ?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getMail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().toString());
            ps.setInt(5, user.getUserId());  // Set userId for update
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get user by userId
    public User getUser(int userID) {
        User user = null;
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userId = ?");
            ps.setInt(1, userID);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("userId");
                String email = resultSet.getString("email");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String roleString = resultSet.getString("role");
                Role role = Role.valueOf(roleString.toUpperCase());

                user = new User();
                user.setUserId(id);
                user.setMail(email);
                user.setUsername(userName);
                user.setPassword(password);
                user.setRole(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getUserRole(String username){
        final String sql = "SELECT role FROM user WHERE username = ?";
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            String s = resultSet.getString("role");
            return s;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Role.ADMIN.toString();

    }

    public static void main(String[] args) {
        UserService userService = new UserService();

        User user = new User(0, "test12", "test12@mail.com", "testtest", Role.GESTIONNAIRE); // Password too short (less than 8 characters)
        user.setUsername("test");
        int result = userService.addUser(user);

        if (result > 0) {
            System.out.println("User added successfully. User ID: " + user.getUserId());
        } else {
            System.out.println("Failed to add user. Password must be at least 8 characters long.");
        }

    }
}
