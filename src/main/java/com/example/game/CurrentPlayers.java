package com.example.game;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class CurrentPlayers extends Application {
    public Label userMessageLabel;
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
        stage.setTitle("Current Players");
        stage.show();
    }

    @FXML
    private void initialize() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter() {
            @Override
            public String fromString(String string) {
                return string.toUpperCase();
            }
        }));
        usernameColumn.setOnEditCommit(event -> {
            CurrentPlayer player = event.getRowValue();
            String oldName = player.getUsername().toUpperCase();
            String newName = event.getNewValue();
            if (newName.equals(oldName)) {
                userMessageLabel.setText("No changes done.");
            } else if (!isPlayerNameTaken(newName)) {
                userMessageLabel.setText("");
                player.setUsername(newName.toUpperCase());
                updatePlayerInDatabase(player);
            } else {
                userMessageLabel.setText("Player name is already taken.");
                playersTable.refresh();
            }
        });

        fetchPlayersFromDatabase();
        populateTableView();
        addButtonColumns();

        playersTable.setEditable(true);
        playersTable.setFixedCellSize(40);
        playersTable.setRowFactory(tv -> {
            TableRow<CurrentPlayer> row = new TableRow<>();
            row.setPrefHeight(playersTable.getFixedCellSize());
            return row;
        });
        playersTable.prefHeightProperty().bind(playersTable.fixedCellSizeProperty().multiply(11));
        playersTable.minHeightProperty().bind(playersTable.prefHeightProperty());
        playersTable.maxHeightProperty().bind(playersTable.prefHeightProperty());
        playersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        usernameColumn.setSortable(false);
    }


    private void addButtonColumns() {
        TableColumn<CurrentPlayer, Void> editColumn = new TableColumn<>("Edit");
        editColumn.setMinWidth(115);
        editColumn.setMaxWidth(115);
        editColumn.setSortable(false);
        editColumn.setCellFactory(createButtonCellFactory("", "/com/example/images/edit_icon.png", this::handleEditAction));
        playersTable.getColumns().add(editColumn);

        TableColumn<CurrentPlayer, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setMinWidth(115);
        deleteColumn.setMaxWidth(115);
        deleteColumn.setSortable(false);
        deleteColumn.setCellFactory(createButtonCellFactory("", "/com/example/images/delete_icon.png", this::handleDeleteAction));
        playersTable.getColumns().add(deleteColumn);
    }

    private void handleEditAction(CurrentPlayer player) {
        int rowIndex = playersTable.getItems().indexOf(player);
        if (rowIndex >= 0) {
            playersTable.edit(rowIndex, usernameColumn);
        }
    }

    private boolean isPlayerNameTaken(String newName) {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE username = ? AND isDeleted = 0")) {
            statement.setString(1, newName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    private void handleDeleteAction(CurrentPlayer player) {
        System.out.println("Delete player: " + player.getUsername());
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE player SET isDeleted = 1 WHERE id = ?")) {
            statement.setInt(1, player.getId());
            statement.executeUpdate();
            playerList.remove(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Callback<TableColumn<CurrentPlayer, Void>, TableCell<CurrentPlayer, Void>> createButtonCellFactory(String buttonText, String iconFileName, ActionHandler<CurrentPlayer> handler) {
        return new Callback<>() {
            @Override
            public TableCell<CurrentPlayer, Void> call(TableColumn<CurrentPlayer, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button();

                    {
                        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconFileName)));
                        ImageView imageView = new ImageView(icon);
                        imageView.setFitWidth(20);
                        imageView.setFitHeight(20);
                        button.setGraphic(imageView);
                        button.getStyleClass().add("editDelete-color");
                        button.setOnAction(event -> {
                            CurrentPlayer player = getTableView().getItems().get(getIndex());
                            if (iconFileName.equals("/com/example/images/delete_icon.png")) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation");
                                alert.setHeaderText("Delete Player");
                                alert.setContentText("Are you sure you want to delete " + player.getUsername() + "?");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    handler.handleAction(player);
                                }
                            } else if (iconFileName.equals("/com/example/images/edit_icon.png")) {
                                System.out.println("Edit Icon Clicked");
                                TableView<CurrentPlayer> table = getTableView();
                                table.edit(getIndex(), usernameColumn);
                            } else {
                                handler.handleAction(player);
                            }
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

                    @Override
                    protected double computeMinWidth(double height) {
                        return 100;
                    }

                    @Override
                    protected double computeMinHeight(double width) {
                        return 30;
                    }

                    @Override
                    protected double computeMaxWidth(double height) {
                        return 200;
                    }

                    @Override
                    protected double computeMaxHeight(double width) {
                        return 50;
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

    private void updatePlayerInDatabase(CurrentPlayer player) {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE player SET username = ? WHERE id = ?")) {
            statement.setString(1, player.getUsername());
            statement.setInt(2, player.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
