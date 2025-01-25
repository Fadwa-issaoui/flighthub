package com.example.flighthub.services;

import com.example.flighthub.models.Role;
import com.example.flighthub.models.User;
import databaseConnection.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {

    public static final DatabaseConnection connection = DatabaseConnection.getInstance();

    // Add a new user
    public void addUser(User user) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("insert into user (userName, password, role) values (?, ?, ?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove an existing user by username
    public void removeUser(String username) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("DELETE FROM user WHERE userName = ?");
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing user's information
    public void updateUser(User user) {
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("UPDATE user SET password = ?, role = ? WHERE userName = ?");
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getRole().toString());
            ps.setString(3, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get user by username
    public User getUser(String username) {
        User user = null;
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT * FROM user WHERE userName = ?");
            ps.setString(1, username);
            var resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("userID");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String roleString = resultSet.getString("role");
                Role role = Role.valueOf(roleString.toUpperCase());

                user = new User();
                user.setUserId(id);
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
        User user = userService.getUser("admin");
        System.out.println(user.getUserId());
    }
}
