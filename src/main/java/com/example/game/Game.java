package com.example.game;

import com.example.game.Bullets.EnemyBulletLevel1;
import com.example.game.Entity.Enemy;
import com.example.game.Entity.EnemyT1Bomber;
import com.example.game.Entity.EnemyT1Strafer;
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
import java.util.*;

public class Game implements Runnable{
    public static AnchorPane main_container;
    public static boolean game_running;
    private double minimumHealth = 100;
    private double minimumSpeed = 50;
    private int interval = 2000;
    public static ArrayList<Runnable> enemies;
    private ImageView character;
    private ImageView enemy_type_1;
    private ImageView enemy_type_2;
    public static int score;
    public static Player player;
    private static String name;

    public Game(AnchorPane pane, ImageView character, ImageView background, ImageView background2) {
        this.main_container = pane;
        game_running = true;
        enemies = new ArrayList<>();
        this.character = character;
        this.enemy_type_1 = background;
        this.enemy_type_2 = background2;
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

    public ImageView clone(ImageView to_clone) {
        ImageView imageView = new ImageView(to_clone.getImage());
        imageView.setFitHeight(to_clone.getFitHeight());
        imageView.setFitWidth(to_clone.getFitWidth());
        imageView.setRotate(180);
        return imageView;
    }

    public static void setPlayer(String player) {
        name = player;
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

//                if (tempValue == previousValue || tempValue == previousValue + 50 || tempValue == previousValue - 50) {
//                    do {
//                        tempValue =
//                        randomX = tempValue;
//                    } while (tempValue == previousValue || tempValue == previousValue + 50 || tempValue == previousValue - 50);
//                }
//                int bomberWidth = 20; // Assuming the width of the Bomber entity is 20 units
//                int x = (int) main_container.widthProperty().get();
                randomX = random.nextInt(50, 500 - 50);

                makeFleet("Strafer", 50, 3);
                makeFleet("Bomber",randomX , 1);

//                if(enemies.size() < 10) {
//                    long speed = (long)(random.nextDouble() * (50 - minimumSpeed) + minimumSpeed);
//                    double health = random.nextInt((int)minimumHealth);
//                    EnemyT1Strafer enemy = new EnemyT1Strafer(50, randomX, Color.BLUE,"Enemy");
//                    enemy.setAnchorPane(main_container);
//                    enemy.setHealth(health);
//                    Thread enemyThread = new Thread(enemy);
//                    Platform.runLater(()->{
//                        main_container.getChildren().add(enemy);
//                    });
//                    enemyThread.start();
//                }

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
            MySQLConnection.updatePlayerScore(name,score);
            Platform.runLater(()->{
                main_container.getChildren().removeAll();
                Stage stage = (Stage) character.getScene().getWindow();
                stage.close();
            });
        }


    }

    private void makeFleet(String enemyType, int startLocation, int countOfEnemies) {
        if (enemies.isEmpty()) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int count = 0;

                @Override
                public void run() {
                    double locationStart = startLocation;
                    if (count < countOfEnemies) {
                        final double initialLocation = locationStart;
                        Platform.runLater(() -> {
                            createEnemy(enemyType, initialLocation);
                        });
                        count++;
                    } else {
                        timer.cancel();
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 3000);
        }
    }

    private void createEnemy(String enemyType, double locationX) {
        Enemy enemy;
        ImageView enemy_character = null;
        switch (enemyType) {
            case "Strafer":
                enemy = new EnemyT1Strafer(locationX, 50, Color.BLUE, "Strafer");
                enemy_character = clone(enemy_type_1);
                break;
            case "Bomber":
                enemy = new EnemyT1Bomber(10, 20, locationX, 50, Color.RED, "Bomber");
                enemy_character = clone(enemy_type_2);
                break;
            // Add more cases for other enemy types if needed

            default:
                throw new IllegalArgumentException("Invalid enemy type: " + enemyType);
        }
        enemy.setEnemy(enemy_character);
        enemy.setAnchorPane(main_container);
        main_container.getChildren().add(enemy);
        Thread enemyThread = new Thread(enemy);
        enemyThread.start();
    }


}
