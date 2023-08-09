package model;


import java.util.Random;

public class Settings {
    private Difficulty difficulty;
    private int numberOfBalls;
    private boolean soundOn;
    private boolean blackAndWhite;
    private long time;
    private String shootBall;
    private String moveBallLeft;
    private String moveBallRight;
    private String freeze;
    private int map;

    public Settings() {
        difficulty = Difficulty.EASY;
        numberOfBalls = 8;
        soundOn = true;
        blackAndWhite = false;
        shootBall = "SPACE";
        freeze = "TAB";
        moveBallLeft = "LEFT";
        moveBallRight = "RIGHT";
        time = 300000;
        map = 1;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getNumberOfBalls() {
        return numberOfBalls;
    }

    public boolean isBlackAndWhite() {
        return blackAndWhite;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void setBlackAndWhite(boolean blackAndWhite) {
        this.blackAndWhite = blackAndWhite;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setNumberOfBalls(int numberOfBalls) {
        this.numberOfBalls = numberOfBalls;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }

    public void setShootBall(String shootBall) {
        this.shootBall = shootBall;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public void setMoveBallLeft(String moveBallLeft) {
        this.moveBallLeft = moveBallLeft;
    }

    public void setMoveBallRight(String moveBallRight) {
        this.moveBallRight = moveBallRight;
    }

    public String getShootBall() {
        return shootBall;
    }

    public String getFreeze() {
        return freeze;
    }

    public String getMoveBallLeft() {
        return moveBallLeft;
    }

    public String getMoveBallRight() {
        return moveBallRight;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public double getRandomAngle() {
        Random random = new Random();
        int sign = 2 * (new Random().nextInt(0,2)) - 1;
        return sign * random.nextDouble(difficulty.getMinAngle(), difficulty.getMaxAngle());
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public Settings clone() {
        Settings result = new Settings();
        result.setDifficulty(difficulty);
        result.setNumberOfBalls(numberOfBalls);
        result.setBlackAndWhite(blackAndWhite);
        result.setSoundOn(soundOn);
        result.setBlackAndWhite(blackAndWhite);
        result.setTime(time);
        result.setShootBall(shootBall);
        result.setMoveBallLeft(moveBallLeft);
        result.setMoveBallRight(moveBallRight);
        result.setFreeze(freeze);
        result.setMap(map);
        return result;
    }
}
