package org.example.memento;

import org.example.task.SimpleTask;
import org.example.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    private final List<Task> state;

    public Memento(List<Task> state) {
        List<Task> copy = new ArrayList<>();
        for (Task t : state) {
            //foi criado uma task clonad para evitar trabalhar com referencias diretas
            Task clonedTask = new SimpleTask(t.getTitle(), t.getDescription());
            clonedTask.setDone(t.isDone());
            copy.add(clonedTask);
        }
        this.state = copy; // após isso state se tornou uma lista independente funcionando o undo para edit
    }


    public List<Task> getState() {
        return state;
    }
}
