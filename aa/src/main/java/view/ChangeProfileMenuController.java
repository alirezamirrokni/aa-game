package view;

import controller.AppCenter;
import controller.ProfileController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ChangeProfileMenuController implements Initializable {
    @FXML
    private ImageView avatar;
    @FXML
    private TextField username;
    @FXML
    private Label usernameLabel;
    @FXML
    private PasswordField currentPass;
    @FXML
    private Label currentPassLabel;
    @FXML
    private Label newPassLabel;
    @FXML
    private Label confirmNewPassLabel;
    @FXML
    private PasswordField newPass;
    @FXML
    private PasswordField confirmNewPass;
    private final ProfileController controller = new ProfileController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAvatar(AppCenter.getCurrentUser().getAvatarAddress());
        username.setText(AppCenter.getCurrentUser().getUsername());
        if (!AppCenter.getSettings().isBlackAndWhite()) {
            usernameLabel.setTextFill(Color.RED);
            currentPassLabel.setTextFill(Color.RED);
            newPassLabel.setTextFill(Color.RED);
            confirmNewPassLabel.setTextFill(Color.RED);
        }
        if (controller.isGuest()) {
            username.setEditable(false);
            currentPass.setEditable(false);
            newPass.setEditable(false);
            confirmNewPass.setEditable(false);
        }
    }

    private void setAvatar(String imageAddress) {
        avatar.setImage(new Image(imageAddress, 100, 100, false, false));
        Circle circle = new Circle(50, 50, 50);
        avatar.setClip(circle);
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        App.profileMenu.start(App.stage);
    }

    public void changeUsername(MouseEvent mouseEvent) throws IOException {
        boolean isGuest = controller.isGuest();
        usernameLabel.setText("");
        String username = this.username.getText();
        if (username.isBlank() && !isGuest)
            usernameLabel.setText("*this field is empty!");
        switch (controller.changeUsername(username)) {
            case EMPTY_FIELD -> {
                break;
            }
            case USERNAME_EXISTS -> {
                usernameLabel.setText("*this username already exists!");
            }
            case INVALID_USERNAME_FORMAT -> {
                usernameLabel.setText("*invalid username format!");
            }
            case SUCCESS -> {
                Alert alert = AppCenter.information("Success", "username changed successfully!");
                alert.showAndWait();
            }
        }
    }

    public void changePassword(MouseEvent mouseEvent) throws IOException {
        boolean isGuest = controller.isGuest();
        currentPassLabel.setText("");
        newPassLabel.setText("");
        confirmNewPassLabel.setText("");
        String currentPass = this.currentPass.getText();
        String newPass = this.newPass.getText();
        String confirmNewPass = this.confirmNewPass.getText();
        if (currentPass.isBlank() && !isGuest)
            currentPassLabel.setText("*this field is empty!");
        if (newPass.isBlank() && !isGuest)
            newPassLabel.setText("*this field is empty!");
        if (confirmNewPass.isBlank() && !isGuest)
            confirmNewPassLabel.setText("*this field is empty!");
        switch (controller.changePassword(currentPass, newPass, confirmNewPass)) {
            case EMPTY_FIELD -> {
                break;
            }
            case INCORRECT_PASSWORD -> {
                currentPassLabel.setText("*incorrect password!");
            }
            case WEAK_PASSWORD -> {
                newPassLabel.setText("*weak password!");
            }
            case DIDNT_CONFIRM -> {
                confirmNewPassLabel.setText("*password didnt confirm!");
            }
            case SUCCESS -> {
                Alert alert = AppCenter.information("Success", "password changed successfully!");
                alert.showAndWait();
            }
        }
    }

    public void browse(MouseEvent mouseEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(App.stage);
        if (file != null) {
            ProfileController.changeAvatar(file.getPath());
            setAvatar(file.getPath());
        }
    }

    public void gameAvatars(MouseEvent mouseEvent) throws IOException {
        BorderPane borderPane = FXMLLoader.load(ChangeProfileMenuController.class.getResource("/FXML/GameAvatars.fxml"));
        App.gameAvatarStage.setScene(new Scene(borderPane));
        App.gameAvatarStage.show();
    }

    public void randomAvatar(MouseEvent mouseEvent) throws IOException {
        int random = new Random().nextInt(1,10);
        String path = ChangeProfileMenuController.class.getResource("/Images/" + random + ".png").toString();
        ProfileController.changeAvatar(path);
        setAvatar(path);
    }
}
