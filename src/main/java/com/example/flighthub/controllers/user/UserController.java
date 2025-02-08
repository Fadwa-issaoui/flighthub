package com.example.flighthub.controllers.user;

import com.example.flighthub.Validator.InputValidator;
import com.example.flighthub.models.Role;
import com.example.flighthub.models.User;
import com.example.flighthub.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserController {

    @FXML
    private TextField textfieldId;
    @FXML
    private TextField textfieldUsername;
    @FXML
    private TextField textfieldEmail;
    @FXML
    private TextField textfieldPassword;
    @FXML
    private TextField textfieldRole;
    @FXML
    private TableView<User> tableviewUser;
    @FXML
    private TableColumn<User, Integer> columnId;
    @FXML
    private TableColumn<User, String> columnUsername;
    @FXML
    private TableColumn<User, String> columnEmail;
    @FXML
    private TableColumn<User, String> columnRole;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonModify;
    @FXML
    private Button buttonDelete;
    @FXML
    private TextField textfieldSearch;
    @FXML
    private Button buttonSearch;

    private UserService userService = new UserService();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("userId"));
        columnUsername.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("username"));
        columnEmail.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("mail"));
        columnRole.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("role"));

        tableviewUser.setItems(userList);
        loadUserData();
    }

    private void loadUserData() {
        userList.clear();
        for (int i = 1; i <= 100; i++) { // Simulating fetching multiple users
            User user = userService.getUser(i);
            if (user != null) {
                userList.add(user);
            }
        }
    }

    @FXML
    public void handleAddUser() {
        try {
            int id = Integer.parseInt(textfieldId.getText());

            // Check if the user ID already exists in the database
            User existingUser = userService.getUser(id);
            if (existingUser != null) {
                showAlert("Input Error", "User ID already exists.");
                return; // Stop execution if the user ID already exists
            }

            String username = textfieldUsername.getText();
            String email = textfieldEmail.getText();
            String password = textfieldPassword.getText();
            Role role = Role.valueOf(textfieldRole.getText().toUpperCase());

            if (username.isEmpty()){
                showAlert("Input Error", "Username cannot be empty.");
                return;
            }
            if (!InputValidator.isValidEmail(email)) {
                showAlert("Input Error", "Invalid email format.");
                return; // Stop execution if email is invalid
            }

            // Use the same ID as entered, don't change it
            User user = new User(id, username, email, password, role);
            if (userService.addUser(user) > 0) {
                userList.add(user);
                tableviewUser.refresh();
                clearFields(); // Clear fields after adding the user
            } else {
                showAlert("Error", "Failed to add user.");
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid user ID.");
        } catch (IllegalArgumentException e) {
            showAlert("Input Error", "Invalid role. Use ADMIN, AGENT, etc.");
        }
    }

    @FXML
    public void handleModifyUser() {
        User selectedUser = tableviewUser.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                String email = textfieldEmail.getText();

                if (textfieldUsername.getText().isEmpty()){
                    showAlert("Input Error", "Username cannot be empty.");
                    return;
                }
                if (!InputValidator.isValidEmail(email)) {
                    showAlert("Input Error", "Invalid email format.");
                    return; // Stop execution if email is invalid
                }

                // Update selected user fields with the new data
                selectedUser.setUsername(textfieldUsername.getText());
                selectedUser.setMail(email);
                selectedUser.setPassword(textfieldPassword.getText());
                selectedUser.setRole(Role.valueOf(textfieldRole.getText().toUpperCase()));

                // Update the user in the database
                userService.updateUser(selectedUser);
                tableviewUser.refresh();
                clearFields();
            } catch (IllegalArgumentException e) {
                showAlert("Input Error", "Invalid role. Use ADMIN, AGENT, etc.");
            }
        } else {
            showAlert("Selection Error", "Please select a user to modify.");
        }
    }

    @FXML
    public void handleDeleteUser() {
        User selectedUser = tableviewUser.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userService.removeUser(selectedUser.getUserId());
            userList.remove(selectedUser);
        } else {
            showAlert("Selection Error", "Please select a user to delete.");
        }
    }

    @FXML
    public void handleSearchUser() {
        try {
            int userId = Integer.parseInt(textfieldSearch.getText());
            User user = userService.getUser(userId);
            if (user != null) {
                userList.setAll(user);
            } else {
                showAlert("Search Error", "No user found with this ID.");
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid user ID.");
        }
    }

    private void clearFields() {
        textfieldId.clear();
        textfieldUsername.clear();
        textfieldEmail.clear();
        textfieldPassword.clear();
        textfieldRole.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
