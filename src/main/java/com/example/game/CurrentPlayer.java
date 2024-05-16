package com.example.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CurrentPlayer {
    private final int id;
    private final StringProperty username;

    public CurrentPlayer(int id, String username) {
        this.id = id;
        this.username = new SimpleStringProperty(username);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }
}

