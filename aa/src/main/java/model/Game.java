package model;

import controller.AppCenter;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Game {
    private Settings settings;
    private String musicAddress;
    private int phase;
    private int score;
    private int numberOfBallsLeft;
    private long timeSpent;
    private double freezePercent;
    private boolean scaling;
    private boolean changingVisibility;
    private ArrayList<SavingBall> savingBalls = new ArrayList<>();

    public Game(Settings settings) {
        this.settings = settings;
        this.numberOfBallsLeft = settings.getNumberOfBalls();
        this.scaling = false;
        this.changingVisibility = false;
        phase = 1;
        score = 0;
        freezePercent = 0;
        timeSpent = 0;
        musicAddress = Game.class.getResource("/AudioFiles/music1.mp3").toString();
        initialBalls();
    }

    private double sin(double theta) {
        return Math.sin(Math.toRadians(theta));
    }

    private double cos(double theta) {
        return Math.cos(Math.toRadians(theta));
    }

    private void initialBalls() {
        double x = AppCenter.ox;
        double y = AppCenter.oy;
        int r = 120;
        switch (settings.getMap()) {
            case 1:
                for (int i = 0; i < 5; i++)
                    savingBalls.add(new SavingBall(x + r * cos(i * 72), y + r * sin(i * 72), "man"));
                break;
            case 2:
                savingBalls.add(new SavingBall(x + r, y, "black"));
                savingBalls.add(new SavingBall(x - r, y, "black"));
                savingBalls.add(new SavingBall(x + r * cos(75), y + r * sin(75), "black"));
                savingBalls.add(new SavingBall(x + r * cos(105), y + r * sin(105), "black"));
                savingBalls.add(new SavingBall(x + r * cos(-75), y + r * sin(-75), "black"));
                savingBalls.add(new SavingBall(x + r * cos(-105), y + r * sin(-105), "black"));
                break;
            case 3:
                savingBalls.add(new SavingBall(x + r * cos(-15), y + r * sin(-15), "black"));
                savingBalls.add(new SavingBall(x + r * cos(75), y + r * sin(75), "black"));
                savingBalls.add(new SavingBall(x, y + r, "black"));
                savingBalls.add(new SavingBall(x + r * cos(105), y + r * sin(105), "black"));
                savingBalls.add(new SavingBall(x + r * cos(195), y + r * sin(195), "black"));
                break;
        }
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }

    public void setNumberOfBallsLeft(int numberOfBallsLeft) {
        this.numberOfBallsLeft = numberOfBallsLeft;
    }

    public Settings getSettings() {
        return settings;
    }

    public int getNumberOfBallsLeft() {
        return numberOfBallsLeft;
    }

    public double getFreezePercent() {
        return freezePercent;
    }

    public void setFreezePercent(double freezePercent) {
        this.freezePercent = freezePercent;
    }

    public boolean isChangingVisibility() {
        return changingVisibility;
    }

    public boolean isScaling() {
        return scaling;
    }

    public void setChangingVisibility(boolean changingVisibility) {
        this.changingVisibility = changingVisibility;
    }

    public void setScaling(boolean scaling) {
        this.scaling = scaling;
    }

    public ArrayList<SavingBall> getSavingBalls() {
        return savingBalls;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public String getMusicAddress() {
        return musicAddress;
    }

    public void setMusicAddress(String musicAddress) {
        this.musicAddress = musicAddress;
    }
}
