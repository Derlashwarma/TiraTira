package com.example.game.Levels;

import com.example.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Level1 implements Initializable {
    @FXML
    public AnchorPane main_container;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread gameThread = new Thread(new Game(main_container));
        gameThread.start();
    }
}