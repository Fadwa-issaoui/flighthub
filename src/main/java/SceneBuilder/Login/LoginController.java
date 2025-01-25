package SceneBuilder.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LoginController {



    @FXML
    private AnchorPane anchorPaneLoginLeft;

    @FXML
    private AnchorPane anchorPaneLoginRight;

    @FXML
    private BorderPane borderPaneLogin;

    @FXML
    private Button buttonSignInLogin;

    @FXML
    private Button buttonSignUpLogin;

    @FXML
    private HBox hBoxEmailLogin;

    @FXML
    private HBox hBoxPasswordLogin;

    @FXML
    private ImageView iconLogin;

    @FXML
    private ImageView iconMailLogin;

    @FXML
    private ImageView iconPasswordLogin;

    @FXML
    private Label labelHelloLogin;

    @FXML
    private Label labelLogin;

    @FXML
    private TextField textFieldEmailLogin;

    @FXML
    private TextField textFieldPasswordLogin;

    @FXML
    private Text textHelloLogin;

    @FXML
    void changerVersCreateAccount(ActionEvent event) {

    }


    /*@FXML
    private Button buttonSignInLogin;*/
    @FXML
    private void initialize() {
        buttonSignUpLogin.setOnAction(event -> switchToSignUp());
    }

    private void switchToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/CreateAccount.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonSignUpLogin.getScene().getWindow(); // Get current window
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openSignUp() throws IOException {
        Stage stage = (Stage) buttonSignUpLogin.getScene().getWindow(); // Get current stage
        Parent root = FXMLLoader.load(getClass().getResource("/FlightHub/SceneBuilder/CreateAccount.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
