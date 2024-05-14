package com.example.game.Levels;
import com.example.game.Entity.Enemy;
import com.example.game.Entity.EnemyT1Bomber;
import com.example.game.Entity.EnemyT1Strafer;
import com.example.game.Game;
import com.example.game.Sound.SoundManager;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class BattleMaker implements Runnable {
    SoundManager sm = new SoundManager();
    private final AnchorPane mainContainer;
    private final ImageView enemyType1;
    private final ImageView enemyType2;

    public void setScore(int score) {
        this.score = score;
    }

    private volatile int score;
    private int battleScenario;
    public static volatile boolean isActiveCount = true;
    private Timer currentTimer;
    private final Object timerLock = new Object();

    public BattleMaker(AnchorPane mainContainer, ImageView enemyType1, ImageView enemyType2) {
        this.mainContainer = mainContainer;
        this.enemyType1 = enemyType1;
        this.enemyType2 = enemyType2;
    }

    @Override
    public void run() {
        while (Game.game_running) {
            determineBattleScenario();
            if (isActiveCount) {
                synchronized (timerLock) {
                    if (currentTimer != null) {
                        currentTimer.cancel();
                    }
                    currentTimer = new Timer();
                    switch (battleScenario) {
                        case 1:
                            makeFleet("StraferV1", 500, 0, 5, 8000, 1000,currentTimer);
                            makeFleet("StraferV1", 0 , 50 ,5, 5000, 2000,currentTimer);
                            makeFleet("Bomber", -1 , 0,10, 6000, 2500,currentTimer);
                            makeFleet("StraferV2", -1 , 0,10, 4000, 10000,currentTimer);
                            makeFleet("StraferV2", -1 , 20,5, 2000, 5000,currentTimer);
                            break;
                        case 2:
                            // Add scenario 2 logic here
                            break;
                        // Add more scenarios if needed
                    }
                }
            }
            checkGameState();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void determineBattleScenario() {
        if (score < 100) {
            battleScenario = 1;
        } else if (score < 200) {
            battleScenario = 2;
        } else if (score < 300) {
            battleScenario = 3;
        }
    }

    private void checkGameState() {
        if (Game.size == 0) {
            isActiveCount = true;
        }
    }

    private void makeFleet(String enemyType, int startLocationX, int startLocationY , int countOfEnemies, int spawnTime, int actionTime, Timer timer) {
        TimerTask task = new TimerTask() {
            int count = 0;

            @Override
            public void run() {

                if (count < countOfEnemies) {
                    Platform.runLater(() -> {
                        double startX = (double) startLocationX;
                        if (startX < 0) {
                            Random rand = new Random();
                            startX = (rand.nextInt(12) + 1) * 40;
                        }
                        createEnemy(enemyType, startX, startLocationY);
                        System.out.println("1");
                        count++;
                    });
                } else {
                    timer.cancel();
                }
//                System.out.println("Check");
            }
        };
        timer.scheduleAtFixedRate(task, actionTime, spawnTime);
    }

    private void createEnemy(String enemyType, double locationX, double locationY) {
        Enemy enemy = EnemyFactory.createEnemy(enemyType, locationX, locationY,enemyType1, enemyType2);
        if (enemy != null) {
            enemy.setAnchorPane(mainContainer);
            Game.addEnemy(enemy);
            mainContainer.getChildren().add(enemy);
            new Thread(enemy).start();
        }
    }
}

class EnemyFactory {
    public static Enemy createEnemy(String enemyType, double locationX, double locationY, ImageView enemyType1, ImageView enemyType2) {
        Enemy enemy;
        ImageView enemyCharacter;
        switch (enemyType) {
            case "StraferV1":
                enemy = new EnemyT1Strafer(20, locationX, locationY, Color.BLUE, "Strafer");
                enemy.setMovementPattern(1);
                enemyCharacter = cloneImageView(enemyType1);
                break;
            case "StraferV2":
                enemy = new EnemyT1Strafer(10, locationX, locationY, Color.BLUE, "Strafer");
                enemyCharacter = cloneImageView(enemyType1);
                break;
            case "Bomber":
                enemy = new EnemyT1Bomber(10, 20, locationX, locationY, Color.RED, "Bomber");
                enemyCharacter = cloneImageView(enemyType2);
                break;
            default:
                throw new IllegalArgumentException("Invalid enemy type: " + enemyType);
        }
        enemy.setEnemy(enemyCharacter);
        return enemy;
    }

    private static ImageView cloneImageView(ImageView toClone) {
        ImageView imageView = new ImageView(toClone.getImage());
        imageView.setFitHeight(toClone.getFitHeight());
        imageView.setFitWidth(toClone.getFitWidth());
        imageView.setRotate(180);
        return imageView;
    }
}
