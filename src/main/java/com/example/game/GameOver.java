package com.example.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class GameOver extends Application {

    public Label totalScoreText;
    public Label playerNameLabel;
    private static String playerName;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(GameOver.class.getResource("game_over.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Game Over");
        InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
        if (iconStream == null) {
            throw new RuntimeException("Icon resource not found");
        }
        Image icon = new Image(iconStream);
        stage.getIcons().add(icon);
        stage.show();
        stage.centerOnScreen();

    }

    public static void putPlayerName(String name) {
        playerName = name;
    }

    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName); // Set the player's name in the label
    }

    public void setTotalScore(int score) {
        totalScoreText.setText(String.valueOf(score)); // Set the player's score in the label
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void tryAgain(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
        Game.score = 0;
        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("main_menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Main_Menu mainMenuController = fxmlLoader.getController();
        mainMenuController.setPlayerName(playerName);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());

        InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
        if (iconStream == null) {
            throw new RuntimeException("Icon resource not found");
        }
        Image icon = new Image(iconStream);
        currentStage.getIcons().add(icon);
        currentStage.centerOnScreen();

        Platform.runLater(()->{
            mainMenuController.startNewGame();
        });
    }


    public void exit(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
        try {
            Main_Menu mainMenu = new Main_Menu();
            mainMenu.start(currentStage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
