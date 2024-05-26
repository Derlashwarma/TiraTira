package com.example.game.Bullets;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.Player;
import com.example.game.Game;
import com.example.game.Levels.Level1;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;

public class EnemyBulletLevel1 extends Bullet {

    private Player player;
    public EnemyBulletLevel1(double currentX, double currentY, ImageView enemyProj) {
        super(5, 1, 20, 20, currentX, currentY, Color.PURPLE, 1, "Enemy");
        this.player = Game.player;
    }
    @Override
    protected boolean checkCollision() {
        synchronized (pane) {
            if (getBoundsInParent().intersects(player.getBoundsInParent())) {
                if (player.isAlive()) {
                    player.changeHealth(-damage);
                    System.out.println("AGAY... Remaining Health: " + player.getHealth());
                    synchronized (Game.healthBar) {
                        Game.reduceProgress(damage/100);
                    }
                    if(player.getHealth() <= 0) {
                        Platform.runLater(() -> pane.getChildren().remove(player));
                        Game.endGame();
                    }
                }
                Platform.runLater(() -> pane.getChildren().remove(this));
                return true;
            }
        }
        return false;
    }
}
