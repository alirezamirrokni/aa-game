package view;

import controller.AppCenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class LoginMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(LoginMenu.class.getResource("/FXML/LoginMenu.fxml"));
        borderPane.setStyle("-fx-background-color: yellow");
        stage.setScene(new Scene(borderPane));
        stage.show();
    }
}
