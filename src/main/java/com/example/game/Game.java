package com.example.game;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.Player;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Random;

public class Game implements Runnable{
    public AnchorPane main_container;
    private static boolean game_running;
    private double minimumHealth = 100;
    private double minimumSpeed = 50;
    private final int minRange = 5;
    private final int maxRange = 495;
    private final int range = maxRange - minRange + 1;
    private int interval = 2000;

    public Game(AnchorPane pane) {
        this.main_container = pane;
        game_running = true;
    }
    @Override
    public void run() {
        Player player = new Player(20, Color.GREEN,"Ardel");
        player.setAnchorPane(main_container);
        Thread playerThread = new Thread(player);
        main_container.getChildren().add(player);
        playerThread.start();

        main_container.setOnMouseMoved(event -> {
            player.setLayoutX(event.getX());
            player.setLayoutY(event.getY());
            player.setCurrentX(event.getX());
            player.setCurrentY(event.getY());
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

            try {
                Thread.sleep(interval);
                interval-=10;
                minimumHealth += 10;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
