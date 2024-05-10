package com.example.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Main_Menu extends Application {

    public TextField playerNameInput;
    public Label userMessageLabel;


    public void initialize() {
            playerNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            playerNameInput.setText(newValue.toUpperCase());
        });

        playerNameInput.setOnMouseClicked(event -> userMessageLabel.setText(""));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("main_menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());
        stage.setTitle("Space Horizon");
        stage.setScene(scene);
        stage.show();

        MySQLConnection.createPlayerTable();
    }

    public void startGame() {
        String playerName = playerNameInput.getText();
        if (playerName.isEmpty()) {
            userMessageLabel.setText("Please enter your username.");
            return;
        }

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM player WHERE username = ?");
             PreparedStatement insertStatement = connection.prepareStatement(
                     "INSERT INTO player (username) VALUES (?)")) {

            checkStatement.setString(1, playerName);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next(); // Move cursor to the first row
            int count = resultSet.getInt(1);

            if (count > 0) {
                userMessageLabel.setText("Username is already taken");
            } else {
                insertStatement.setString(1, playerName);
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Player " + playerName + " added to the database.");
                    // Start the game
                    try {
                        Game.setPlayer(playerName);
                        GameStart gameStart = new GameStart();
                        gameStart.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Starting the game...");
                    Stage stage = (Stage) playerNameInput.getScene().getWindow();
                    stage.close();
                } else {
                    System.out.println("Registration failed!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    public void showGuide() {
        // Add logic to show the guide
        System.out.println("Showing the guide...");
    }

    public void showHighScores() {
        // Add logic to show high scores
        System.out.println("Showing high scores...");
    }

//    public static void main(String[] args) {
////        launch(args);
//    }
}
