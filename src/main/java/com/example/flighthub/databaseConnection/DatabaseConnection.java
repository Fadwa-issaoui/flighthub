package com.example.flighthub.databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Singleton instance
    private static DatabaseConnection instance;

    // Database credentials

    private final String url = "jdbc:mysql://localhost:3306/flighthub";
    private final String username = "root";

    private final String password = "";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        // Constructor is now empty, connection will be established when requested
    }

    // Public method to get the singleton instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }


    // Public method to get a new connection (must be closed manually)
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}