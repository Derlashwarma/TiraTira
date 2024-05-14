package com.example.game;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CurrentPlayers extends Application {
    @FXML
    private AnchorPane main_container;

    @FXML
    private TableView<CurrentPlayer> playersTable;

    @FXML
    private TableColumn<CurrentPlayer, String> usernameColumn;

    private final ObservableList<CurrentPlayer> playerList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("current_players.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("menu_styles.css")).toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Instructions");
        stage.show();
    }

    @FXML
    private void initialize() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        fetchPlayersFromDatabase();
        populateTableView();
        addButtonColumns();
    }

    private void addButtonColumns() {
        TableColumn<CurrentPlayer, Void> editColumn = new TableColumn<>("Edit");
        editColumn.setPrefWidth(120);
        editColumn.setCellFactory(createButtonCellFactory("Edit", this::handleEditAction));
        playersTable.getColumns().add(editColumn);

        TableColumn<CurrentPlayer, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setPrefWidth(120);
        deleteColumn.setCellFactory(createButtonCellFactory("Delete", this::handleDeleteAction));
        playersTable.getColumns().add(deleteColumn);
    }

    private void handleEditAction(CurrentPlayer player) {
        // Handle edit action for the given player
        System.out.println("Edit player: " + player.getUsername());
    }

    private void handleDeleteAction(CurrentPlayer player) {
        // Handle delete action for the given player
        System.out.println("Delete player: " + player.getUsername());
    }

    private Callback<TableColumn<CurrentPlayer, Void>, TableCell<CurrentPlayer, Void>> createButtonCellFactory(String buttonText, ActionHandler<CurrentPlayer> handler) {
        return new Callback<>() {
            @Override
            public TableCell<CurrentPlayer, Void> call(TableColumn<CurrentPlayer, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button(buttonText);

                    {
                        button.getStyleClass().add("editDelete-color");
                        button.setOnAction(event -> {
                            CurrentPlayer player = getTableView().getItems().get(getIndex());
                            handler.handleAction(player);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        };
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

            InputStream iconStream = getClass().getResourceAsStream("/com/example/images/game_icon2.png");
            if (iconStream == null) {
                throw new RuntimeException("Icon resource not found");
            }
            Image icon = new Image(iconStream);
            mainMenuStage.getIcons().add(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchPlayersFromDatabase() {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE isDeleted = 0");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                playerList.add(new CurrentPlayer(id, username));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTableView() {
        playersTable.setItems(playerList);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private void editPlayerAction(ActionEvent event) {
        CurrentPlayer player = playersTable.getSelectionModel().getSelectedItem();
        if (player != null) {
            handleEditAction(player);
        }
    }

    @FXML
    private void deletePlayerAction(ActionEvent event) {
        CurrentPlayer player = playersTable.getSelectionModel().getSelectedItem();
        if (player != null) {
            handleDeleteAction(player);
        }
    }
}
