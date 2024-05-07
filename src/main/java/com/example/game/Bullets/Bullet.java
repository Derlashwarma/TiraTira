package com.example.game.Bullets;

import com.example.game.Entity.Enemy;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public abstract class Bullet extends javafx.scene.shape.Rectangle implements Runnable{
    protected double speed;
    protected double currentX;
    protected double currentY;
    protected final double BOTTOM_LIMIT = 760;
    protected final double TOP_LIMIT = 10;
    protected int direction;
    protected double damage;
    protected String name;
    protected static final Object lock = new Object();

    protected AnchorPane pane;
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
    protected abstract boolean checkCollision();
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
