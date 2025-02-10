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
    private final String URL = "jdbc:mysql://localhost:3306/flighthub";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {

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
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
