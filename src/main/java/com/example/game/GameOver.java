package com.example.game;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameOver extends Application {

    public Label totalScoreText;
    public Label playerNameLabel;
    private String playerName;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(GameOver.class.getResource("game_over.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());
        stage.setTitle("Game Over");
        stage.setScene(scene);
        stage.show();
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

        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("main_menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());

        Main_Menu mainMenuController = fxmlLoader.getController();
        mainMenuController.setPlayerName(playerName);

        mainMenuController.startNewGame();
    }


    public void exit(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("main_menu.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());

            Stage mainMenuStage = new Stage();
            mainMenuStage.setScene(scene);
            mainMenuStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
