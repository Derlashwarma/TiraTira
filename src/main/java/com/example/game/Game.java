package com.example.game;

import com.example.game.Bullets.EnemyBulletLevel1;
import com.example.game.Entity.Enemy;
import com.example.game.Entity.EnemyT1Bomber;
import com.example.game.Entity.EnemyT1Strafer;
import com.example.game.Entity.Player;
import com.example.game.Levels.BattleMaker;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;

import static com.example.game.GameStart.sm;

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
    private ImageView playerBullet;
    private ImageView enemyBullet;
    private ImageView powerUP1;
    private ImageView powerUP2;
    public static int score;
    public static Player player;
    private static String name;
    public static int size;
    public static ProgressBar healthBar;
    private static ImageView icon;

    public Game(AnchorPane pane, ImageView character, ImageView background, ImageView background2,
                ImageView playerProd, ImageView enemyProd, String playerName,
                ImageView powerup1, ImageView powerup2) {
        main_container = pane;
        game_running = true;
        enemies = new ArrayList<>();
        this.character = character;
        this.enemy_type_1 = background;
        this.enemy_type_2 = background2;
        this.playerBullet = playerProd;
        this.enemyBullet = enemyProd;
        this.powerUP1 = powerup1;
        this.powerUP2 = powerup2;
        size = 0;
        Game.name = playerName;
        Game.score = 0;
    }
    public static void addScore(int sc){
        score += sc;
    }

    public static void end() {
        game_running = false;
    }
    public static void endGame(){
        Platform.runLater(() -> {
            Stage currentStage = (Stage) main_container.getScene().getWindow();
            currentStage.close();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("game_over.fxml"));
                Parent root = fxmlLoader.load();
                GameOver gameOverController = fxmlLoader.getController();
                gameOverController.setTotalScore(score);
                GameOver.putPlayerName(name);
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
        Platform.runLater(Game::bringHealthBarToFront);
    }
    public synchronized static void removeEnemy(Runnable enemy){
        enemies.remove(enemy);
        size--;
        Platform.runLater(Game::bringHealthBarToFront);
    }

    public ImageView clone(ImageView to_clone) {
        ImageView imageView = new ImageView(to_clone.getImage());
        imageView.setFitHeight(to_clone.getFitHeight());
        imageView.setFitWidth(to_clone.getFitWidth());
        imageView.setRotate(180);
        return imageView;
    }
    public static int getSize() {
        return size;
    }
    public static void setPlayer(String player) {
        name = player;
    }

    @Override
    public void run() {
            BattleMaker bm = new BattleMaker(main_container, enemy_type_1, enemy_type_2,
                                             enemyBullet, powerUP1, powerUP2);
            player = new Player(20, Color.GREEN, Main_Menu.getName(), playerBullet);
            player.setAnchorPane(main_container);
            player.setAnchorPane(main_container);
            Thread playerThread = new Thread(player);
            playerThread.start();
            addProgressBar();
            main_container.setOnMouseMoved(event -> {
                character.setLayoutX(event.getX()-30);
                character.setLayoutY(event.getY()-50);
                player.setLayoutX(event.getX());
                player.setLayoutY(event.getY());
                player.setCurrentX(event.getX());
                player.setCurrentY(event.getY());
            });
            Platform.runLater(()->{
                Thread thread = new Thread(new GameSound());
                thread.start();
            });

            Thread bmThread = new Thread(bm);
            bmThread.start();
            double health = 100;
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
            sm.stopAllSounds();
            Platform.runLater(() -> {
                MySQLConnection.updatePlayerScore(name,score);
                Stage stage = (Stage) character.getScene().getWindow();
                main_container.getChildren().clear();
                stage.close();
                endGame();
            });
    }

    private void addProgressBar() {
        Platform.runLater(() -> {
            icon = new ImageView();
            InputStream iconStream = getClass().getResourceAsStream("/com/example/images/playerShip_A.png");
            if (iconStream != null) {
                Image iconImage = new Image(iconStream);
                icon.setImage(iconImage);
                icon.setFitHeight(55);
                icon.setFitWidth(40);
            } else {
                System.out.println("Icon resource not found");
            }

            healthBar = new ProgressBar();
            healthBar.setProgress(1);
            healthBar.getStyleClass().add("progress-bar");



            HBox hbox = new HBox(-230);
            hbox.setLayoutX(40);
            hbox.setLayoutY(20);
            hbox.getChildren().addAll(icon, healthBar);
            hbox.setAlignment(Pos.CENTER_LEFT);

            main_container.getChildren().add(hbox);
            icon.toFront();

            Scene scene = main_container.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/com/example/game/menu_styles.css").toExternalForm());
            }
        });
    }

    private static void bringHealthBarToFront() {
        if (healthBar != null && icon != null) {
            HBox hbox = (HBox) healthBar.getParent();
            if (hbox != null) {
                hbox.toFront();
            }
        }
    }

    public static synchronized void reduceProgress(double damage){
        double currHealth = healthBar.getProgress() - damage;
        Platform.runLater(()->healthBar.setProgress(currHealth));
        if(healthBar.getProgress() <= .3) {
            healthBar.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        }
        else if(healthBar.getProgress() <= .7) {
            healthBar.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
        }
    }
    private class GameSound implements Runnable {
        @Override
        public void run() {

            sm.loopSound("Space");
            while(game_running) {
            }
            Thread.currentThread().interrupt();
        }
    }
}