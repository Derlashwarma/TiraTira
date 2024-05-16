package com.example.game.Bullets;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.EnemyT1Bomber;
import com.example.game.Entity.EnemyT1Strafer;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class PlayerBullet extends Bullet{
    public PlayerBullet(double currentX, double currentY,String name) {
        super(100,1,10,10,currentX,currentY, Color.GREEN,-1,name);
    }
    public void setAnchorPane(AnchorPane pane){
        super.pane = pane;
    }


    @Override
    protected boolean checkCollision() {
        synchronized (pane) {
            try{
                for (Node node : new ArrayList<>(pane.getChildren())) {
                    if (node instanceof Enemy && getBoundsInParent().intersects(node.getBoundsInParent())) {
                        Enemy enemy = (Enemy) node;
                        if (enemy.isAlive()) {
                            enemy.changeHealth(-damage);
                            if(enemy.getHealth() <= 0) {
                                Platform.runLater(() -> pane.getChildren().remove(enemy));
                                int add = 0;
                                if (node instanceof EnemyT1Strafer) {
                                    add = 2;
                                } else if (node instanceof EnemyT1Bomber) {
                                    add = 5;
                                }
                                Game.addScore(add);
                            }
                        }
                        Platform.runLater(() -> pane.getChildren().remove(this));
                        return true;
                    }
                }
            }
            catch (Exception e){
                Platform.runLater(() ->{
                    pane.getChildren().remove(this);
                    Thread.currentThread().interrupt();
                });
            }
        }
        return false;
    }
}
