package model;

import controller.AppCenter;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import view.ShootingBall;

public class Ball extends Circle {
    private Color color;
    private Line rod;
    private Text text;
    private int number;
    private int ownerNumber;
    private double startingX;
    private double startingY;
    private double textStartingX;
    private double textStartingY;

    public Ball(double radius, double x, double y, int number, Color color, int ownerNumber) {
        super(x, y, radius);
        super.setFill(color);
        this.color = color;
        this.number = number;
        this.ownerNumber = ownerNumber;
        text = new Text(String.valueOf(number));
        text.setStyle("-fx-font-size: 11;");
        text.setFill(Color.WHITE);
        double change = number < 10 ? 1 : -2;
        text.setX(x - radius / 2 + change);
        text.setY(y + radius / 2);
    }

    public int getOwnerNumber() {
        return ownerNumber;
    }

    public void setRod(Line rod) {
        rod.setStroke(color);
        this.rod = rod;
    }

    public Line getRod() {
        return rod;
    }

    public void moveLeft() {
        double x = this.getCenterX() - 4;
        if (x > 4) {
            this.setCenterX(x);
            text.setX(text.getX() - 4);
        }
    }

    public void moveRight() {
        double x = this.getCenterX() + 4;
        if (x < AppCenter.gameWidth - 4) {
            this.setCenterX(x);
            text.setX(text.getX() + 4);
        }
    }


    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }


    public void setStartingX(double startingX) {
        this.startingX = startingX;
    }

    public double getStartingX() {
        return startingX;
    }

    public Text getText() {
        return text;
    }

    public void setTextStartingX(double startingX) {
        textStartingX = startingX;
    }

    public double getTextStartingX() {
        return textStartingX;
    }

    public double getStartingY() {
        return startingY;
    }

    public double getTextStartingY() {
        return textStartingY;
    }

    public void setStartingY(double startingY) {
        this.startingY = startingY;
    }

    public void setTextStartingY(double textStartingY) {
        this.textStartingY = textStartingY;
    }

}
