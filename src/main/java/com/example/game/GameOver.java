package com.example.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class GameOver extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(GameOver.class.getResource("game_over.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());
        stage.setTitle("SpaceX");
        stage.setScene(scene);
        stage.show();

        // Retrieve the controller instance after the FXML file is loaded
//        Main_Menu controller = fxmlLoader.getController();
//        controller.initialize();
//        MySQLConnection.createPlayerTable();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
