package com.example.game.Entity;

import javafx.application.Platform;
import javafx.scene.paint.Color;

public class EnemyT1Bomber extends Enemy{
    public EnemyT1Bomber(long speed, double size, double currentX, double currentY, Color color, String name) {
        super(speed, size, currentX, currentY, color, name);
    }

    @Override
    public void move() {
        int width = (int) anchorPane.widthProperty().get();
        Platform.runLater(() -> {
            setLayoutX(this.getCurrentX());
            setLayoutY(this.getCurrentY());
        });
        double x = this.getRadius();

        if (this.getCurrentX()+x> width) {
            setDirectionX(-1);
        }
        if (this.getCurrentY()-x < 0) {
            setDirectionX(1);
        }
    }
}
