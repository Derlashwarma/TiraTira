package com.example.game;

import com.example.game.Levels.Level1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class GameStart extends Application {

    private static String playerName;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameStart.class.getResource("first-level-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Space Horizon!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
        if (iconStream == null) {
            throw new RuntimeException("Icon resource not found");
        }
        Image icon = new Image(iconStream);
        stage.getIcons().add(icon);
        stage.centerOnScreen();
    }

    public GameStart(String playerName) {
        GameStart.playerName = playerName;
    }

    public static String getPlayerName() {
        return playerName;
    }

    public static void main(String[] args) {
//        launch();
    }
}