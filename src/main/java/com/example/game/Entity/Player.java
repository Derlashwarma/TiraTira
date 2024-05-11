package com.example.game.Entity;

import com.example.game.Bullets.PlayerBullet;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity implements Runnable{
    private String name;
    ArrayList<Thread> bullets;
    public Player(double size, Color color, String name) {
        super(size, color, name);
        bullets = new ArrayList<>();
        this.name = name;
    }
    public String getName(){
        return name;
    }

    private boolean checkCollision() {
        for(Node node: new ArrayList<>(anchorPane.getChildren())){
            if(node instanceof Enemy && getBoundsInParent().intersects(node.getBoundsInParent())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void move(){
        if(checkCollision()){
            setHealth(-(getHealth() * 0.05));
            System.out.println(getHealth());
            if(getHealth() <= 0){
                Game.endGame();
            }
        }
    }

    @Override
    public void run() {
        while(getHealth() > 0 && Game.game_running) {
            move();
            PlayerBullet playerBullet = new PlayerBullet(getLayoutX(),getLayoutY()-30, name);
            playerBullet.setAnchorPane(anchorPane);
            Platform.runLater(() -> {
                anchorPane.getChildren().add(playerBullet);
            });

            Thread thread = new Thread(playerBullet);
            thread.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(()->{
            anchorPane.getChildren().remove(this);
        });
        Thread.currentThread().interrupt();
    }
}
