package com.example.game.Entity;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Enemy extends Entity implements Runnable{
    private final double BOTTOM_LIMIT = 760;
    private long speed;
    public Enemy(long speed,double size,double currentX, double currentY, Color color, String name) {
        super(size, color, name);
        setCurrentX(currentX);
        setCurrentY(currentY);
        setDirection(1);
        this.speed = speed;
    }

    @Override
    public void run() {
        while(getLayoutY() < BOTTOM_LIMIT  && isAlive()) {
            move();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(() -> {
            anchorPane.getChildren().remove(this);
            Thread.currentThread().interrupt();
        });
    }
}
