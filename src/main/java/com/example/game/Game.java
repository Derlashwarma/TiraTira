package com.example.game;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.Player;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game implements Runnable{
    public AnchorPane main_container;
    public static boolean game_running;
    private double minimumHealth = 100;
    private double minimumSpeed = 50;
    private final int minRange = 5;
    private final int maxRange = 495;
    private final int range = maxRange - minRange + 1;
    private int interval = 2000;
    public static ArrayList<Runnable> enemies;

    public Game(AnchorPane pane) {
        this.main_container = pane;
        game_running = true;
        enemies = new ArrayList<>();
    }

    @Override
    public void run() {
        while(game_running){
            Player player = new Player(20, Color.GREEN,"Ardel");
            player.setAnchorPane(main_container);
            player.setAnchorPane(main_container);
            Thread playerThread = new Thread(player);
            main_container.getChildren().addAll(player);
            playerThread.start();

            main_container.setOnMouseMoved(event -> {
                player.setLayoutX(event.getX());
                player.setLayoutY(event.getY());
                player.setCurrentX(event.getX());
                player.setCurrentY(event.getY());
                player.move();
            });
            Random random = new Random();
            double game_screen = main_container.getWidth();

            while(game_running) {
                int minRange = 5;
                int maxRange = 495;
                int range = maxRange - minRange + 1;
                int previousValue = 0;
                int tempValue = random.nextInt(range + 1) + minRange;
                int randomX = tempValue;
                if (tempValue == previousValue || tempValue == previousValue + 50 || tempValue == previousValue - 50) {
                    do {
                        tempValue = random.nextInt(range + 1) + minRange;
                        randomX = tempValue;
                    } while (tempValue == previousValue || tempValue == previousValue + 50 || tempValue == previousValue - 50);
                }
                previousValue = randomX;
                if(enemies.size() < 10) {
                    long speed = (long)(random.nextDouble() * (50 - minimumSpeed) + minimumSpeed);
                    double health = random.nextInt((int)minimumHealth);
                    Enemy enemy = new Enemy(speed, 50, randomX, 0, Color.BLUE,"Enemy");
                    enemy.setAnchorPane(main_container);
                    enemy.setHealth(health);
                    Thread enemyThread = new Thread(enemy);
                    Platform.runLater(()->{
                        main_container.getChildren().add(enemy);
                    });
                    enemyThread.start();
                }

                try {
                    Thread.sleep(interval);
                    if(interval > 50) {
                        interval-=10;
                    }
                    minimumHealth += 10;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("GAME OVER");
            main_container.getChildren().removeAll();
        }
    }
}
