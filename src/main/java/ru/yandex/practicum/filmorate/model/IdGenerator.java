package ru.yandex.practicum.filmorate.model;

public class IdGenerator {
    private int nextId = 1;

    public int getNextId() {
        return nextId++;
    }
}
