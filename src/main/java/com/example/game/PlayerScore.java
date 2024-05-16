package com.example.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerScore {
    private final IntegerProperty rank;
    private final StringProperty playerName;
    private final IntegerProperty highestScore;

    public PlayerScore(int rank, String playerName, int highestScore) {
        this.rank = new SimpleIntegerProperty(rank);
        this.playerName = new SimpleStringProperty(playerName);
        this.highestScore = new SimpleIntegerProperty(highestScore);
    }

    public int getRank() {
        return rank.get();
    }

    public IntegerProperty rankProperty() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank.set(rank);
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

    public int getHighestScore() {
        return highestScore.get();
    }

    public IntegerProperty highestScoreProperty() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore.set(highestScore);
    }
}
