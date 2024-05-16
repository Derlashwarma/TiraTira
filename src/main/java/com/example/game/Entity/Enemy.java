package com.example.game.Entity;

import com.example.game.Bullets.EnemyBulletLevel1;
import com.example.game.Bullets.PlayerBullet;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Enemy extends Entity implements Runnable {
    private int movementPattern = 0;
    private final double BOTTOM_LIMIT = 760;
    private long speed;
    private ImageView enemyProjectile;

    public int getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(int movementPattern) {
        this.movementPattern = movementPattern;
    }

    private int points;

    public Enemy(long speed,double size,double health,double currentX, double currentY, Color color, String name, int points) {
        super(size,speed, health ,Color.WHITE, name);
        setCurrentX(currentX);
        setCurrentY(currentY);
        setDirectionX(1);
        this.points = points;
        //this.enemyProjectile = enemyProj;
    }
    public int getPoints() {
        return points;
    }

    private ImageView clone(ImageView to_clone) {
        ImageView imageView = new ImageView(to_clone.getImage());
        imageView.setFitHeight(to_clone.getFitHeight());
        imageView.setFitWidth(to_clone.getFitWidth());
        return imageView;
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
        Game.removeEnemy(this);
    }


}
