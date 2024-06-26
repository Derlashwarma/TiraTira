package com.example.game.Levels;

import com.example.game.Game;
import com.example.game.GameStart;
import com.example.game.Main_Menu;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.game.GameStart.sm;

public class Level1 implements Initializable {
    @FXML
    public AnchorPane main_container;
    @FXML
    ImageView character;
    @FXML
    ImageView background;
    @FXML
    ImageView background2;
    @FXML
    ImageView playerProd;
    @FXML
    ImageView enemyProd;
    @FXML
    ImageView powerUP1;
    @FXML
    ImageView powerUP2;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Game game = new Game(main_container, character, background, background2, playerProd, enemyProd, Main_Menu.getName(), powerUP1, powerUP2);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}