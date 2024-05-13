package com.example.game.Entity;

import com.example.game.Bullets.EnemyBulletLevel1;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Timer;
import java.util.TimerTask;

public class EnemyT1Strafer extends Enemy{
    public EnemyT1Strafer(double currentX, double currentY, Color color, String name) {
        super(50, 10, 200 ,currentX, currentY, color, name);
    }
    @Override
    public void shoot(){
        if ((getCounter() % 100) == 0) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int count = 0;

                @Override
                public void run() {
                    if (count < 3) {
                        EnemyBulletLevel1 bullet = new EnemyBulletLevel1(getLayoutX(), getLayoutY());
                        bullet.setPane(anchorPane);
                        Platform.runLater(() -> {
                            anchorPane.getChildren().add(bullet);
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
        int width = (int) anchorPane.widthProperty().get();
        double x = this.getRadius();

        if (getCurrentX() + x > width) {
            setDirectionX(-1);
        }
        if (getCurrentX() - x < 0) {
            setDirectionX(1);
        }

        setCurrentX(getCurrentX() + getDirectionX());
        setCurrentY(getCurrentY() + 1);

        Platform.runLater(() -> {
            setLayoutX(getCurrentX());
            setLayoutY(getCurrentY());
        });
    }

}
