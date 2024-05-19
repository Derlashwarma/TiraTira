package com.example.game.Entity;

import com.example.game.Bullets.PlayerBullet;
import com.example.game.Bullets.PlayerBulletLevel2;
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
    private static String powerup;

    public Player(double size, Color color, String name, ImageView playerProd) {
        super(size,0, 200, color, name);
        bullets = new ArrayList<>();
        this.name = name;
        this.playerProjectile = playerProd;
        powerup = null;
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
            PlayerBullet[] playerBullet = new PlayerBullet[3];
            double currX = getCurrentX();
            double currY = getCurrentY();
            if(powerup == null) {
                spawnLevel1PlayerBullet(playerBullet,currX,currY);
            } else if(powerup.equalsIgnoreCase("playerBulletLevel2")) {
                spawnLevel2PlayerBullet(playerBullet,currX,currY);
            } else if(powerup.equalsIgnoreCase("playerBulletLevel3")) {
                spawnLevel3PlayerBullet(playerBullet,currX,currY);
            }
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
    public static String getPowerup() {
        return Player.powerup;
    }

    public static void setPowerup(String powerup) {
        Player.powerup = powerup;
    }

    private void spawnLevel1PlayerBullet(PlayerBullet[] playerBullet, double currentX, double currentY) {
        ImageView clone = clone(playerProjectile);
        playerBullet[0] = new PlayerBullet(20,currentX-4,currentY-90, name);
        playerBullet[0].setAnchorPane(anchorPane);
        playerBullet[0].setBullet(clone);
        Platform.runLater(() -> {
            anchorPane.getChildren().addAll(playerBullet[0],clone);
        });
        Thread thread = new Thread(playerBullet[0]);
        thread.start();
    }
    private void spawnLevel2PlayerBullet(PlayerBullet[] playerBullet, double currentX, double currentY) {
        ImageView clone1 = clone(playerProjectile);
        ImageView clone2 = clone(playerProjectile);
        playerBullet[0] = new PlayerBulletLevel2(currentX+36,currentY-50, name);
        playerBullet[1] = new PlayerBulletLevel2(currentX-40,currentY-50, name);
        playerBullet[0].setAnchorPane(anchorPane);
        playerBullet[1].setAnchorPane(anchorPane);
        playerBullet[0].setBullet(clone1);
        playerBullet[1].setBullet(clone2);
        Platform.runLater(() -> {
            anchorPane.getChildren().addAll(playerBullet[0],playerBullet[1],clone1,clone2);
        });
        Thread thread1 = new Thread(playerBullet[0]);
        thread1.start();
        Thread thread2 = new Thread(playerBullet[1]);
        thread2.start();
    }
    private void spawnLevel3PlayerBullet(PlayerBullet[] playerBullet, double currentX, double currentY) {
        if(currentX < 30 && currentY < 30) {
            return;
        }
        ImageView clone1 = clone(playerProjectile);
        ImageView clone2 = clone(playerProjectile);
        ImageView clone3 = clone(playerProjectile);
        playerBullet[0] = new PlayerBulletLevel2(currentX+36,currentY-40, name);
        playerBullet[1] = new PlayerBulletLevel2(currentX-4,currentY-80, name);
        playerBullet[2] = new PlayerBulletLevel2(currentX-40,currentY-40, name);
        playerBullet[0].setAnchorPane(anchorPane);
        playerBullet[1].setAnchorPane(anchorPane);
        playerBullet[2].setAnchorPane(anchorPane);
        playerBullet[0].setBullet(clone1);
        playerBullet[1].setBullet(clone2);
        playerBullet[2].setBullet(clone3);
        Platform.runLater(() -> {
            anchorPane.getChildren().addAll(playerBullet[0],playerBullet[1],playerBullet[2],clone1,clone2,clone3);
        });
        Thread thread1 = new Thread(playerBullet[0]);
        thread1.start();
        Thread thread2 = new Thread(playerBullet[1]);
        thread2.start();
        Thread thread3 = new Thread(playerBullet[2]);
        thread3.start();
    }
}