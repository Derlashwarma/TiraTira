package com.example.game.Entity;

import com.example.game.Bullets.PlayerBullet;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class Enemy extends Entity implements Runnable{
    private final double BOTTOM_LIMIT = 760;
    private String name;
    private long speed;
    public Enemy(long speed,double size,double currentX, double currentY, Color color, String name) {
        super(size, color, name);
        setCurrentX(currentX);
        setCurrentY(currentY);
        setDirection(1);
        this.speed = speed;
        this.name = name;
    }

    @Override
    public void run() {
        Game.enemies.add(this);
        while(getLayoutY() < BOTTOM_LIMIT  && isAlive() && Game.game_running) {
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
        Game.enemies.remove(this);
    }
}
