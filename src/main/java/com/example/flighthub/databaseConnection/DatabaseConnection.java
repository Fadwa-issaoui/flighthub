package com.example.flighthub.databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Singleton instance
    private static DatabaseConnection instance;

    // Database connection object
    private Connection connection;

    // Database credentials
    private final String url = "jdbc:mysql://localhost:3306/flighthub";
    private final String username = "root";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        try {
            // Initialize the connection
            connection = DriverManager.getConnection(url, username,"");
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
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

    // Public method to get the connection
    public Connection getConnection() {
        return connection;
    }
}
