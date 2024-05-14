package com.example.game.Entity;

import com.example.game.Bullets.EnemyBulletLevel1;
import com.example.game.Bullets.PlayerBullet;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Enemy extends Entity implements Runnable {
    private int movementPattern = 0;
    private final double BOTTOM_LIMIT = 760;
    private long speed;

    public int getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(int movementPattern) {
        this.movementPattern = movementPattern;
    }

    public Enemy(long speed,double size,double health,double currentX, double currentY, Color color, String name) {
        super(size,speed, health ,Color.WHITE, name);
        setCurrentX(currentX);
        setCurrentY(currentY);
        setDirectionX(1);
    }

    @Override
    public void run() {
        Game.enemies.add(this);
        while (getLayoutY() < BOTTOM_LIMIT && isAlive() && Game.game_running) {
            move();
            shoot();
            try {
                Thread.sleep(getSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(() -> {
            anchorPane.getChildren().remove(this);
            anchorPane.getChildren().remove(enemy);
            anchorPane.getChildren().remove(second_form);
        });
        Thread.currentThread().interrupt();
        Game.enemies.remove(this);
    }


}
