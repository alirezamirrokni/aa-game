package view;

import controller.AppCenter;
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
import model.Game;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenu extends Application implements Initializable {
    @FXML
    private Label menuName;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(MainMenu.class.getResource("/FXML/MainMenu.fxml"));
        if (!AppCenter.getSettings().isBlackAndWhite())
            borderPane.setStyle("-fx-background-color: pink");
        stage.setScene(new Scene(borderPane));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuName.setStyle("-fx-font-size: 30; -fx-font-family: 'Century Gothic';");
        if (!AppCenter.getSettings().isBlackAndWhite())
            menuName.setTextFill(Color.PURPLE);
    }

    public void exit(MouseEvent mouseEvent) {
        Alert alert = AppCenter.warning("Exit", "are you sure you want to exit?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES))
            System.exit(0);
    }

    public void enterProfileMenu(MouseEvent mouseEvent) throws Exception {
        App.profileMenu.start(App.stage);
    }

    public void enterSettings(MouseEvent mouseEvent) throws Exception {
        BorderPane borderPane = FXMLLoader.load(MainMenu.class.getResource("/FXML/Settings.fxml"));
        if (!AppCenter.getSettings().isBlackAndWhite())
            borderPane.setStyle("-fx-background-color: #6e6ee8");
        App.settingsBorderPane = borderPane;
        App.stage.setScene(new Scene(borderPane));
        App.stage.show();
    }

    public void startNewGame(MouseEvent mouseEvent) throws Exception {
        AppCenter.ox = AppCenter.gameWidth/2;
        AppCenter.oy = 260;
        AppCenter.setCurrentGame(new Game(AppCenter.getSettings().clone()));
        new GameScene().start(App.stage);
    }

    public void resumeLastGame(MouseEvent mouseEvent) throws Exception {
        AppCenter.ox = AppCenter.gameWidth/2;
        AppCenter.oy = 260;
        if (AppCenter.getCurrentUser().getLastGame() != null) {
            AppCenter.setCurrentGame(AppCenter.getCurrentUser().getLastGame());
            new GameScene().start(App.stage);
        }
    }

    public void enterScoreBoard(MouseEvent mouseEvent) throws Exception {
        App.scoreBoard.start(App.stage);
    }

    public void startTwoPlayerGame(MouseEvent mouseEvent) throws Exception {
        AppCenter.ox = AppCenter.gameWidth/2;
        AppCenter.oy = 340;
        AppCenter.setCurrentGame(new Game(AppCenter.getSettings().clone()));
        new TwoPlayerGameScene().start(App.stage);
    }
}
