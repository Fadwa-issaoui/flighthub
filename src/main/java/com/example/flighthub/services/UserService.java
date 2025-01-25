package com.example.flighthub.services;

import com.example.flighthub.models.Role;
import com.example.flighthub.models.User;
import databaseConnection.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {

    public static final DatabaseConnection connection = DatabaseConnection.getInstance();

    public void addUser(User user) throws SQLException {
        PreparedStatement ps = connection.getConnection().prepareStatement( "insert into user (userName,password,role) values (?,?,?)");
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getRole().toString());
        ps.executeUpdate();
    }

    public static void main(String[] args) throws SQLException {
        UserService userService = new UserService();
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setRole(Role.ADMIN);
        userService.addUser(user);
    }

}
