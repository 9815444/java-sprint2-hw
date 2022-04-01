import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = FileBackedTasksManager.loadFromFile(new File("./resources/data.csv"));


//        TaskManager manager = Managers.getDefault();
//
//        manager.addTask(new Task("Task1", "", Status.DONE));
//        manager.addTask(new Task("Task2", "", Status.NEW));
//
//        Epic epic1 = new Epic("Epic1", "");
//        manager.addEpic(epic1);
//
//        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1);
//        manager.addSubtask(subtask11);
//        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask12);
//        Subtask subtask13 = new Subtask("Epic1 Subtask3", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask13);
//
//        Epic epic2 = new Epic("Epic2", "");
//        manager.addEpic(epic2);
//
//        System.out.println("tasks = " + manager.getTasks());
//        System.out.println("epics = " + manager.getEpics());
//        System.out.println("subtasks = " + manager.getSubtasks());
//
//        Epic epic;
        Task task;
//        Subtask subtask;
//
//        epic = manager.getEpic(3);
//        epic = manager.getEpic(3);
//        epic = manager.getEpic(7);
//        epic = manager.getEpic(7);
//        epic = manager.getEpic(7);
//        epic = manager.getEpic(3);
//
//        task = manager.getTask(1);
//        task = manager.getTask(1);
//        task = manager.getTask(2);
        task = manager.getTask(9);
//
//        subtask = manager.getSubtask(4);
//
////        manager.deleteEpic(3);
//
//        System.out.println("history = " + manager.history());

    }
}
