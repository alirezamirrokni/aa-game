package view;

import controller.AppCenter;
import controller.ProfileController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileMenu extends Application implements Initializable {
    @FXML
    private Label menuName;
    private final ProfileController controller = new ProfileController();

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(ProfileMenu.class.getResource("/FXML/ProfileMenu.fxml"));
        if (!AppCenter.getSettings().isBlackAndWhite())
            borderPane.setStyle("-fx-background-color: #79de79");
        stage.setScene(new Scene(borderPane));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuName.setStyle("-fx-font-size: 30; -fx-font-family: '2  Baran';");
        if (!AppCenter.getSettings().isBlackAndWhite())
            menuName.setTextFill(Color.BROWN);
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        App.mainMenu.start(App.stage);
    }

    public void logout(MouseEvent mouseEvent) throws Exception {
        if (controller.isGuest())
            return;
        Alert alert = AppCenter.warning("Logout", "are you sure you want to logout?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            controller.logout();
            alert = AppCenter.information("Success", "logged out successfully!");
            alert.showAndWait();
            App.loginMenu.start(App.stage);
        }
    }

    public void deleteAccount(MouseEvent mouseEvent) throws Exception {
        if (controller.isGuest())
            return;
        Alert alert = AppCenter.warning("Delete Account", "are you sure you want to delete your account?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            controller.deleteAccount();
            alert = AppCenter.information("Success", "account deleted successfully!");
            alert.showAndWait();
            App.loginMenu.start(App.stage);
        }
    }

    public void enterChangeProfileMenu(MouseEvent mouseEvent) throws Exception {
        App.changeProfileMenu.start(App.stage);
    }
}
