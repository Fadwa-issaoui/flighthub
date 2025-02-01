package com.example.flighthub.services;

import com.example.flighthub.models.User;
import com.example.flighthub.databaseConnection.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    public boolean login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = new User();

        while (resultSet.next()) {
            user.setPassword(resultSet.getString("password"));
        }

        return user.getEmail().equalsIgnoreCase(username);
    }
}
