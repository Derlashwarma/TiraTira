package com.example.game.Bullets;

import com.example.game.Entity.Enemy;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class PlayerBullet extends Bullet{
    private double speed;
    private double currentX;
    private double currentY;
    private final double BOTTOM_LIMIT = 760;
    private final double TOP_LIMIT = 10;
    private int direction;
    private double damage;
    private String name;
    private static final Object lock = new Object();

    private AnchorPane pane;
    public PlayerBullet(double damage, double speed, double v, double v1, double currentX, double currentY, Paint paint, int direction, String name) {
        super(damage,speed,v,v1,currentX,currentY,paint,direction,name);
    }
    public void setAnchorPane(AnchorPane pane){
        setPane(pane);
    }
}
