package com.example.game;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CurrentPlayers extends Application {
    @FXML
    private AnchorPane main_container;

    @FXML
    private TableView<Player> playersTable;

    @FXML
    private TableColumn<Player, Integer> idColumn;

    @FXML
    private TableColumn<Player, String> usernameColumn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("current_players.fxml"));
        fxmlLoader.setController(this);
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());
        primaryStage.setTitle("Current Players");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadPlayers();
    }

    private void loadPlayers() {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, username FROM player WHERE isDeleted = 0")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                playersTable.getItems().add(new Player(id, username));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButton() {
        Stage currentStage = (Stage) main_container.getScene().getWindow();
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

    public static void main(String[] args) {
        launch(args);
    }

    public void deletePlayer(ActionEvent actionEvent) {
    }

    public void editPlayer(ActionEvent actionEvent) {
    }
}
