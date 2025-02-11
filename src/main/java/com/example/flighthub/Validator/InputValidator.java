package com.example.flighthub.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static com.example.flighthub.services.UserService.connection;

public class InputValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }

    // Validate password length (must be at least 8 characters)
    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    public boolean isIdExists(String tableName, int id) {
        try {
            // Check if the ID exists in the specified table
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT 1 FROM " + tableName + " WHERE id = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next(); // If a result is found, the ID exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
