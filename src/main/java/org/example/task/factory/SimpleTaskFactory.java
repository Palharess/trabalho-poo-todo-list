package org.example.task.factory;

import org.example.task.SimpleTask;
import org.example.task.Task;
import org.example.task.TaskFactory;

public class SimpleTaskFactory extends TaskFactory {
    public Task createTask(String title, String description) {
        return new SimpleTask(title, description);
    }
}
