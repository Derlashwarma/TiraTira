package com.example.game;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game implements Runnable{
    public static AnchorPane main_container;
    public static boolean game_running;
    private double minimumHealth = 100;
    private double minimumSpeed = 50;
    private int interval = 2000;
    public static ArrayList<Runnable> enemies;
    private ImageView character;
    public static int score;
    public static Player player;

    public Game(AnchorPane pane, ImageView character) {
        this.main_container = pane;
        game_running = true;
        enemies = new ArrayList<>();
        this.character = character;
    }
    public static void addScore(int sc){
        score += sc;
    }
    public static void endGame(){
        game_running = false;
        Platform.runLater(() -> {
            Stage currentStage = (Stage) main_container.getScene().getWindow(); // Get the current stage
            currentStage.close(); // Close current stage
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("game_over.fxml"));
                Parent root = fxmlLoader.load();
                GameOver gameOverController = fxmlLoader.getController();
                gameOverController.setTotalScore(score);
                gameOverController.setPlayerName(player.getName() + "'S SCORE:");
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getScene().getStylesheets().add(Game.class.getResource("menu_styles.css").toExternalForm());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        while(game_running){
            player = new Player(20, Color.GREEN, "JEECOO");
            player.setAnchorPane(main_container);
            player.setAnchorPane(main_container);
            Thread playerThread = new Thread(player);
            playerThread.start();

            main_container.setOnMouseMoved(event -> {
                character.setLayoutX(event.getX()-50);
                character.setLayoutY(event.getY()-40);
                player.setLayoutX(event.getX());
                player.setLayoutY(event.getY());
                player.setCurrentX(event.getX());
                player.setCurrentY(event.getY());
            });
            Random random = new Random();
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
                        interval -= 10;
                    }
                    minimumHealth += 10;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("GAME OVER");
            System.out.println("TOTAL SCORE: " + score);
            Platform.runLater(()->{
                main_container.getChildren().removeAll();
            });
        }


    }
}
