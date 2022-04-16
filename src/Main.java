import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.now;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        //Without dates
        manager.addTask(new Task("Task1", "", Status.DONE));
        manager.addTask(new Task("Task2", "", Status.NEW));

        //With dates
//        manager.addTask(new Task("Task1", "", Status.DONE, LocalDateTime.now(), Duration.ofHours(1)));
//        manager.addTask(new Task("Task2", "", Status.NEW, LocalDateTime.now(), Duration.ofHours(2)));

        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);

        //Without dates
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Epic1 Subtask3", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask13);

        //With dates
//        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1,
//                LocalDateTime.of(2022, 1, 1, 1, 1), Duration.ofHours(4));
//        manager.addSubtask(subtask11);
//        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1,
//                LocalDateTime.of(2022, 5, 1, 1, 1), Duration.ofHours(5));
//        manager.addSubtask(subtask12);
//        Subtask subtask13 = new Subtask("Epic1 Subtask3", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask13);
//
//        Epic epic2 = new Epic("Epic2", "");
//        manager.addEpic(epic2);
//
        System.out.println("tasks = " + manager.getTasks());
        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());
//
//        Epic epic;
//        Task task;
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
//        task = manager.getTask(9);
//
//        subtask = manager.getSubtask(4);
//
////        manager.deleteEpic(3);
//
//
//
        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("./resources/data.csv"));

        System.out.println("tasks = " + manager.getTasks());
        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());
//        System.out.println("tasks = " + manager.getTasks());
//
//        System.out.println("history = " + fileBackedTasksManager.history());

    }
}
