package com.example.game.Entity;

import javafx.application.Platform;
import javafx.scene.paint.Color;

public class EnemyT1Bomber extends Enemy{
    public EnemyT1Bomber(long speed, double size, double currentX, double currentY, Color color, String name) {
        super(80, 20, 300 ,currentX, currentY, color, name);
    }
    // hello
    @Override
    public void move() {
        Platform.runLater(() -> {
            setLayoutX(getCurrentX()); // Update the layout X position
            setLayoutY(getCurrentY()); // Update the layout Y position
        });
        setCurrentY(getCurrentY() + 1); // Increment Y position
    }

}
