package com.example.game.Bullets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class BulletView extends ImageView {
    private Bullet bullet;

    public BulletView(Bullet bullet, Image image) {
        super(image);
        this.bullet = bullet;
    }

    public void update() {
        setLayoutX(bullet.getCurrentX());
        setLayoutY(bullet.getCurrentY());
    }

    public void setPane(AnchorPane pane) {
        pane.getChildren().add(this);
    }
}
