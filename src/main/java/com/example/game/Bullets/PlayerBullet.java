package com.example.game.Bullets;

import com.example.game.Entity.Enemy;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class PlayerBullet extends Bullet{
    public PlayerBullet(double currentX, double currentY,String name) {
        super(20,1,10,10,currentX,currentY, Color.GREEN,-1,name);
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
                                if(node instanceof Enemy) {
                                    Game.addScore(1);
                                    System.out.println("SCOORE" + 1);
                                }
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
