package controller;

import model.Difficulty;
import model.User;

import java.io.IOException;

public class SettingsController {
    public void changeDifficulty(String difficulty) throws IOException {
        AppCenter.getSettings().setDifficulty(Difficulty.getDifficultyByName(difficulty));
        User.updateUsers();
    }

    public void changeNumberOfBalls(int number) throws IOException {
        AppCenter.getSettings().setNumberOfBalls(number);
        User.updateUsers();
    }

    public void changeSoundOn(boolean isSoundOn) throws IOException {
        AppCenter.getSettings().setSoundOn(isSoundOn);
        User.updateUsers();
    }

    public void changeBlackAndWhite(boolean isBlackAndWhite) throws IOException {
        AppCenter.getSettings().setBlackAndWhite(isBlackAndWhite);
        User.updateUsers();
    }

    public void changeShootBall(String keyName) throws IOException {
        if (AppCenter.getSettings().getFreeze().equals(keyName) ||
                AppCenter.getSettings().getMoveBallLeft().equals(keyName) ||
                AppCenter.getSettings().getMoveBallRight().equals(keyName))
            return;
        AppCenter.getSettings().setShootBall(keyName);
        User.updateUsers();
    }

    public void changeFreeze(String keyName) throws IOException {
        if (AppCenter.getSettings().getShootBall().equals(keyName) ||
                AppCenter.getSettings().getMoveBallLeft().equals(keyName) ||
                AppCenter.getSettings().getMoveBallRight().equals(keyName))
            return;
        AppCenter.getSettings().setFreeze(keyName);
        User.updateUsers();
    }

    public void changeMoveBallRight(String keyName) throws IOException {
        if (AppCenter.getSettings().getFreeze().equals(keyName) ||
                AppCenter.getSettings().getMoveBallLeft().equals(keyName) ||
                AppCenter.getSettings().getShootBall().equals(keyName))
            return;
        AppCenter.getSettings().setMoveBallRight(keyName);
        User.updateUsers();
    }

    public void changeMoveBallLeft(String keyName) throws IOException {
        if (AppCenter.getSettings().getFreeze().equals(keyName) ||
                AppCenter.getSettings().getShootBall().equals(keyName) ||
                AppCenter.getSettings().getMoveBallRight().equals(keyName))
            return;
        AppCenter.getSettings().setMoveBallLeft(keyName);
        User.updateUsers();
    }
}
