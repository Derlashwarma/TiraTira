package com.example.game.PowerUps;

import com.example.game.Entity.Player;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.awt.*;

import static com.example.game.Game.player;

public abstract class PowerUp  extends javafx.scene.shape.Circle implements Runnable{
    private String powerUp;
    protected int currentX;
    private int currentY;
    private int direction;
    private int xDirection;
    private ImageView powerUpImage;
    protected final double BOTTOM_LIMIT = 760;
    protected final double TOP_LIMIT = 10;
    private int speed;
    private AnchorPane pane;

    public PowerUp(Color color, int currentX, int currentY) {
        super(40, color);
        this.currentX = currentX;
        this.currentY = currentY;
        this.direction = 1;
        this.xDirection = 0;
        this.powerUp = null;
//        setVisible(false);
        this.speed = 10;
    }

    public AnchorPane getPane() {
        return pane;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    public void setPowerUp(String powerUp) {
        this.powerUp = powerUp;
    }
    public String getPowerUp() {
        return powerUp;
    }
    public void setXDirection(int xDirection) {
        this.xDirection = xDirection;
    }
    protected void move(){
        Platform.runLater(()->{
            if(powerUpImage != null) {
                powerUpImage.setLayoutY(currentY);
                powerUpImage.setLayoutX(currentX);
            }
            setLayoutX(currentX);
            setLayoutY(currentY);
        });
        currentX += xDirection;
        currentY += direction;
    }

    public ImageView getPowerUpImage() {
        return powerUpImage;
    }

    public void setPowerUpImage(ImageView powerUpImage) {
        this.powerUpImage = powerUpImage;
    }

    private boolean checkCollision() {
        if (getBoundsInParent().intersects(player.getBoundsInParent())) {
            Player.setPowerup(getPowerUp());
            Platform.runLater(() -> pane.getChildren().remove(this));
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while(currentY < BOTTOM_LIMIT && currentY > TOP_LIMIT && !checkCollision() ) {
            try {
                move();
                Thread.sleep((long)speed);
            } catch (Exception e) {
                break;
            }
        }

        Platform.runLater(() ->{
            pane.getChildren().remove(this);
            pane.getChildren().remove(powerUpImage);
            Thread.currentThread().interrupt();
        });
    }
    public double getCurrentX() {
        return currentX;
    }
    public double getCurrentY() {
        return currentY;
    }
}
