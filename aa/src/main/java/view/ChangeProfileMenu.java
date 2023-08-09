package view;

import controller.AppCenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChangeProfileMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(ChangeProfileMenu.class.getResource("/FXML/ChangeProfileMenu.fxml"));
        if (!AppCenter.getSettings().isBlackAndWhite())
            borderPane.setStyle("-fx-background-color: #79de79");
        stage.setScene(new Scene(borderPane));
        stage.show();
    }
}
