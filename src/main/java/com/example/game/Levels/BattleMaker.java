package com.example.game.Levels;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.EnemyT1Bomber;
import com.example.game.Entity.EnemyT1Strafer;
import com.example.game.Game;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.*;

public class BattleMaker implements Runnable {
    private final AnchorPane mainContainer;
    private final ImageView enemyType1;
    private final ImageView enemyType2;
    public void setScore(int score) {
        this.score = score;
    }

    private int score;

    private int BattleScenario;

    private ImageView projectile;
    public static boolean isActiveCount;


    public BattleMaker(AnchorPane mainContainer, ImageView enemyType1, ImageView enemyType2) {
        this.mainContainer = mainContainer;
        this.enemyType1 = enemyType1;
        this.enemyType2 = enemyType2;
        isActiveCount = true;
    }

    @Override
    public void run() {
        while(Game.game_running){
            if(score < 100){
                BattleScenario = 1;
            }
            else if(score < 200){
                BattleScenario = 2;
            } else if (score < 300) {
                BattleScenario = 3;
            }
            if (BattleScenario == 1 && isActiveCount) {
                makeFleet("Bomber", -1, 3, 4000);
//                    makeFleet("StraferV1", 0 , 5, 1500);
//                    makeFleet("StraferV1", 500 , 5, 2500);
//                    makeFleet("StraferV2", 500 , 5, 6000);
            } else if (BattleScenario == 2 && isActiveCount) {
                makeFleet("StraferV1", 0 , 5, 1500);
            }
            System.out.println("Size: " + Game.size);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(Game.size == 0){
                isActiveCount = true;
            }
        }

    }

    private void makeFleet(String enemyType, int startLocation, int countOfEnemies, int spawnTime) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                if (count < countOfEnemies) {
                    Platform.runLater(() -> {
                        double start = (double) startLocation;
                        if (start < 0) {
                            Random rand = new Random();
                            start = rand.nextInt(460) + 40; // Generate a random start location
                        }
                        createEnemy(enemyType, start);
                    });
                    count++;
                } else {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 100, spawnTime);
    }


    private void createEnemy(String enemyType, double locationX) {
        Enemy enemy;
        ImageView enemyCharacter = null;
        switch (enemyType) {
            // Fast movement on x-axis but slow on y-axis
            case "StraferV1":
                enemy = new EnemyT1Strafer(20,locationX, 50, Color.BLUE, "Strafer", projectile);
                enemy.setMovementPattern(1);
                enemyCharacter = clone(enemyType1);
                break;
            // Fast movement on x-axis and y-axis
            case "StraferV2":
                enemy = new EnemyT1Strafer(10,locationX, 50, Color.BLUE, "Strafer", projectile);
                enemyCharacter = clone(enemyType1);
                break;
            case "Bomber":
                enemy = new EnemyT1Bomber(10, 20, locationX, 50, Color.RED, "Bomber", projectile);
                enemyCharacter = clone(enemyType2);
                break;
            // Add more cases for other enemy types if needed
            default:
                throw new IllegalArgumentException("Invalid enemy type: " + enemyType);
        }
        enemy.setEnemy(enemyCharacter);
        enemy.setAnchorPane(mainContainer);
        Game.addEnemy(enemy);
        mainContainer.getChildren().add(enemy);
        Thread enemyThread = new Thread(enemy);
        enemyThread.start();
    }

    private ImageView clone(ImageView toClone) {
        ImageView imageView = new ImageView(toClone.getImage());
        imageView.setFitHeight(toClone.getFitHeight());
        imageView.setFitWidth(toClone.getFitWidth());
        imageView.setRotate(180);
        return imageView;
    }
}
