package com.example.game.Entity;

import com.example.game.Bullets.EnemyBulletLevel1;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Random;

public class Entity extends javafx.scene.shape.Circle{

    // Main attributes of Entity
    private double health;

    private long speed;

    private boolean isAlive;

    // Attributes  used for determining position

    private double currentX;
    private double currentY;
    protected AnchorPane anchorPane;
    protected String name;
    private int directionX;
    private int directionY;

    private int direction;
    protected ImageView enemy;
    protected ImageView second_form;

    protected ImageView enemyProj;

    private int counter = 0;

    // Constructors takes all basic that makes up an Plane

    public Entity(double size, long speed, double health, Color color, String name,ImageView enemyProjectile){
        super(size,Color.WHITE);
        this.speed = speed;
        this.health = health;
        this.name = name;
        isAlive = true;
        this.enemyProj = enemyProjectile;
    }
    public void setEnemy(ImageView enemy) {
        this.enemy = enemy;
        if(this.enemy != null){
            addEnemy();
        }
    }
    public void addEnemy(){
        Platform.runLater(()->{
            anchorPane.getChildren().add(enemy);
        });
    }
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }
    public String getName(){return name;}

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public synchronized void changeHealth(double amount) {
        health += amount;
        if(health <= 0) {
            setAlive(false);
        }
    }
    public synchronized double getHealth() {
        return health;
    }

    public synchronized void setHealth(double health) {
        this.health = health;
    }

    public double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX = currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY = currentY;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }


    // Main function of a plane

    public void move() {
        setCurrentY(getCurrentY() + 1);
        Platform.runLater(() -> {
            if(enemy != null){
                enemy.setLayoutX(currentX-50);
                enemy.setLayoutY(currentY-40);
                second_form.setLayoutX(currentX-50);
                second_form.setLayoutY(currentY-40);
            }
            setLayoutX(currentX);
            setLayoutY(currentY);
        });
    }

    private ImageView clone(ImageView to_clone) {
        ImageView imageView = new ImageView(to_clone.getImage());
        imageView.setFitHeight(to_clone.getFitHeight());
        imageView.setFitWidth(to_clone.getFitWidth());
        return imageView;
    }

    // Adjusted shoot method
    public void shoot() {
        if ((counter % 100) == 0) { // Fixed speed parameter to 100
            EnemyBulletLevel1 bullet = new EnemyBulletLevel1(getLayoutX(), getLayoutY());
            bullet.setPane(anchorPane);
            bullet.setVisible(false);
            ImageView clone = clone(enemyProj);
            bullet.setBullet(clone);
            Platform.runLater(() -> {
                anchorPane.getChildren().addAll(bullet, clone);
            });
            Thread enemyBullet = new Thread(bullet);
            enemyBullet.start();
        }
        counter++;
    }



}
