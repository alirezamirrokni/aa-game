package view;

import controller.AppCenter;
import controller.GameController;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Ball;
import model.Game;
import model.SavingBall;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameScene extends Application {
    protected GameController controller = new GameController(this);
    protected Pane gamePane;
    protected MediaPlayer mediaPlayer;
    protected MediaPlayer beepPlayer;
    protected StackPane centerCircle;
    protected ProgressBar progressBar;
    protected Timer timer;
    protected long startGame;
    protected long startPause;
    protected long pauseTime = 0;
    private Label score;
    private StackPane numberOfBallsLeft;
    protected Label time;
    protected Label angle;
    protected Stage endGameStage;
    protected Stage pauseStage;
    protected ArrayList<Ball> balls = new ArrayList<>();
    protected ArrayList<Ball> rotatingBalls = new ArrayList<>();
    protected ArrayList<Ball> shootingBalls = new ArrayList<>();
    protected ArrayList<Transition> transitions = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        setGeneralSettings(stage);
        createBalls();
        setShowDetails();
    }

    protected void setGeneralSettings(Stage stage) throws IOException {
        startGame = System.currentTimeMillis();
        gamePane = FXMLLoader.load(GameScene.class.getResource("/FXML/Game.fxml"));
        ShootingBall.resetAngle();
        if (controller.isSoundOn()) startMusic();
        stage.setScene(createScene());
        stage.show();
        setCenterCircle();
        setBackGroundColor();
        setTimer();
        initializeRotatingBalls();
    }

    protected void setBackGroundColor() {
        if (controller.isBlackAndWhite())
            return;
        switch (controller.getPhase()) {
            case 2:
                gamePane.setStyle("-fx-background-color: yellow");
                break;
            case 3:
                gamePane.setStyle("-fx-background-color: cyan");
                break;
            case 4:
                gamePane.setStyle("-fx-background-color: magenta");
                break;
        }
    }

    protected void setTimer() {
        time = new Label();
        setTimeText(controller.getSpentTime());
        time.setLayoutX(AppCenter.gameWidth - 50);
        time.setLayoutY(10);
        gamePane.getChildren().add(time);
        scheduleTimer();
    }

    private void scheduleTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!controller.isGameOver())
                    endGame(false);
            }
        };

        TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                setTimeText(System.currentTimeMillis() - startGame + controller.getSpentTime() - pauseTime);
            }
        };

        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                if (controller.getPhase() == 4) {
                    double angle = controller.getRandomAngle();
                    ShootingBall.setAngle(angle);
                    showAngle(angle);
                }
            }
        };
        timer.schedule(timerTask, controller.getTime() - controller.getSpentTime());
        timer.schedule(timerTask1, 0, 1000);
        timer.schedule(timerTask2, 0, 5000);
    }

    private void setTimeText(long milliSecs) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int min = (int) (milliSecs / 60000);
                int sec = (int) ((milliSecs - 60000 * min) / 1000);
                String result = String.format("%02d:%02d", min, sec);
                time.setText(result);
            }
        });
    }

    protected Scene createScene() {
        Scene scene = new Scene(gamePane);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().toString();
                if (keyName.equals(controller.getFreeze()) && progressBar.getProgress() > 0.9
                        && !controller.isGameOver() && !controller.isPaused()) {
                    RotatingBall.startFreezing(System.currentTimeMillis());
                    controller.setFreezePercent(0);
                    progressBar.setProgress(0);
                } else if (keyName.equals("ESCAPE")) {
                    pauseGame();
                    pauseStage.show();
                }

            }
        });
        return scene;
    }


    protected void pauseGame() {
        pause();
        BorderPane borderPane = new BorderPane();
        VBox vBox = AppCenter.createVbox(30);
        vBox.getChildren().addAll(getResume(), getRestart(), getMusicOptions(), getHelp(), getSaveAndExit());
        borderPane.setCenter(vBox);
        setPauseStage(borderPane);
    }

    protected void setPauseStage(BorderPane borderPane) {
        Scene scene = new Scene(borderPane, 400, 340);
        borderPane.getStylesheets().add(GameScene.class.getResource("/Style.css").toString());
        pauseStage = AppCenter.createStage("Pause", scene);
        pauseStage.initModality(Modality.WINDOW_MODAL);
        pauseStage.initOwner(App.stage);
    }

    protected Button getResume() {
        Button resume = AppCenter.createButton("Resume");
        resume.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pauseStage.close();
                resume();
            }
        });
        return resume;
    }

    private Button getSaveAndExit() {
        Button save = AppCenter.createButton("Save & Exit");
        save.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pauseStage.close();
                mediaPlayer.stop();
                try {
                    controller.saveGame(time.getText(), rotatingBalls);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    App.mainMenu.start(App.stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return save;
    }

    protected Button getRestart() {
        Button restart = AppCenter.createButton("Restart");
        restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pauseStage.close();
                mediaPlayer.stop();
                AppCenter.setCurrentGame(new Game(AppCenter.getSettings().clone()));
                try {
                    new GameScene().start(App.stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return restart;
    }

    protected Button getHelp() {
        Button help = AppCenter.createButton("Help");
        help.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                BorderPane borderPane = new BorderPane();
                borderPane.getStylesheets().add(GameScene.class.getResource("/Style.css").toString());
                VBox vBox = AppCenter.createVbox(30);
                Label shootBall = new Label("Shoot ball: " + controller.getShootBall());
                Label freeze = new Label("Freeze: " + controller.getFreeze());
                Label moveRight = new Label("Move ball right: " + controller.getMoveBallRight());
                Label moveLeft = new Label("Move ball left: " + controller.getMoveBallLeft());
                vBox.getChildren().addAll(shootBall, freeze, moveRight, moveLeft);
                borderPane.setCenter(vBox);
                AppCenter.createStage("Help", new Scene(borderPane, 300, 300)).show();
            }
        });
        return help;
    }

    protected HBox getMusicOptions() {
        HBox hBox = AppCenter.createHbox(15);
        Label label = new Label("Music:");
        CheckBox music1 = new CheckBox("music1");
        CheckBox music2 = new CheckBox("music2");
        CheckBox music3 = new CheckBox("music3");
        CheckBox mute = new CheckBox("mute");
        checkCurrentMusic(music1, music2, music3, mute);
        hBox.getChildren().addAll(label, music1, music2, music3, mute);
        music1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (music1.isSelected()) {
                    cancelOthers(music2, music3, mute);
                    setMusic(GameScene.class.getResource("/AudioFiles/music1.mp3").toString());
                } else
                    music1.setSelected(true);
            }
        });
        music2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (music2.isSelected()) {
                    cancelOthers(music1, music3, mute);
                    setMusic(GameScene.class.getResource("/AudioFiles/music2.mp3").toString());
                } else
                    music2.setSelected(true);
            }
        });
        music3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (music3.isSelected()) {
                    cancelOthers(music1, music2, mute);
                    setMusic(GameScene.class.getResource("/AudioFiles/music3.mp3").toString());
                } else
                    music3.setSelected(true);
            }
        });
        mute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (mute.isSelected()) {
                    cancelOthers(music1, music2, music3);
                    mediaPlayer.stop();
                } else {
                    String address = controller.getMusicAddress();
                    setMusic(address);
                    if (address.contains("music1"))
                        music1.setSelected(true);
                    else if (address.contains("music2"))
                        music2.setSelected(true);
                    else
                        music3.setSelected(true);
                }
            }
        });
        return hBox;
    }

    private void checkCurrentMusic(CheckBox music1, CheckBox music2, CheckBox music3, CheckBox mute) {
        if (!controller.isSoundOn())
            mute.setSelected(true);
        else if (controller.getMusicAddress().contains("music1"))
            music1.setSelected(true);
        else if (controller.getMusicAddress().contains("music2"))
            music2.setSelected(true);
        else
            music3.setSelected(true);
    }

    private void setMusic(String address) {
        controller.setMusicAddress(address);
        if (mediaPlayer != null)
            mediaPlayer.stop();
        Media media = new Media(address);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(-1);
    }

    private void cancelOthers(CheckBox cB1, CheckBox cB2, CheckBox cB3) {
        cB1.setSelected(false);
        cB2.setSelected(false);
        cB3.setSelected(false);
    }

    private void resume() {
        pauseTime += System.currentTimeMillis() - startPause;
        controller.setPaused(false);
        scheduleTimer();
        for (Transition transition : transitions)
            transition.play();
    }

    protected void pause() {
        startPause = System.currentTimeMillis();
        controller.setPaused(true);
        timer.cancel();
        for (Transition transition : transitions)
            transition.stop();
    }

    protected void startMusic() {
        Media media = new Media(controller.getMusicAddress());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(-1);
        mediaPlayer.play();
    }

    protected void setCenterCircle() {
        Circle circle = new Circle(40);
        Text text = new Text(String.valueOf(controller.getPhase()));
        text.setStyle("-fx-font-size: 40;");
        text.setFill(Color.WHITE);
        centerCircle = new StackPane();
        centerCircle.getChildren().add(circle);
        centerCircle.getChildren().add(text);
        centerCircle.setLayoutX(AppCenter.ox - 40);
        centerCircle.setLayoutY(AppCenter.oy - 40);
        gamePane.getChildren().add(centerCircle);
    }

    private void refreshCenterCircle() {
        if (controller.getPhase() > 1)
            ((Text) centerCircle.getChildren().get(1)).setText(String.valueOf(controller.getPhase()));
    }

    protected void setShowDetails() {
        VBox vBox = getInformationVbox();
        score = new Label();
        vBox.getChildren().add(score);
        showAngle(ShootingBall.getAngle());
        createNumberOfBallsLeft();
        refreshData();
        gamePane.getChildren().addAll(vBox, angle);
        gamePane.getChildren().add(numberOfBallsLeft);
    }

    protected VBox getInformationVbox() {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Label label = new Label("freeze: ");
        progressBar = new ProgressBar(controller.getFreezePercent());
        hBox.getChildren().addAll(label, progressBar);
        vBox.getChildren().add(hBox);
        vBox.setLayoutX(10);
        vBox.setLayoutY(10);
        vBox.setSpacing(7);
        angle = new Label();
        angle.setLayoutX(AppCenter.gameWidth - 50);
        angle.setLayoutY(AppCenter.gameHeight - 20);
        return vBox;
    }

    protected void createNumberOfBallsLeft() {
        Circle circle = new Circle(20);
        circle.setFill(balls.get(balls.size() - 1).getColor());
        Text text = new Text();
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font-size: 20");
        numberOfBallsLeft = new StackPane();
        numberOfBallsLeft.getChildren().addAll(circle, text);
        numberOfBallsLeft.setLayoutX(10);
        numberOfBallsLeft.setLayoutY(AppCenter.gameHeight - 50);
    }


    protected void refreshData() {
        ((Text) numberOfBallsLeft.getChildren().get(1)).setText(String.valueOf(controller.getNumberOfBallsLeft()));
        score.setText("Score: " + controller.getScore());
    }

    protected void initializeRotatingBalls() {
        for (SavingBall savingBall : controller.getSavingBalls())
            rotateNewBall(savingBall);
    }

    private void createBalls() {
        int numberOfBalls = controller.getNumberOfBallsLeft();
        double y = AppCenter.gameHeight - 140;
        double x = AppCenter.gameWidth / 2;
        for (int i = 1; i <= numberOfBalls; i++) {
            Ball ball = createBall(i, x, (numberOfBalls - i) * 35 + AppCenter.ballRadius + y);
            balls.add(ball);
            gamePane.getChildren().add(ball);
            gamePane.getChildren().add(ball.getText());
        }
        balls.get(balls.size() - 1).requestFocus();
    }

    private Ball createBall(int number, double x, double y) {
        Ball ball = new Ball(AppCenter.ballRadius, x, y, number, controller.getBallColor(number, 1), 1);
        GameScene gameScene = this;
        ball.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().toString();
                if (keyName.equals(controller.getShootBall()) && !controller.isGameOver() && !controller.isPaused()) {
                    ball.setStartingX(ball.getCenterX());
                    ball.setStartingY(ball.getCenterY());
                    ball.setTextStartingX(ball.getText().getX());
                    ball.setTextStartingY(ball.getText().getY());
                    if (controller.isSoundOn())
                        beep();
                    shootBall(ball);
                    ShootingBall shootingBall = new ShootingBall(ball, gameScene, controller, 1, false);
                    shootingBall.play();
                } else if (keyName.equals(controller.getMoveBallRight()) && controller.getPhase() == 4
                        && !controller.isGameOver() && !controller.isPaused())
                    ball.moveRight();
                else if (keyName.equals(controller.getMoveBallLeft()) && controller.getPhase() == 4
                        && !controller.isGameOver() && !controller.isPaused())
                    ball.moveLeft();
            }
        });
        return ball;
    }

    private void shootBall(Ball shootingBall) {
        controller.shootBall();
        balls.remove(shootingBall);
        shootingBalls.add(shootingBall);
        for (Ball ball : balls) {
            ball.setCenterY(ball.getCenterY() - 35);
            ball.getText().setY(ball.getText().getY() - 35);
        }
        if (balls.size() > 0) {
            Ball ball = balls.get(balls.size() - 1);
            ((Circle) numberOfBallsLeft.getChildren().get(0)).setFill(ball.getColor());
            balls.get(balls.size() - 1).requestFocus();
        }
    }

    protected void beep() {
        Media media = new Media(GameScene.class.getResource("/AudioFiles/beep.mp3").toString());
        beepPlayer = new MediaPlayer(media);
        beepPlayer.play();
    }

    public void rotateNewBall(SavingBall savingBall) {
        Ball ball = new Ball(AppCenter.ballRadius, savingBall.getX(), savingBall.getY(), 0, Color.BLACK, 1);
        double dx = savingBall.getX() - AppCenter.ox;
        double dy = savingBall.getY() - AppCenter.oy;
        double ratio = 40 / Math.sqrt(dx * dx + dy * dy);
        double startX = ratio * dx + AppCenter.ox;
        double startY = ratio * dy + AppCenter.oy;
        rotatingBalls.add(ball);
        Line rod = new Line(startX, startY, ball.getCenterX(), ball.getCenterY());
        ball.setRod(rod);
        gamePane.getChildren().addAll(ball, rod);
        RotatingBall rotatingBall = new RotatingBall(ball, this, controller);
        ScalingBall scalingBall = new ScalingBall(ball, this, controller);
        transitions.add(rotatingBall);
        transitions.add(scalingBall);
        rotatingBall.play();
        scalingBall.play();
    }

    public void rotateNewBall(Ball ball, boolean twoPlayers) {
        double dx = ball.getBoundsInParent().getCenterX() - AppCenter.ox;
        double dy = ball.getBoundsInParent().getCenterY() - AppCenter.oy;
        double ratio = 40 / Math.sqrt(dx * dx + dy * dy);
        double startX = ratio * dx + AppCenter.ox;
        double startY = ratio * dy + AppCenter.oy;
        gamePane.getChildren().remove(ball.getText());
        controller.setPhase(gamePane, twoPlayers);
        refreshCenterCircle();
        refreshData();
        progressBar.setProgress(progressBar.getProgress() + 0.2);
        controller.setFreezePercent(progressBar.getProgress());
        rotatingBalls.add(ball);
        shootingBalls.remove(ball);
        Line rod = new Line(startX, startY, ball.getCenterX(), ball.getCenterY());
        ball.setRod(rod);
        gamePane.getChildren().add(rod);
        RotatingBall rotatingBall = new RotatingBall(ball, this, controller);
        ScalingBall scalingBall = new ScalingBall(ball, this, controller);
        transitions.add(rotatingBall);
        transitions.add(scalingBall);
        rotatingBall.play();
        scalingBall.play();
    }

    public ArrayList<Ball> getRotatingBalls() {
        return rotatingBalls;
    }


    public void displayEndGame(boolean won, int winnerNumber) {
        if (!won) {
            createEndGameStage("You Lost!", false);
            endGameStage.show();
        } else {
            createEndGameStage("You Won!", true);
            endGameStage.show();
        }
    }

    public void endGame(boolean won) {
        if (controller.isGameOver())
            return;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    prepareEndGame(won, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!won) {
                    if (!controller.isBlackAndWhite())
                        gamePane.setStyle("-fx-background-color: red;");
                    else
                        gamePane.setStyle("-fx-background-color: gray;");
                    if (controller.isSoundOn()) {
                        mediaPlayer = new MediaPlayer(new Media(GameScene.class.getResource("/AudioFiles/lose.mp3").toString()));
                        mediaPlayer.play();
                    }
                } else {
                    if (!controller.isBlackAndWhite())
                        gamePane.setStyle("-fx-background-color: green;");
                    else
                        gamePane.setStyle("-fx-background-color: gray");
                    if (controller.isSoundOn()) {
                        mediaPlayer = new MediaPlayer(new Media(GameScene.class.getResource("/AudioFiles/win.mp3").toString()));
                        mediaPlayer.play();
                    }
                }
                showEndGameAnimation(won, 1);
            }
        });
    }

    protected void prepareEndGame(boolean won, boolean twoPlayers) throws IOException {
        if (!twoPlayers)
            controller.gameOver(time.getText(), score.getText(), won);
        else
            controller.setGameOver(true);
        EndGameAnimation.resetGameOver();
        for (Transition transition : transitions)
            transition.stop();
        if (mediaPlayer != null)
            mediaPlayer.stop();
        timer.cancel();
        if (shootingBalls.size() > 0)
            gamePane.getChildren().remove(shootingBalls.get(0).getText());
        for (int i = 1; i < shootingBalls.size(); i++)
            gamePane.getChildren().removeAll(shootingBalls.get(i), rotatingBalls.get(i).getText());
        for (Ball ball : balls)
            gamePane.getChildren().removeAll(ball, ball.getText());
    }

    protected void showEndGameAnimation(boolean won, int winnerNumber) {
        if (!shootingBalls.isEmpty()) {
            EndGameAnimation animation = new EndGameAnimation(shootingBalls.get(0), won, 1, this, controller);
            animation.play();
        }
        for (Ball rotatingBall : rotatingBalls) {
            rotatingBall.setVisible(true);
            rotatingBall.getRod().setVisible(true);
            rotatingBall.getRod().setStrokeWidth(3);
            EndGameAnimation animation = new EndGameAnimation(rotatingBall, won, winnerNumber, this, controller);
            animation.play();
        }
    }

    private void createEndGameStage(String text, boolean won) {
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GameScene.class.getResource("/Style.css").toString());
        VBox vBox = AppCenter.createVbox(30);
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 30;");
        Button exit = exitGame();
        Button restart = restartGame();
        if (won) {
            Label score = new Label("your " + this.score.getText().toLowerCase() + "\n\nyour time: " + time.getText());
            vBox.getChildren().addAll(label, score, restart, exit);
        } else
            vBox.getChildren().addAll(label, restart, exit);
        borderPane.setCenter(vBox);
        endGameStage = AppCenter.createStage("Game over", new Scene(borderPane, 300, 300));
        endGameStage.initModality(Modality.WINDOW_MODAL);
        endGameStage.initOwner(App.stage);
    }

    protected Button exitGame() {
        Button button = AppCenter.createButton("Exit Game");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    endGameStage.close();
                    App.mainMenu.start(App.stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return button;
    }

    protected Button restartGame() {
        Button button = AppCenter.createButton("Restart game");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    AppCenter.setCurrentGame(new Game(AppCenter.getSettings().clone()));
                    endGameStage.close();
                    new GameScene().start(App.stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return button;
    }

    public void showAngle(double degree) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                angle.setText(String.format("%.2f°", degree));
            }
        });
    }

}

