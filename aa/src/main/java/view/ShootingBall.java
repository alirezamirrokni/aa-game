package view;

import controller.AppCenter;
import controller.GameController;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Ball;

public class ShootingBall extends Transition {
    private Ball ball;
    private Text text;
    private GameScene gameScene;
    private GameController controller;
    private boolean twoPlayers;
    private int playerNumber;
    private double startingX;
    private double startingY;
    private double textStartingX;
    private double textStartingY;
    private double ratio;
    private static double angle = 0.0;

    public ShootingBall(Ball ball, GameScene gameScene, GameController controller, int playerNumber, boolean twoPlayers) {
        this.ball = ball;
        this.text = ball.getText();
        this.gameScene = gameScene;
        this.controller = controller;
        this.startingX = ball.getStartingX();
        this.startingY = ball.getStartingY();
        this.textStartingX = ball.getTextStartingX();
        this.textStartingY = ball.getTextStartingY();
        this.twoPlayers = twoPlayers;
        this.playerNumber = playerNumber;
        this.ratio = -Math.tan(Math.toRadians(angle * 2));
        this.setCycleDuration(Duration.millis(1000));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        double y = ball.getCenterY() + ((playerNumber == 1) ? -10 : 10);
        double x = startingX + ratio * (y - startingY);
        double y1 = ball.getText().getY() + ((playerNumber == 1) ? -10 : 10);
        double x1 = textStartingX + ratio * (y1 - textStartingY);
        ball.setCenterY(y);
        ball.setCenterX(x);
        text.setX(x1);
        text.setY(y1);
        if (controller.intersectsRotatingBalls(ball)) {
            this.stop();
            if (!twoPlayers)
                gameScene.endGame(false);
            else
                ((TwoPlayerGameScene) gameScene).endGame(3 - ball.getOwnerNumber());
        } else if (controller.startRotate(ball)) {
            this.stop();
            gameScene.rotateNewBall(ball, twoPlayers);
            if (!twoPlayers && controller.getNumberOfBallsLeft() == 0)
                gameScene.endGame(true);
            else if (twoPlayers) {
                TwoPlayerGameScene twoPlayerGameScene = (TwoPlayerGameScene) gameScene;
                if (twoPlayerGameScene.getFirstPlayerNumberOfBallsLeft() == 0)
                    twoPlayerGameScene.endGame(1);
                if (twoPlayerGameScene.getSecondPlayerNumberOfBallsLeft() == 0)
                    twoPlayerGameScene.endGame(2);
            }
        }
        if (controller.isOut(x, y)) {
            this.stop();
            if (!twoPlayers)
                gameScene.endGame(false);
            else
                ((TwoPlayerGameScene) gameScene).endGame(3 - ball.getOwnerNumber());
        }
    }

    public static double getAngle() {
        return angle;
    }

    public static void setAngle(double angle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ShootingBall.angle = angle;
            }
        });
    }

    public static void resetAngle() {
        angle = 0.0;
    }
}
