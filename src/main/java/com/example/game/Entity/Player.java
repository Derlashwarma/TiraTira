package com.example.game.Entity;

import com.example.game.Bullets.Bullet;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Player extends Entity implements Runnable{
    ArrayList<Thread> bullets;
    public Player(double size, Color color, String name) {
        super(size, color, name);
        bullets = new ArrayList<>();
    }

    @Override
    public void run() {

        while(getHealth() > 0) {
            Platform.runLater(() -> {
                Bullet bullet = new Bullet(20,1 ,10,10,getCurrentX(),getCurrentY()-30,Color.RED, -1);
                bullet.setAnchorPane(anchorPane);
                anchorPane.getChildren().add(bullet);
                Thread thread = new Thread(bullet);
                thread.start();
            });
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for(Thread thread : bullets) {
            thread.interrupt();
        }
    }
}
