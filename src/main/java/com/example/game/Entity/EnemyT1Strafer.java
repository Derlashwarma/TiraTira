package com.example.game.Entity;

import com.example.game.Bullets.EnemyBulletLevel1;
import com.example.game.Levels.BattleMaker;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.game.GameStart.sm;

public class EnemyT1Strafer extends Enemy{

    private ImageView projectile;
    public EnemyT1Strafer(long speed, double currentX, double currentY, Color color, String name, ImageView enemyProj) {
        super(speed, 40, 200 ,currentX, currentY, color, name, 2);
        setVisible(false);
        this.projectile = enemyProj;
    }
    @Override
    public void shoot(){
        if ((getCounter() % 100) == 0) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int count = 0;
                int limit = 3;
                @Override
                public void run() {
                    synchronized ((Object)BattleMaker.BattleScenario) {
                        if(BattleMaker.BattleScenario == 4) {
                            limit = 5;
                        }
                    }
                    if (count < limit) {
                        ImageView bulletClone = EnemyT1Strafer.this.clone(projectile);
                        bulletClone.setRotate(180);
                        EnemyBulletLevel1 bullet = new EnemyBulletLevel1(getLayoutX()-5, getLayoutY()+20, bulletClone);
//                        Platform.runLater(()->sm.playSound("Player"));
                        bullet.setPane(anchorPane);
                        bullet.setVisible(false);
                        bullet.setBullet(bulletClone);
                        Platform.runLater(() -> {
                            anchorPane.getChildren().addAll(bullet,bulletClone);
                        });
                        Thread enemyBullet = new Thread(bullet);
                        enemyBullet.start();
                        count++;
                    } else {
                        timer.cancel();
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 250);
        }
        setCounter(getCounter() + 1);
    }


    private static void changeDirection(boolean change){

    }

    @Override
    public void move() {
        int mov = getMovementPattern();
        if(mov == 0){
            double x = this.getRadius();

            if (getCurrentX() + x > 500) {
                setDirectionX(-1);
            }
            if (getCurrentX() - x < 0) {
                setDirectionX(1);
            }

            setCurrentX(getCurrentX() + getDirectionX());
            setCurrentY(getCurrentY() + 1);

            Platform.runLater(() -> {
                if(enemy != null) {
                    enemy.setLayoutX(getCurrentX()-50);
                    enemy.setLayoutY(getCurrentY()-40);
                }
                setLayoutX(getCurrentX());
                setLayoutY(getCurrentY());
            });
        } else if (mov == 1) {
            double x = this.getRadius();
            setDirectionY(0);

            if (getCurrentX() + x > 500) {
                setDirectionX(-1);
                setDirectionY(5);
            }
            if (getCurrentX() - x < 0) {
                setDirectionX(1);
                setDirectionY(5);
            }

            setCurrentX(getCurrentX() + getDirectionX());
            setCurrentY(getCurrentY() + getDirectionY());

            Platform.runLater(() -> {
                if(enemy != null) {
                    enemy.setLayoutX(getCurrentX()-50);
                    enemy.setLayoutY(getCurrentY()-40);
                }
                setLayoutX(getCurrentX());
                setLayoutY(getCurrentY());
            });
        }

    }

}
