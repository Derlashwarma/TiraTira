package com.example.game.Levels;

import com.example.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

public class Level1 implements Initializable {
    @FXML
    public AnchorPane main_container;
    @FXML
    ImageView character;
    @FXML
    ImageView background;
    @FXML
    ImageView background2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Game game = new Game(main_container, character, background, background2);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}