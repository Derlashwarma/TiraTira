package com.example.game.Entity;

import com.example.game.Bullets.PlayerBullet;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
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
                synchronized ((Object)Game.game_running){
                    Game.game_running = false;
                }
            }
            return;
        }
    }

    @Override
    public void run() {
        while(getHealth() > 0 && Game.game_running) {
            Platform.runLater(() -> {
                PlayerBullet playerBullet = new PlayerBullet(20,1 ,10,10,getCurrentX(),getCurrentY()-30,Color.RED, -1, name);
                playerBullet.setAnchorPane(anchorPane);
                anchorPane.getChildren().add(playerBullet);
                Thread thread = new Thread(playerBullet);
                thread.start();
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Thread.currentThread().interrupt();
    }
}
