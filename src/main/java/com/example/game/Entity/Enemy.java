package com.example.game.Entity;

import com.example.game.Bullets.EnemyBulletLevel1;
import com.example.game.Bullets.PlayerBullet;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class Enemy extends Entity implements Runnable{
    private final double BOTTOM_LIMIT = 760;
    private long speed;
    public Enemy(long speed,double size,double currentX, double currentY, Color color, String name) {
        super(size, color, name);
        setCurrentX(currentX);
        setCurrentY(currentY);
        setDirectionX(1);
        setDirection(1);
        this.speed = speed;
    }

    @Override
    public void run() {
        Game.enemies.add(this);
        int counter = 0;
        while(getLayoutY() < BOTTOM_LIMIT  && isAlive() && Game.game_running) {
            move();
            if((counter % 20) == 0 ) {
                EnemyBulletLevel1 bullet = new EnemyBulletLevel1(getLayoutX(),getLayoutY());
                bullet.setPane(anchorPane);
                Platform.runLater(()->{
                    anchorPane.getChildren().add(bullet);
                });
                Thread enemyBullet = new Thread(bullet);
                enemyBullet.start();
            }
            try {
                Thread.sleep(speed);
                counter++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(() -> {
            anchorPane.getChildren().remove(this);
        });
        Thread.currentThread().interrupt();
        Game.enemies.remove(this);
    }
}
