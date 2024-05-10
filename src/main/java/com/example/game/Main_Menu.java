package com.example.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Main_Menu extends Application {

    public TextField playerNameInput;
    public TextField newPlayerNameInput;
    public Label userMessageLabel;

    public void initialize() {
        playerNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            playerNameInput.setText(newValue.toUpperCase());
        });

        playerNameInput.setOnMouseClicked(event -> {
            userMessageLabel.setText("");
            newPlayerNameInput.setText(""); // Reset newPlayerNameInput
        });

        newPlayerNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            newPlayerNameInput.setText(newValue.toUpperCase());
        });

        newPlayerNameInput.setOnMouseClicked(event -> {
            userMessageLabel.setText("");
            playerNameInput.setText(""); // Reset playerNameInput
        });
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
//        populateUserMenu();
    }

    public void startGame() {
        String playerName = playerNameInput.getText();
        String newPlayerName = newPlayerNameInput.getText();

        // If both fields are not empty, show an error message and return
        if (!playerName.isEmpty() && !newPlayerName.isEmpty()) {
            userMessageLabel.setText("Enter player name or create new one");
            return;
        }

        // If newPlayerName is not empty, the user wants to register a new username
        if (!newPlayerName.isEmpty()) {
            try (Connection connection = MySQLConnection.getConnection();
                 PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM player WHERE username = ?")) {

                checkStatement.setString(1, newPlayerName);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next(); // Move cursor to the first row
                int count = resultSet.getInt(1);

                if (count > 0) {
                    userMessageLabel.setText("Player name is already taken");
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO player (username) VALUES (?)")) {
                        insertStatement.setString(1, newPlayerName);
                        int rowsInserted = insertStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Player " + newPlayerName + " added to the database.");

                            // Start the game
                            try {
                                GameStart gameStart = new GameStart();
                                gameStart.start(new Stage());
                                System.out.println("Starting the game...");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Registration failed!");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (!playerName.isEmpty()) {
            // If playerName is not empty, check if it exists in the database
            try (Connection connection = MySQLConnection.getConnection();
                 PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM player WHERE username = ?")) {

                checkStatement.setString(1, playerName);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next(); // Move cursor to the first row
                int count = resultSet.getInt(1);

                if (count > 0) {
                    // If the username exists, start the game
                    try {
                        GameStart gameStart = new GameStart();
                        gameStart.start(new Stage());
                        System.out.println("Starting the game...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // If the username doesn't exist, show an error message
                    userMessageLabel.setText("Player name does not exist. Enter a new player name.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // If both fields are empty, show an error message
            userMessageLabel.setText("Please enter a username.");
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

    public static void main(String[] args) {
        launch(args);
    }
}
