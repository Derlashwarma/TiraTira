package com.example.game;

import java.sql.*;

public class MySQLConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/dbshooter";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void createPlayerTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String createTable = "CREATE TABLE IF NOT EXISTS player (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                "username VARCHAR(255)," +
                                "scores INT DEFAULT 0," +
                                "isDeleted BOOLEAN DEFAULT FALSE" +
                                ");";

            statement.execute(createTable);
            System.out.println("Player table created successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }
    public static void updatePlayerScore(String username, int score) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player SET scores =? WHERE username =? AND scores <?")) {
            preparedStatement.setInt(1, score); // Target score
            preparedStatement.setString(2, username); // Username to match
            preparedStatement.setInt(3, score); // Current score to compare against
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Score updated for " + username + ". Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating player score", e);
        }
    }

}
