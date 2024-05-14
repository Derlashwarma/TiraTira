package com.example.game;

import com.example.game.Bullets.EnemyBulletLevel1;
import com.example.game.Entity.Enemy;
import com.example.game.Entity.EnemyT1Bomber;
import com.example.game.Entity.EnemyT1Strafer;
import com.example.game.Entity.Player;
import com.example.game.Levels.BattleMaker;
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
    public static int size;


    public Game(AnchorPane pane, ImageView character, ImageView background, ImageView background2) {
        main_container = pane;
        game_running = true;
        enemies = new ArrayList<>();
        this.character = character;
        this.enemy_type_1 = background;
        this.enemy_type_2 = background2;
        size = 0;
    }
    public static void addScore(int sc){
        score += sc;
    }
    public static void endGame(){
        game_running = false;
        Platform.runLater(() -> {
            Stage currentStage = (Stage) main_container.getScene().getWindow();
            currentStage.close();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("game_over.fxml"));
                Parent root = fxmlLoader.load();
                GameOver gameOverController = fxmlLoader.getController();
                gameOverController.setTotalScore(score);
                gameOverController.setPlayerName(player.getName() + "'S SCORE:");
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getScene().getStylesheets().add(Game.class.getResource("menu_styles.css").toExternalForm());
                InputStream iconStream = Game.class.getResourceAsStream("/com/example/images/game_icon2.png");
                if (iconStream == null) {
                    throw new RuntimeException("Icon resource not found");
                }
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
                stage.setTitle("Game Over!");
                stage.show();
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public synchronized static void addEnemy(Runnable enemy){
        enemies.add(enemy);
        size++;
    }
    public synchronized static void removeEnemy(Runnable enemy){
        enemies.remove(enemy);
        size--;
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
            BattleMaker bm = new BattleMaker(main_container, enemy_type_1, enemy_type_2);
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
            Thread bmThread = new Thread(bm);
            bmThread.start();
            while(game_running) {
                bm.setScore(score);
                try {
                    Thread.sleep(interval);
                    if(interval > 50) {
                        interval -= 10;
                    }
                    minimumHealth += 10;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized ((Object)BattleMaker.isActiveCount){
                    BattleMaker.isActiveCount = enemies.isEmpty();
                }
            }
            System.out.println("GAME OVER");
            System.out.println("TOTAL SCORE: " + score);
            MySQLConnection.updatePlayerScore(name,score);

            Platform.runLater(() -> {
                Stage stage = (Stage) character.getScene().getWindow();
                stage.close();
            });
    }


}