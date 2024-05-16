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
    private ImageView playerProjectile;
    ArrayList<Thread> bullets;
    private ImageView bullet;

    public Player(double size, Color color, String name, ImageView playerProd) {
        super(size,0, 200, color, name);
        bullets = new ArrayList<>();
        this.name = name;
        this.playerProjectile = playerProd;
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
    private ImageView clone(ImageView to_clone) {
        ImageView imageView = new ImageView(to_clone.getImage());
        imageView.setFitHeight(to_clone.getFitHeight());
        imageView.setFitWidth(to_clone.getFitWidth());
        return imageView;
    }

    public void setBullet(ImageView bullet){
        this.bullet = bullet;
    }

    @Override
    public void run() {
        while(getHealth() > 0 && Game.game_running) {
            move();
            PlayerBullet playerBullet = new PlayerBullet(getLayoutX()-10,getLayoutY()-50, name);
            playerBullet.setAnchorPane(anchorPane);
//            playerBullet.setVisible(false);
            ImageView clone = clone(playerProjectile);
            playerBullet.setBullet(clone);
            Platform.runLater(() -> {
                anchorPane.getChildren().addAll(playerBullet,clone);
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