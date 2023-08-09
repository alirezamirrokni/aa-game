package view;

import controller.AppCenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RegisterMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(RegisterMenu.class.getResource("/FXML/RegisterMenu.fxml"));
        borderPane.setStyle("-fx-background-color: yellow");
        stage.setScene(new Scene(borderPane));
        stage.show();
    }
}
