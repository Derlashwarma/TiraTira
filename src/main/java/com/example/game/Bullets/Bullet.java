package com.example.game.Bullets;

import com.example.game.Entity.Enemy;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public abstract class Bullet extends javafx.scene.shape.Rectangle implements Runnable{
    private double speed;
    private double currentX;
    private double currentY;
    private final double BOTTOM_LIMIT = 760;
    private final double TOP_LIMIT = 10;
    private int direction;
    private double damage;
    private String name;
    private static final Object lock = new Object();

    private AnchorPane pane;
    public Bullet(double damage, double speed, double v, double v1, double currentX, double currentY, Paint paint, int direction, String name){
        super(v, v1, paint);
        this.speed = speed;
        this.damage = damage;
        this.currentX = currentX;
        this.currentY = currentY;
        this.direction = direction;
        this.name = name;
    }
    protected void move(){
        Platform.runLater(()->{
            setLayoutX(currentX);
            setLayoutY(currentY);
        });
        currentY += direction;
    }
    public void setPane(AnchorPane pane){
        this.pane = pane;
    }

    protected boolean checkCollision() {
        synchronized (pane) {
            try{
                for (Node node : new ArrayList<>(pane.getChildren())) {
                    if (node instanceof Enemy && getBoundsInParent().intersects(node.getBoundsInParent())) {
                        Enemy enemy = (Enemy) node;
                        if (enemy.isAlive()) {
                            enemy.changeHealth(-damage);
                            System.out.println("HIT... Remaining Health: " + enemy.getHealth());
                            if(enemy.getHealth() <= 0) {
                                Platform.runLater(() -> pane.getChildren().remove(enemy));
                                System.out.println("SCORE PLUS 1");
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
    @Override
    public void run() {
        while(currentY < BOTTOM_LIMIT && currentY > TOP_LIMIT && !checkCollision()) {
            move();
            try {
                Thread.sleep((long)speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(() ->{
            pane.getChildren().remove(this);
            Thread.currentThread().interrupt();
        });
    }

}
