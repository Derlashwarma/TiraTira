package com.example.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static java.awt.SystemColor.window;

public class Main_Menu extends Application {

    public TextField playerNameInput;
    public TextField newPlayerNameInput;
    public Label userMessageLabel;
    private Stage currentStage;
//    ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("        window.setIconImage(icon.getImage());")));

    public void initialize() {
        playerNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            playerNameInput.setText(newValue.toUpperCase());
        });

        playerNameInput.setOnMouseClicked(event -> {
            userMessageLabel.setText("");
            newPlayerNameInput.setText("");
        });

        newPlayerNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            newPlayerNameInput.setText(newValue.toUpperCase());
        });

        newPlayerNameInput.setOnMouseClicked(event -> {
            userMessageLabel.setText("");
            playerNameInput.setText("");
        });
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("main_menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());

        InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
        if (iconStream == null) {
            throw new RuntimeException("Icon resource not found");
        }
        Image icon = new Image(iconStream);
        primaryStage.getIcons().add(icon);

        primaryStage.setTitle("Space Horizon");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        MySQLConnection.createPlayerTable();
    }


    public void startGame() {
        String playerName = playerNameInput.getText();
        String newPlayerName = newPlayerNameInput.getText();

        if (!playerName.isEmpty() && !newPlayerName.isEmpty()) {
            userMessageLabel.setText("Enter player name or create a new one.");
            return;
        }

        if (!newPlayerName.isEmpty()) {
            if (isMaxPlayersReached()) {
                userMessageLabel.setText("Maximum number of players reached. Please remove a player.");
                return;
            }
            try (Connection connection = MySQLConnection.getConnection();
                 PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM player WHERE username = ?")) {

                checkStatement.setString(1, newPlayerName);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next(); // Move cursor to the first row
                int count = resultSet.getInt(1);

                if (count > 0) {
                    userMessageLabel.setText("Player name is already taken.");
                } else {
                    try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO player (username) VALUES (?)")) {
                        insertStatement.setString(1, newPlayerName);
                        int rowsInserted = insertStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Player " + newPlayerName + " added to the database.");

                            try {
                                GameStart gameStart = new GameStart(newPlayerName);
                                gameStart.start(new Stage());
                                System.out.println("Starting the game...");
                                currentStage = (Stage) playerNameInput.getScene().getWindow();
                                currentStage.close();
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
            try (Connection connection = MySQLConnection.getConnection();
                 PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM player WHERE username = ?")) {

                checkStatement.setString(1, playerName);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    currentStage = (Stage) playerNameInput.getScene().getWindow();
                    currentStage.close();

                    try {
                        GameStart gameStart = new GameStart(playerName);
                        gameStart.start(new Stage());
                        System.out.println("Starting the game...");
                        currentStage = (Stage) playerNameInput.getScene().getWindow();
                        currentStage.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    userMessageLabel.setText("Player name does not exist. Enter a new player name.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            userMessageLabel.setText("Please enter player name.");
        }
    }

    private boolean isMaxPlayersReached() {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM player WHERE isDeleted = 0")) {
            ResultSet resultSet = countStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count >= 10;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void startNewGame() {
        try {
            GameStart gameStart = new GameStart(playerNameInput.getText());
            gameStart.start(new Stage());
            currentStage = (Stage) playerNameInput.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setPlayerName(String playerName) {
        playerNameInput.setText(playerName);
    }

    public TextField getPlayerNameInput() {
        return playerNameInput;
    }

    public void howToPlay(ActionEvent actionEvent) {
        System.out.println("Showing the guide...");
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Instructions.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("How To Play");
            stage.show();

            InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
            if (iconStream == null) {
                throw new RuntimeException("Icon resource not found");
            }
            Image icon = new Image(iconStream);
            stage.getIcons().add(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showPlayers(ActionEvent actionEvent) {
        System.out.println("Showing current players...");
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("current_players.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());


            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Registered Players");
            stage.show();

            InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
            if (iconStream == null) {
                throw new RuntimeException("Icon resource not found");
            }
            Image icon = new Image(iconStream);
            stage.getIcons().add(icon);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showLeaderboard(ActionEvent actionEvent) {
        System.out.println("Showing high scores...");
    }

    public void quitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to quit the game?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage stage = (Stage) playerNameInput.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void facebook() {
        openWebPage("https://www.facebook.com");
    }

    public void discord() {
        openWebPage("https://www.discord.com");
    }

    public void twitter() {
        openWebPage("https://www.twitter.com");
    }

    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}