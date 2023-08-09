package view;

import controller.AppCenter;
import controller.LoginAndRegisterController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginMenuController implements Initializable {
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
    private final LoginAndRegisterController controller = new LoginAndRegisterController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuName.setStyle("-fx-font-size: 30; -fx-font-family: 'Century Gothic';");
        menuName.setTextFill(Color.BLUE);
        usernameLabel.setTextFill(Color.RED);
        passwordLabel.setTextFill(Color.RED);
    }

    public void login(MouseEvent mouseEvent) throws Exception {
        usernameLabel.setText("");
        passwordLabel.setText("");
        String username = this.username.getText();
        String password = this.password.getText();
        if (username.isBlank())
            usernameLabel.setText("*this field is empty!");
        if (password.isBlank())
            passwordLabel.setText("*this field is empty!");
        switch (controller.login(username, password)) {
            case EMPTY_FIELD -> {
                break;
            }
            case USERNAME_EXISTS -> {
                usernameLabel.setText("*this username already exists!");
            }
            case USERNAME_NOT_FOUND -> {
                usernameLabel.setText("*username not found!");
            }
            case INCORRECT_PASSWORD -> {
                passwordLabel.setText("*incorrect password!");
            }
            case SUCCESS -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Success");
                alert.setContentText("logged in successfully!");
                alert.showAndWait();
                App.mainMenu.start(App.stage);
            }
        }

    }

    public void register(MouseEvent mouseEvent) throws Exception {
        App.registerMenu.start(App.stage);
    }

    public void exit(MouseEvent mouseEvent) {
        Alert alert = AppCenter.warning("Exit", "are you sure you want to exit?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES))
            System.exit(0);
    }

    public void enterAsGuest(MouseEvent mouseEvent) throws Exception {
        controller.eneterAsGuest();
        Alert alert = AppCenter.information("Success", "you entered as a guest!");
        alert.showAndWait();
        App.mainMenu.start(App.stage);
    }
}
