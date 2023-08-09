package view;

import controller.AppCenter;
import controller.GameController;
import javafx.animation.Transition;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.Ball;

public class EndGameAnimation extends Transition {
    private Ball ball;
    private Line rod;
    private double ratio;
    private int sign;
    private boolean won;
    private int winnerNumber;
    private GameScene gameScene;
    private GameController controller;
    private static boolean gameOver = true;

    public EndGameAnimation(Ball ball, boolean won, int winnerNumber, GameScene gameScene, GameController controller) {
        this.ball = ball;
        this.rod = ball.getRod();
        this.won = won;
        this.winnerNumber = winnerNumber;
        this.gameScene = gameScene;
        this.controller = controller;
        double dx = ball.getBoundsInParent().getCenterX() - AppCenter.ox;
        double dy = ball.getBoundsInParent().getCenterY() - AppCenter.oy;
        ratio = dx / dy;
        sign = dy > 0 ? 1 : -1;
        this.setCycleDuration(Duration.millis(500));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        double y = ball.getCenterY() + sign * 5;
        double x = (y - AppCenter.oy) * ratio + AppCenter.ox;
        ball.setCenterX(x);
        ball.setCenterY(y);
        if (rod != null) {
            rod.setEndX(x);
            rod.setEndY(y);
        }
        if (stopAnimation(x, y))
            this.stop();
    }

    public static void resetGameOver() {
        gameOver = false;
    }

    private boolean stopAnimation(double x, double y) {
        if (controller.isOut(x, y)) {
            if (!gameOver) {
                gameOver = true;
                gameScene.displayEndGame(won, winnerNumber);
            }
            return true;
        }
        return false;
    }
}
