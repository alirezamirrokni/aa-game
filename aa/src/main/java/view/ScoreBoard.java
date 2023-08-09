package view;

import controller.AppCenter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScoreBoard extends Application {

    private static VBox vBox;
    private static Label name;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(MainMenu.class.getResource("/FXML/ScoreBoard.fxml"));
        name = new Label("ScoreBoard");
        name.setStyle("-fx-font-size: 30; -fx-font-family: 'Century Gothic'");
        vBox = AppCenter.createVbox(30);
        vBox.getChildren().addAll(name, new HBox(), options());
        borderPane.setCenter(vBox);
        stage.setScene(new Scene(borderPane));
        stage.show();
        sortUsers("Easy");
    }


    private void sortUsers(String difficulty) {
        ArrayList<User> users = User.getSortedUsersByDifficulty(difficulty);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(1);
        VBox rank = AppCenter.createVbox(10);
        VBox username = AppCenter.createVbox(10);
        VBox score = AppCenter.createVbox(10);
        VBox time = AppCenter.createVbox(10);
        VBox diff = AppCenter.createVbox(10);
        rank.getChildren().add(createLabel("Rank", 100, 15));
        username.getChildren().add(createLabel("User", 100, 15));
        score.getChildren().add(createLabel("Score", 100, 15));
        time.getChildren().add(createLabel("Time", 100, 15));
        diff.getChildren().add(createLabel("Difficulty", 100, 15));
        int counter = 1;
        for (User user : users) {
            if (counter > 10)
                break;
            setInformation(user, rank, username, score, time, diff, counter, difficulty);
            counter++;
        }
        hBox.getChildren().addAll(rank, username, score, time, diff);
        vBox.getChildren().set(1, hBox);
    }


    private void setInformation(User user, VBox rank, VBox username, VBox score, VBox time, VBox diff, int counter, String difficulty) {
        Label rankLabel = createLabel(String.valueOf(counter), 100, 30);
        Label usernameLabel = createLabel(user.getUsername(), 70, 30);
        HBox userInfo = AppCenter.createHbox(3);
        Circle circle = new Circle(15);
        circle.setFill(new ImagePattern(new Image(user.getAvatarAddress())));
        userInfo.setMinWidth(120);
        userInfo.getChildren().addAll(circle, usernameLabel);
        int[] difficultyInformation = user.getDifficultyInformation().get(difficulty);
        Label scoreLabel = createLabel(String.valueOf(difficultyInformation[0]), 100, 30);
        int minute = difficultyInformation[1] / 60;
        int second = difficultyInformation[1] - 60 * minute;
        Label timeLabel = createLabel(String.format("%02d:%02d", minute, second), 100, 30);
        Label diffLabel = createLabel(difficulty, 100, 30);
        if (counter == 1)
            setGold(rankLabel, userInfo, scoreLabel, timeLabel, diffLabel);
        else if (counter == 2)
            setSilver(rankLabel, userInfo, scoreLabel, timeLabel, diffLabel);
        else if (counter == 3)
            setBronze(rankLabel, userInfo, scoreLabel, timeLabel, diffLabel);
        rank.getChildren().add(rankLabel);
        username.getChildren().add(userInfo);
        score.getChildren().add(scoreLabel);
        time.getChildren().add(timeLabel);
        diff.getChildren().add(diffLabel);
    }

    private void setBronze(Label rankLabel, HBox userInfo, Label scoreLabel, Label timeLabel, Label diffLabel) {
        rankLabel.setStyle("-fx-background-color: #CD7F32;");
        userInfo.setStyle("-fx-background-color: #CD7F32;");
        scoreLabel.setStyle("-fx-background-color: #CD7F32;");
        timeLabel.setStyle("-fx-background-color: #CD7F32;");
        diffLabel.setStyle("-fx-background-color: #CD7F32;");
    }

    private void setSilver(Label rankLabel, HBox userInfo, Label scoreLabel, Label timeLabel, Label diffLabel) {
        rankLabel.setStyle("-fx-background-color: silver;");
        userInfo.setStyle("-fx-background-color: silver;");
        scoreLabel.setStyle("-fx-background-color: silver;");
        timeLabel.setStyle("-fx-background-color: silver;");
        diffLabel.setStyle("-fx-background-color: silver;");
    }

    private void setGold(Label rankLabel, HBox userInfo, Label scoreLabel, Label timeLabel, Label diffLabel) {
        rankLabel.setStyle("-fx-background-color: gold;");
        userInfo.setStyle("-fx-background-color: gold;");
        scoreLabel.setStyle("-fx-background-color: gold;");
        timeLabel.setStyle("-fx-background-color: gold;");
        diffLabel.setStyle("-fx-background-color: gold;");
    }

    private HBox options() {
        HBox hBox = AppCenter.createHbox(40);
        hBox.getChildren().addAll(backButton(), filter());
        return hBox;
    }

    private Button backButton() {
        Button button = AppCenter.createButton("Back");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    App.mainMenu.start(App.stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return button;
    }

    private HBox filter() {
        HBox hBox = AppCenter.createHbox(5);
        ComboBox comboBox = new ComboBox<>();
        comboBox.getItems().clear();
        comboBox.getItems().addAll("Easy", "Medium", "Hard");
        comboBox.setPromptText("Easy");
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sortUsers(comboBox.getValue().toString());
            }
        });
        hBox.getChildren().addAll(new Label("Filter by:"), comboBox);
        return hBox;
    }

    private Label createLabel(String text, int minWidth, int minHeight) {
        Label label = new Label(text);
        label.setMinWidth(minWidth);
        label.setMinHeight(minHeight);
        label.setAlignment(Pos.CENTER);
        return label;
    }

}
