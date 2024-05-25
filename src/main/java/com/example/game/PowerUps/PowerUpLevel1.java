package com.example.game.PowerUps;

import javafx.scene.paint.Color;

import java.awt.*;

public class PowerUpLevel1 extends PowerUp{
    public PowerUpLevel1(int currentX, int currentY) {
        super(Color.GREEN, currentX, currentY);
        setPowerUp("playerBulletLevel2");
        setVisible(false);
    }
}
