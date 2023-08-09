package controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Game;
import model.Settings;
import model.User;
import view.GameScene;

import java.net.PortUnreachableException;
import java.util.Random;

public class AppCenter {
    private static User currentUser;
    private static Game currentGame;
    public static int gameWidth = 450;
    public static int gameHeight = 750;
    public static double ox;
    public static double oy;
    public static int ballRadius = 8;

    public static void setCurrentUser(User currentUser) {
        AppCenter.currentUser = currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Settings getSettings() {
        return currentUser.getSettings();
    }

    public static Alert warning(String headText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        return alert;
    }

    public static Alert information(String headText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        return alert;
    }

    public static void setCurrentGame(Game currentGame) {
        AppCenter.currentGame = currentGame;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static int getRandomDuration() {
        Random random = new Random();
        return random.nextInt(4,8) * 1000;
    }

    public static VBox createVbox(int spacing) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(spacing);
        return vBox;
    }

    public static HBox createHbox(int spacing) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(spacing);
        return hBox;
    }

    public static Stage createStage(String title, Scene scene) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(new Image(GameScene.class.getResource("/Images/logo.png").toString()));
        stage.setScene(scene);
        return stage;
    }

    public static Button createButton(String text) {
        Button button = new Button();
        button.setText(text);
        return button;
    }
}
