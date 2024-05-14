package com.example.game.Entity;

import com.example.game.Sound.SoundManager;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class EnemyT1Bomber extends Enemy{

    public EnemyT1Bomber(long speed, double size, double currentX, double currentY, Color color, String name) {
        super(80, 40, 300 ,currentX, currentY, color, name);
        setVisible(false);
    }
    // hello
    @Override
    public void move() {
        Platform.runLater(() -> {
            if(enemy != null) {
                enemy.setLayoutX(getCurrentX()-50);
                enemy.setLayoutY(getCurrentY()-40);
            }
            setLayoutX(getCurrentX()); // Update the layout X position
            setLayoutY(getCurrentY()); // Update the layout Y position
        });
        setCurrentY(getCurrentY() + 1); // Increment Y position
//        if (getCurrentX() + x > 500) {
//            setDirectionX(-1);
//        }
//        if (getCurrentX() - x < 0) {
//            setDirectionX(1);
//            setDirectionY(5);
//        }
    }

}
