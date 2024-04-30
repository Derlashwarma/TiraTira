package com.example.game.Levels;

import com.example.game.Bullets.Bullet;
import com.example.game.Entity.Enemy;
import com.example.game.Entity.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.w3c.dom.Entity;

import java.net.URL;
import java.util.ResourceBundle;

public class Level1 implements Initializable {
    @FXML
    public AnchorPane main_container;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Player player = new Player(20, Color.GREEN,"Ardel");
        player.setAnchorPane(main_container);
        Thread playerThread = new Thread(player);

        main_container.getChildren().add(player);
        playerThread.start();

        main_container.setOnMouseMoved(event -> {
            player.setLayoutX(event.getX());
            player.setLayoutY(event.getY());
            player.setCurrentX(event.getX());
            player.setCurrentY(event.getY());
        });

        Enemy enemy = new Enemy(10,30,200,0,Color.BLUE,"ENEMY");
        enemy.setAnchorPane(main_container);

        Thread enemyThread = new Thread(enemy);
        main_container.getChildren().add(enemy);
        enemyThread.start();

        Enemy enemy2 = new Enemy(20,30,20,0,Color.BLUE,"ENEMY");
        enemy2.setAnchorPane(main_container);

        Thread enemyThread2 = new Thread(enemy2);
        main_container.getChildren().add(enemy2);
        enemyThread2.start();

        Enemy enemy3 = new Enemy(40,30,50,0,Color.BLUE,"ENEMY");
        enemy3.setAnchorPane(main_container);

        Thread enemyThread3 = new Thread(enemy3);
        main_container.getChildren().add(enemy3);
        enemyThread3.start();

        Enemy enemy4 = new Enemy(60,30,300,0,Color.BLUE,"ENEMY");
        enemy4.setAnchorPane(main_container);

        Thread enemyThread4 = new Thread(enemy4);
        main_container.getChildren().add(enemy4);
        enemyThread4.start();

        Enemy enemy5 = new Enemy(10,30,200,0,Color.BLUE,"ENEMY");
        enemy5.setAnchorPane(main_container);

        Thread enemyThread5 = new Thread(enemy5);
        main_container.getChildren().add(enemy5);
        enemyThread5.start();
    }
}