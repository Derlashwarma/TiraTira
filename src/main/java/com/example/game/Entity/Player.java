package com.example.game.Entity;

import com.example.game.Bullets.PlayerBullet;
import javafx.application.Platform;
import javafx.scene.paint.Color;

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

    @Override
    public void run() {

        while(getHealth() > 0) {
            Platform.runLater(() -> {
                PlayerBullet playerBullet = new PlayerBullet(20,1 ,10,10,getCurrentX(),getCurrentY()-30,Color.RED, -1, name);
                playerBullet.setAnchorPane(anchorPane);
                anchorPane.getChildren().add(playerBullet);
                Thread thread = new Thread(playerBullet);
                thread.start();
            });
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
