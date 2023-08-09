package view;

import controller.AppCenter;
import controller.GameController;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Ball;

public class RotatingBall extends Transition {
    private Ball ball;
    private Line rod;
    private int speed;
    private int frozenTime;
    private GameScene gameScene;
    private GameController controller;
    private static boolean isFreezing = false;
    private static long startFreezeTime;
    private static long startChangingRotation;
    private static long rotationDuration;
    private static int direction;
    private static long startChangingVisibility;


    public RotatingBall(Ball ball, GameScene gameScene, GameController controller) {
        this.ball = ball;
        this.rod = ball.getRod();
        this.gameScene = gameScene;
        this.controller = controller;
        this.frozenTime = controller.getFrozenTime();
        this.speed = controller.getRotationSpeed();
        this.setCycleDuration(Duration.INDEFINITE);
        this.setCycleCount(-1);
    }

    public static void startFreezing(long time) {
        startFreezeTime = time;
    }


    public static void startChangingVisibility(long time) {
        startChangingVisibility = time;
    }

    public static void setDirection() {
        direction = 1;
    }

    @Override
    protected void interpolate(double v) {
        long currentTime = System.currentTimeMillis();
        double theta = getAngle(currentTime);
        rotateBall(theta);
        rotateRod(theta);
        if (startChangingVisibility > 0) {
            if ((int) Math.floor((currentTime - startChangingVisibility) / 2000) % 2 == 0) {
                ball.setVisible(false);
                rod.setVisible(false);
            } else {
                ball.setVisible(true);
                rod.setVisible(true);
            }
        }
        if (controller.getPhase() >= 2 && currentTime - startChangingRotation >= rotationDuration) {
            startChangingRotation = currentTime;
            rotationDuration = AppCenter.getRandomDuration();
            direction *= -1;
        }


    }


    private void rotateRod(double theta) {
        double ox = AppCenter.ox;
        double oy = AppCenter.oy;
        double x1 = rod.getStartX();
        double y1 = rod.getStartY();
        double px1 = Math.cos(theta) * (x1 - ox) - Math.sin(theta) * (y1 - oy) + ox;
        double py1 = Math.sin(theta) * (x1 - ox) + Math.cos(theta) * (y1 - oy) + oy;
        double x2 = rod.getEndX();
        double y2 = rod.getEndY();
        double px2 = Math.cos(theta) * (x2 - ox) - Math.sin(theta) * (y2 - oy) + ox;
        double py2 = Math.sin(theta) * (x2 - ox) + Math.cos(theta) * (y2 - oy) + oy;
        rod.setStartX(px1);
        rod.setStartY(py1);
        rod.setEndX(px2);
        rod.setEndY(py2);
    }

    private void rotateBall(double theta) {
        double ox = AppCenter.ox;
        double oy = AppCenter.oy;
        double x = ball.getCenterX();
        double y = ball.getCenterY();
        double px = Math.cos(theta) * (x - ox) - Math.sin(theta) * (y - oy) + ox;
        double py = Math.sin(theta) * (x - ox) + Math.cos(theta) * (y - oy) + oy;
        ball.setCenterX(px);
        ball.setCenterY(py);
    }

    private double getAngle(long currentTime) {
        if (currentTime - startFreezeTime > frozenTime)
            return direction * Math.toRadians(speed / 10.0);
        return direction * Math.toRadians(0.1);
    }


}
