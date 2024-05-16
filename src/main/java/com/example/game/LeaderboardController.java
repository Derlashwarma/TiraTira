package com.example.game;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javax.security.auth.callback.Callback;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.game.Game.main_container;

public class LeaderboardController {

    public AnchorPane main_container;
    @FXML
    private TableView<PlayerScore> leaderboardTable;

    @FXML
    private TableColumn<PlayerScore, Integer> rankColumn;

    @FXML
    private TableColumn<PlayerScore, String> playerNameColumn;

    @FXML
    private TableColumn<PlayerScore, Integer> highestScoreColumn;

    private final ObservableList<PlayerScore> topPlayers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(cellData -> cellData.getValue().rankProperty().asObject());
        playerNameColumn.setCellValueFactory(cellData -> {
            PlayerScore playerScore = cellData.getValue();
            String playerName = playerScore.getPlayerName();
            return new SimpleStringProperty(playerName);
        });
        highestScoreColumn.setCellValueFactory(cellData -> cellData.getValue().highestScoreProperty().asObject());

        leaderboardTable.setRowFactory(tableView -> new TableRow<>() {
            @Override
            protected void updateItem(PlayerScore item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && getIndex() == 0) {
                    getStyleClass().add("first-rank");
                } else {
                    getStyleClass().remove("first-rank");
                }
            }
        });

        fetchTopPlayers();
    }

    private void fetchTopPlayers() {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT username, scores FROM player WHERE scores > 0 ORDER BY scores DESC LIMIT 5");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            int rank = 1;
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int score = resultSet.getInt("scores");
                topPlayers.add(new PlayerScore(rank, username, score));
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        leaderboardTable.setItems(topPlayers);
    }

    @FXML
    private void backButton(ActionEvent actionEvent) {
        Stage currentStage = (Stage) main_container.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("main_menu.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());


            Stage mainMenuStage = new Stage();
            mainMenuStage.setTitle("Space Horizon");
            mainMenuStage.setScene(scene);
            mainMenuStage.show();

            InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
            if (iconStream == null) {
                throw new RuntimeException("Icon resource not found");
            }
            Image icon = new Image(iconStream);
            mainMenuStage.getIcons().add(icon);
            mainMenuStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
