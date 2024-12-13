package org.example;

import org.example.interfaceGrafica.MainFrame;
import org.example.task.*;
import org.example.task.factory.SimpleTaskFactory;
import org.example.task.manager.TaskManager;
import org.example.memento.TaskManagerHistory;
import org.example.thread.AutoSaveThread;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog(null, "Qual é o seu nome?", "Bem-vindo", JOptionPane.QUESTION_MESSAGE);

        if (username == null || username.trim().isEmpty()) {
            username = "usuario_padrao";
        }

        TaskManager taskManager = new TaskManager();
        TaskManagerHistory history = new TaskManagerHistory();
        TaskFactory factory = new SimpleTaskFactory();

        // banco de dados com o nome
        File dbFile = new File(username + "_tasks.db");

        // carrega as tasks
        if (dbFile.exists()) {
            try {
                List<Task> loaded = TaskFileHandler.loadTasks(dbFile);
                for (Task t : loaded) {
                    taskManager.addTask(t);
                }
            } catch (IOException e) {
                System.err.println("Não foi possível carregar as tarefas do usuário " + username + ": " + e.getMessage());
            }
        }

        String finalUsername = username;

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(taskManager, history, factory, finalUsername);
            taskManager.refreshObservers();

            // autosave
            AutoSaveThread autoSaveThread = new AutoSaveThread(taskManager, dbFile);
            autoSaveThread.start();

            // o salvamento é feito ao fechar
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    try {
                        TaskFileHandler.saveTasks(taskManager.getTasks(), dbFile);
                    } catch (IOException e) {
                        System.err.println("Erro ao salvar as tarefas de " + finalUsername + ": " + e.getMessage());
                    }
                    // parar a thread
                    autoSaveThread.stopAutoSave();
                }
            });

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
