package com.example.flighthub.controllers.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import java.io.IOException;

public class CreateAccountController {
    @FXML
    private AnchorPane anchorPaneCreateAccountLeft;

    @FXML
    private AnchorPane anchorPaneCreateAccountRight;

    @FXML
    private BorderPane borderPaneCreateAccount;

    @FXML
    private Button buttonSignInCreateAccount;

    @FXML
    private Button buttonSignUpCreateAccount;

    @FXML
    private ImageView iconCreateAccount;

    @FXML
    private ImageView iconMailCreateAccount;

    @FXML
    private ImageView iconPasswordCreateAccount;

    @FXML
    private ImageView iconUserCreateAccount;

    @FXML
    private Label labelCreateAccount;

    @FXML
    private Label labelWelcomeBack;

    @FXML
    private TextField textFieldEmailCreateAccount;

    @FXML
    private TextField textFieldPasswordCreateAccount;

    @FXML
    private TextField textFieldUserCreateAccount;

    @FXML
    private Text textWelcomeBack;

    @FXML
    void changerVersLogin(ActionEvent event) {

    }
    @FXML
    private void initialize() {
        buttonSignInCreateAccount.setOnAction(event -> switchToSignUp());
    }

    private void switchToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonSignInCreateAccount.getScene().getWindow(); // Get current window
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openSignUp() throws IOException {
        Stage stage = (Stage) buttonSignInCreateAccount.getScene().getWindow(); // Get current stage
        Parent root = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/Login.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
