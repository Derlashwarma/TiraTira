package com.example.game.Entity;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Random;

public class Entity extends javafx.scene.shape.Circle{
    private double health;
    private double currentX;
    private double currentY;
    protected AnchorPane anchorPane;
    protected String name;
    private int directionX;

    private int direction;
    private boolean isAlive;

    public Entity(double size, Color color, String name){
        super(size);
        health = 200;
        this.name = name;
        isAlive = true;
    }
    public String getName(){return name;}

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public synchronized void changeHealth(double amount) {
        health += amount;
        if(health <= 0) {
            setAlive(false);
        }
    }
    public synchronized double getHealth() {
        return health;
    }

    public synchronized void setHealth(double health) {
        this.health = health;
    }

    public double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public void move() {
        int width = (int) anchorPane.widthProperty().get();
        Platform.runLater(() -> {
            setLayoutX(currentX);
            setLayoutY(currentY);
        });
        double x = this.getRadius();

        if (currentX+x> width) {
            setDirectionX(-1);
        }
        if (currentX-x < 0) {
            setDirectionX(1);
        }

        currentX += directionX;
        currentY += direction;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
