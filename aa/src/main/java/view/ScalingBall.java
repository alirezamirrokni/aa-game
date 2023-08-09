package view;

import controller.AppCenter;
import controller.GameController;
import javafx.animation.Transition;
import javafx.util.Duration;
import model.Ball;

public class ScalingBall extends Transition {
    private Ball ball;
    private GameScene gameScene;
    private GameController controller;
    private static double scalingCounter = 0;
    private static boolean scaling = false;

    public ScalingBall(Ball ball, GameScene gameScene, GameController controller) {
        this.ball = ball;
        this.gameScene = gameScene;
        this.controller = controller;
        this.setCycleDuration(Duration.INDEFINITE);
        this.setCycleCount(-1);
    }

    public static void setScaling(boolean scaling) {
        ScalingBall.scaling = scaling;
    }

    @Override
    protected void interpolate(double v) {
        if (scaling) {
            int change = Math.abs((int) Math.floor(scalingCounter) % 3 - 2);
            ball.setRadius((1.2 * AppCenter.ballRadius) - change);
            scalingCounter += 0.001;
            if (intersectBalls()) {
                this.stop();
                if (gameScene instanceof TwoPlayerGameScene twoPlayerGameScene)
                    twoPlayerGameScene.endGame(3 - ball.getOwnerNumber());
                else
                    gameScene.endGame(false);
            }
        }
    }

    private boolean intersectBalls() {
        for (Ball rotatingBall : gameScene.getRotatingBalls())
            if (!ball.equals(rotatingBall) && ball.getNumber() <= rotatingBall.getNumber()
                    && controller.doIntersect(ball, rotatingBall))
                return true;
        return false;
    }

}
