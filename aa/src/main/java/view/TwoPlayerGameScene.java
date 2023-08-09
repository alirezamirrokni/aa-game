package view;

import controller.AppCenter;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Ball;
import model.Game;

import java.io.IOException;
import java.util.ArrayList;

public class TwoPlayerGameScene extends GameScene {
    private Ball firstPlayerBall;
    private Ball secondPlayerBall;
    private int firstPlayerNumberOfBallsLeft = controller.getNumberOfBallsLeft();
    private int secondPlayerNumberOfBallsLeft = controller.getNumberOfBallsLeft();
    private StackPane firstPlayerNumberOfBallsLeftMonitor;
    private StackPane secondPlayerNumberOfBallsLeftMonitor;

    @Override
    public void start(Stage stage) throws Exception {
        setGeneralSettings(stage);
        createBall(1);
        createBall(2);
        setShowDetails();
        controller.setNumberOfBallsLeft(controller.getNumberOfBallsLeft() * 2);
    }

    @Override
    protected Scene createScene() {
        Scene scene = new Scene(gamePane);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().toString();
                if (keyName.equals("TAB") && progressBar.getProgress() > 0.9
                        && !controller.isGameOver() && !controller.isPaused()) {
                    RotatingBall.startFreezing(System.currentTimeMillis());
                    controller.setFreezePercent(0);
                    progressBar.setProgress(0);
                } else if (keyName.equals("SPACE")) {
                    beep();
                    shootBall(1);
                } else if (keyName.equals("ENTER")) {
                    beep();
                    shootBall(2);
                } else if (keyName.equals("LEFT")) {
                    firstPlayerBall.moveLeft();
                } else if (keyName.equals("RIGHT")) {
                    firstPlayerBall.moveRight();
                } else if (keyName.equals("A")) {
                    secondPlayerBall.moveLeft();
                } else if (keyName.equals("D")) {
                    secondPlayerBall.moveRight();
                } else if (keyName.equals("ESCAPE")) {
                    pauseGame();
                    pauseStage.show();
                }
            }
        });
        return scene;
    }


    private void createBall(int playerNumber) {
        if (playerNumber == 1) {
            firstPlayerBall = new Ball(AppCenter.ballRadius, AppCenter.gameWidth / 2, 580,
                    firstPlayerNumberOfBallsLeft, controller.getBallColor(firstPlayerNumberOfBallsLeft, 1), 1);
            gamePane.getChildren().addAll(firstPlayerBall, firstPlayerBall.getText());
            balls.add(firstPlayerBall);
        } else {
            secondPlayerBall = new Ball(AppCenter.ballRadius, AppCenter.gameWidth / 2, 100,
                    secondPlayerNumberOfBallsLeft, controller.getBallColor(secondPlayerNumberOfBallsLeft, 2), 2);
            gamePane.getChildren().addAll(secondPlayerBall, secondPlayerBall.getText());
            balls.add(secondPlayerBall);
        }

    }

    private void shootBall(int playerNumber) {
        controller.setNumberOfBallsLeft(controller.getNumberOfBallsLeft() - 1);
        Ball ball = null;
        if (playerNumber == 1) {
            firstPlayerNumberOfBallsLeft--;
            ball = firstPlayerBall;
        } else {
            secondPlayerNumberOfBallsLeft--;
            ball = secondPlayerBall;
        }
        ball.setStartingX(ball.getCenterX());
        ball.setStartingY(ball.getCenterY());
        ball.setTextStartingX(ball.getText().getX());
        ball.setTextStartingY(ball.getText().getY());
        ShootingBall shootingBall = new ShootingBall(ball, this, controller, playerNumber, true);
        shootingBall.play();
        transitions.add(shootingBall);
        shootingBalls.add(ball);
        createBall(playerNumber);
        ((Circle) firstPlayerNumberOfBallsLeftMonitor.getChildren().get(0)).setFill(firstPlayerBall.getColor());
        ((Circle) secondPlayerNumberOfBallsLeftMonitor.getChildren().get(0)).setFill(secondPlayerBall.getColor());
    }

    public void endGame(int winnerPlayer) {
        if (controller.isGameOver())
            return;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    prepareEndGame(true, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!controller.isBlackAndWhite())
                    gamePane.setStyle("-fx-background-color: green;");
                else
                    gamePane.setStyle("-fx-background-color: gray");
                if (controller.isSoundOn()) {
                    mediaPlayer = new MediaPlayer(new Media(GameScene.class.getResource("/AudioFiles/win.mp3").toString()));
                    mediaPlayer.play();
                }
                showEndGameAnimation(true, winnerPlayer);
            }
        });
    }

    @Override
    protected void refreshData() {
        ((Text) firstPlayerNumberOfBallsLeftMonitor.getChildren().get(1)).setText(String.valueOf(firstPlayerNumberOfBallsLeft));
        ((Text) secondPlayerNumberOfBallsLeftMonitor.getChildren().get(1)).setText(String.valueOf(secondPlayerNumberOfBallsLeft));
    }

    @Override
    protected void createNumberOfBallsLeft() {
        Circle circle1 = new Circle(20);
        circle1.setFill(firstPlayerBall.getColor());
        Text text1 = new Text();
        text1.setFill(Color.WHITE);
        text1.setStyle("-fx-font-size: 20");
        firstPlayerNumberOfBallsLeftMonitor = new StackPane();
        firstPlayerNumberOfBallsLeftMonitor.getChildren().addAll(circle1, text1);
        firstPlayerNumberOfBallsLeftMonitor.setLayoutX(10);
        firstPlayerNumberOfBallsLeftMonitor.setLayoutY(AppCenter.gameHeight - 50);
        Circle circle2 = new Circle(20);
        circle2.setFill(secondPlayerBall.getColor());
        Text text2 = new Text();
        text2.setFill(Color.WHITE);
        text2.setStyle("-fx-font-size: 20");
        secondPlayerNumberOfBallsLeftMonitor = new StackPane();
        secondPlayerNumberOfBallsLeftMonitor.getChildren().addAll(circle2, text2);
        secondPlayerNumberOfBallsLeftMonitor.setLayoutX(60);
        secondPlayerNumberOfBallsLeftMonitor.setLayoutY(AppCenter.gameHeight - 50);
    }

    @Override
    protected void setShowDetails() {
        VBox vBox = getInformationVbox();
        showAngle(ShootingBall.getAngle());
        createNumberOfBallsLeft();
        refreshData();
        angle.setLayoutX(AppCenter.gameWidth - 50);
        angle.setLayoutY(AppCenter.gameHeight - 20);
        gamePane.getChildren().addAll(vBox, angle, firstPlayerNumberOfBallsLeftMonitor, secondPlayerNumberOfBallsLeftMonitor);
    }


    @Override
    protected void pauseGame() {
        pause();
        BorderPane borderPane = new BorderPane();
        VBox vBox = AppCenter.createVbox(30);
        vBox.getChildren().addAll(getResume(), getRestart(), getMusicOptions(), getHelp());
        borderPane.setCenter(vBox);
        setPauseStage(borderPane);
    }

    @Override
    protected Button getRestart() {
        Button restart = AppCenter.createButton("Restart");
        restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pauseStage.close();
                mediaPlayer.stop();
                AppCenter.setCurrentGame(new Game(AppCenter.getSettings().clone()));
                try {
                    new TwoPlayerGameScene().start(App.stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return restart;
    }

    @Override
    protected Button getHelp() {
        Button help = AppCenter.createButton("Help");
        help.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                BorderPane borderPane = new BorderPane();
                borderPane.getStylesheets().add(GameScene.class.getResource("/Style.css").toString());
                VBox vBox = AppCenter.createVbox(30);
                Label firstPlayerShootBall = new Label("First player shoot ball: SPACE");
                Label secondPlayerShootBall = new Label("Second player shoot ball: ENTER");
                Label freeze = new Label("Freeze: TAB");
                Label firstPlayerMoveRight = new Label("First player move ball right: RIGHT");
                Label firstPlayerMoveLeft = new Label("First player move ball left : LEFT");
                Label secondPlayerMoveRight = new Label("Second player move ball right: D");
                Label secondPlayerMoveLeft = new Label("Second player move ball left : A");
                vBox.getChildren().addAll(firstPlayerShootBall, secondPlayerShootBall, freeze,
                        firstPlayerMoveRight, firstPlayerMoveLeft, secondPlayerMoveRight, secondPlayerMoveLeft);
                borderPane.setCenter(vBox);
                AppCenter.createStage("Help", new Scene(borderPane, 300, 400)).show();
            }
        });
        return help;
    }

    public int getFirstPlayerNumberOfBallsLeft() {
        return firstPlayerNumberOfBallsLeft;
    }

    public int getSecondPlayerNumberOfBallsLeft() {
        return secondPlayerNumberOfBallsLeft;
    }

    @Override
    public void displayEndGame(boolean won, int winnerNumber) {
        createEndGameStage(winnerNumber);
        endGameStage.show();
    }

    private void createEndGameStage(int winnerNumber) {
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GameScene.class.getResource("/Style.css").toString());
        VBox vBox = AppCenter.createVbox(30);
        Label label = new Label("Player " + winnerNumber + " Won!");
        label.setStyle("-fx-font-size: 30;");
        Button exit = exitGame();
        Button restart = restartGame();
        vBox.getChildren().addAll(label, restart, exit);
        borderPane.setCenter(vBox);
        endGameStage = AppCenter.createStage("Game over", new Scene(borderPane, 300, 300));
        endGameStage.initModality(Modality.WINDOW_MODAL);
        endGameStage.initOwner(App.stage);
    }

    @Override
    protected Button restartGame() {
        Button button = AppCenter.createButton("Restart game");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    AppCenter.setCurrentGame(new Game(AppCenter.getSettings().clone()));
                    endGameStage.close();
                    new TwoPlayerGameScene().start(App.stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return button;
    }
}
