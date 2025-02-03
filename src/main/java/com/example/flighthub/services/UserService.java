package com.example.flighthub.services;

import com.example.flighthub.models.Role;
import com.example.flighthub.models.User;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    // Add a new user
    public int addUser(User user) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("INSERT INTO user (userId, username, email, password, role) VALUES (?, ?,?, ?, ?)");
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getUsername());
            var pass = hashPassword(user.getPassword());
            ps.setString(3, user.getMail());
            ps.setString(4, pass);
            ps.setString(5, user.getRole().toString());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    // Remove an existing user by username
    public void removeUser(int userId) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("DELETE FROM user WHERE userId = ?");
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing user's information
    public void updateUser(User user) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("UPDATE user SET username=?, email=?, password = ?, role = ? WHERE userId = ?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getMail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get user by username
    public User getUser(int userID) {
        User user = null;
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userId = ?");
            ps.setInt(1, userID);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("userID");
                String email = resultSet.getString("email");
                String userName = resultSet.getString("userName");
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

    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.removeUser(1);
        User user = new User(1,"test","test@mail.com","testpwd", Role.ADMIN);
        var n = userService.addUser(user);

        if (n > 0) {
            System.out.println("User ID: " + user.getUserId());
        } else {
            System.out.println("User not found.");
        }
    }
}
