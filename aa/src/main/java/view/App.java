package view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

//in cooperation with Ali Najar

public class App extends Application {
    static Stage stage;
    static Stage gameAvatarStage = new Stage();
    static LoginMenu loginMenu = new LoginMenu();
    static RegisterMenu registerMenu = new RegisterMenu();
    static MainMenu mainMenu = new MainMenu();
    static ProfileMenu profileMenu = new ProfileMenu();
    static ChangeProfileMenu changeProfileMenu = new ChangeProfileMenu();
    static ScoreBoard scoreBoard = new ScoreBoard();
    static BorderPane settingsBorderPane;

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        stage.setX(450);
        stage.setY(20);
        setStages();
        User.loadUsers();
        loginMenu.start(stage);
    }

    private void setStages() {
        Image logo = new Image(App.class.getResource("/Images/logo.png").toString());
        stage.getIcons().add(logo);
        gameAvatarStage.getIcons().add(logo);
        stage.setTitle("aa");
        gameAvatarStage.setTitle("game avatars");
    }


}
