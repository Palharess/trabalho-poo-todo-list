package org.example.memento;

import org.example.task.Task;

import java.util.List;

public class Memento {
    private final List<Task> state;

    public Memento(List<Task> state) {
        // copia imutavel para o undo
        this.state = state;
    }

    public List<Task> getState() {
        return state;
    }
}
