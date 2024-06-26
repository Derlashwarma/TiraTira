package com.example.game;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Instructions extends Application{
    @FXML
    private AnchorPane main_container;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main_Menu.class.getResource("instructions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());

        InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
        if (iconStream == null) {
            throw new RuntimeException("Icon resource not found");
        }
        Image icon = new Image(iconStream);
        primaryStage.getIcons().add(icon);

        primaryStage.setTitle("Instructions");
        primaryStage.setScene(scene);
        primaryStage.show();


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

    public static void main(String[] args) {
        launch(args);
    }


}
