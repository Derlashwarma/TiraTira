package com.example.game.PowerUps;

import javafx.scene.paint.Color;

public class PowerUpLevel2 extends PowerUp{
    public PowerUpLevel2(int currentX, int currentY) {
        super(Color.ORANGE,currentX, currentY);
        setPowerUp("playerBulletLevel3");
    }
}
