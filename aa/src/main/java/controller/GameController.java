package controller;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.*;
import view.GameScene;
import view.RotatingBall;
import view.ScalingBall;

import java.io.IOException;
import java.util.ArrayList;

public class GameController {
    private Game game;
    private GameScene gameScene;
    private boolean gameOver = false;
    private boolean paused = false;

    public GameController(GameScene gameScene) {
        game = AppCenter.getCurrentGame();
        this.gameScene = gameScene;
        modifyAnimations();

    }

    private void modifyAnimations() {
        ScalingBall.setScaling(game.isScaling());
        RotatingBall.setDirection();
        RotatingBall.startFreezing(0);
        if (game.isChangingVisibility())
            RotatingBall.startChangingVisibility(System.currentTimeMillis());
        else
            RotatingBall.startChangingVisibility(0);
    }

    public int getPhase() {
        return game.getPhase();
    }

    public boolean isSoundOn() {
        return AppCenter.getSettings().isSoundOn();
    }

    public String getShootBall() {
        return AppCenter.getSettings().getShootBall();
    }

    public String getFreeze() {
        return AppCenter.getSettings().getFreeze();
    }

    public String getMoveBallLeft() {
        return AppCenter.getSettings().getMoveBallLeft();
    }

    public String getMoveBallRight() {
        return AppCenter.getSettings().getMoveBallRight();
    }

    public void shootBall() {
        setNumberOfBallsLeft(game.getNumberOfBallsLeft() - 1);
        int change = game.getSettings().getNumberOfBalls() - game.getNumberOfBallsLeft();
        game.setScore(game.getScore() + change);
    }

    public void setNumberOfBallsLeft(int numberOfBallsLeft) {
        game.setNumberOfBallsLeft(numberOfBallsLeft);
    }

    public void setPhase(Pane pane, boolean twoPlayers) {
        int numberOfBallsLeft = game.getNumberOfBallsLeft();
        int numberOfBalls = game.getSettings().getNumberOfBalls() * (twoPlayers ? 2 : 1);
        int phase = 1;
        int quarter = numberOfBalls / 4;
        if (numberOfBallsLeft <= 3 * quarter && numberOfBallsLeft > 2 * quarter)
            phase = 2;
        else if (numberOfBallsLeft <= 2 * quarter && numberOfBallsLeft > quarter)
            phase = 3;
        else if (numberOfBallsLeft <= quarter)
            phase = 4;
        if (numberOfBallsLeft == 3 * quarter) {
            ScalingBall.setScaling(true);
            game.setScaling(true);
            if (!AppCenter.getSettings().isBlackAndWhite())
                pane.setStyle("-fx-background-color: yellow;");
        } else if (numberOfBallsLeft == 2 * quarter) {
            RotatingBall.startChangingVisibility(System.currentTimeMillis());
            game.setChangingVisibility(true);
            if (!AppCenter.getSettings().isBlackAndWhite())
                pane.setStyle("-fx-background-color: cyan;");
        } else if (numberOfBallsLeft == quarter) {
            if (!AppCenter.getSettings().isBlackAndWhite())
                pane.setStyle("-fx-background-color: magenta");
        }
        game.setPhase(phase);
    }

    public int getNumberOfBallsLeft() {
        return game.getNumberOfBallsLeft();
    }

    public Color getBallColor(int number, int playerNumber) {
        if (AppCenter.getSettings().isBlackAndWhite())
            return (playerNumber == 1) ? Color.BLACK : Color.DARKGRAY;
        double numberOfBalls = game.getSettings().getNumberOfBalls() * 1.0;
        if (number <= 5)
            return (playerNumber == 1) ? Color.PURPLE : Color.DARKBLUE;
        if (number / numberOfBalls <= 0.5)
            return (playerNumber == 1) ? Color.DARKORANGE : Color.RED;
        return (playerNumber == 1) ? Color.BLACK : Color.DARKGRAY;
    }

    public int getRotationSpeed() {
        return game.getSettings().getDifficulty().getRotationSpeed();
    }

    public int getFrozenTime() {
        return game.getSettings().getDifficulty().getFrozenTime();
    }

    public double getFreezePercent() {
        return game.getFreezePercent();
    }

    public void setFreezePercent(double percent) {
        game.setFreezePercent(percent);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void gameOver(String time, String score, boolean won) throws IOException {
        setGameOver(true);
        String[] scoreSplitter = score.split(": ");
        String[] minuteAndSecond = time.split(":");
        game.setScore(Integer.parseInt(scoreSplitter[1]));
        int timeSpent = Integer.parseInt(minuteAndSecond[0]) * 60 + Integer.parseInt(minuteAndSecond[1]);
        User currentUser = AppCenter.getCurrentUser();
        String difficulty = game.getSettings().getDifficulty().toString();
        int[] difficultyInformation = currentUser.getDifficultyInformation().get(difficulty);
        if (won && (game.getScore() > difficultyInformation[0] ||
                game.getScore() == difficultyInformation[0] && timeSpent < difficultyInformation[1]))
            currentUser.setDifficultyInformation(difficulty, new int[]{game.getScore(), timeSpent});
        currentUser.setLastGame(null);
        User.updateUsers();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean doIntersect(Ball ball1, Ball ball2) {
        double dx = ball1.getCenterX() - ball2.getCenterX();
        double dy = ball1.getCenterY() - ball2.getCenterY();
        double radiusSum = ball1.getRadius() + ball2.getRadius();
        return Math.sqrt(dx * dx + dy * dy) <= radiusSum + 0.1;
    }


    public boolean isBlackAndWhite() {
        return AppCenter.getSettings().isBlackAndWhite();
    }

    public ArrayList<SavingBall> getSavingBalls() {
        return game.getSavingBalls();
    }

    public boolean startRotate(Ball ball) {
        double dx = ball.getCenterX() - AppCenter.ox;
        double dy = ball.getCenterY() - AppCenter.oy;
        return (Math.sqrt(dx * dx + dy * dy) <= 120);
    }

    public boolean intersectsRotatingBalls(Ball ball) {
        for (Ball rotatingBall : gameScene.getRotatingBalls())
            if (doIntersect(ball, rotatingBall))
                return true;
        return false;
    }

    public void saveGame(String time, ArrayList<Ball> rotatingBalls) throws IOException {
        game.getSavingBalls().clear();
        for (Ball rotatingBall : rotatingBalls)
            game.getSavingBalls().add(new SavingBall(rotatingBall.getCenterX(), rotatingBall.getCenterY(), rotatingBall.getColor().toString()));
        String[] minuteAndSecond = time.split(":");
        int timeSpent = Integer.parseInt(minuteAndSecond[0]) * 60 + Integer.parseInt(minuteAndSecond[1]);
        game.setTimeSpent(timeSpent * 1000);
        AppCenter.getCurrentUser().setLastGame(game);
        User.updateUsers();
    }

    public int getScore() {
        return game.getScore();
    }

    public long getTime() {
        return game.getSettings().getTime();
    }

    public long getSpentTime() {
        return game.getTimeSpent();
    }


    public String getMusicAddress() {
        return game.getMusicAddress();
    }

    public void setMusicAddress(String address) {
        game.setMusicAddress(address);
    }

    public double getRandomAngle() {
        return game.getSettings().getRandomAngle();
    }

    public boolean isOut(double x, double y) {
        double ballRadius = AppCenter.ballRadius;
        return (x < -ballRadius || x > AppCenter.gameWidth + ballRadius ||
                y < -ballRadius || y > AppCenter.gameHeight + ballRadius);
    }


}
