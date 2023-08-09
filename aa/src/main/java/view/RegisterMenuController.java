package view;

import controller.AppCenter;
import controller.LoginAndRegisterController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterMenuController implements Initializable {
    @FXML
    private Label menuName;
    @FXML
    private TextField username;
    @FXML
    private Label usernameLabel;
    @FXML
    private PasswordField password;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label confirmPasswordLabel;
    private final LoginAndRegisterController controller = new LoginAndRegisterController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuName.setStyle("-fx-font-size: 30; -fx-font-family: 'Century Gothic';");
        menuName.setTextFill(Color.BLUE);
        usernameLabel.setTextFill(Color.RED);
        passwordLabel.setTextFill(Color.RED);
        confirmPasswordLabel.setTextFill(Color.RED);
    }

    public void register(MouseEvent mouseEvent) throws Exception {
        usernameLabel.setText("");
        passwordLabel.setText("");
        confirmPasswordLabel.setText("");
        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.confirmPassword.getText();
        if (username.isBlank())
            usernameLabel.setText("*this field is empty!");
        if (password.isBlank())
            passwordLabel.setText("*this field is empty!");
        if (confirmPassword.isBlank())
            confirmPasswordLabel.setText("*this field is empty!");
        switch (controller.register(username, password, confirmPassword)) {
            case EMPTY_FIELD -> {
                break;
            }
            case INVALID_USERNAME_FORMAT -> {
                usernameLabel.setText("*invalid username format!");
            }
            case WEAK_PASSWORD -> {
                passwordLabel.setText("*weak password!");
            }
            case DIDNT_CONFIRM -> {
                confirmPasswordLabel.setText("*password didnt confirm!");
            }
            case SUCCESS -> {
                Alert alert = AppCenter.information("Success", "registered successfully!");
                alert.showAndWait();
                App.loginMenu.start(App.stage);
            }
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        App.loginMenu.start(App.stage);
    }
}
