package com.example.game;

@FunctionalInterface
public interface ActionHandler<T> {
    void handleAction(T item);
}
