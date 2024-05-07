package com.example.game.Bullets;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.Player;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class EnemyBulletLevel1 extends Bullet {

    private Player player;
    public EnemyBulletLevel1(double currentX, double currentY) {
        super(10, 1, 10, 10, currentX, currentY, Color.PURPLE, 1, "Enemy");
        this.player = Game.player;
    }
    @Override
    protected boolean checkCollision() {
        synchronized (pane) {
            if (getBoundsInParent().intersects(player.getBoundsInParent())) {
                if (player.isAlive()) {
                    player.changeHealth(-damage);
                    System.out.println("AGAY... Remaining Health: " + player.getHealth());
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
