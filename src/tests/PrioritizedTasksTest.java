package tests;

import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.*;
import manager.HTTPTaskManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedTasksTest {

    InMemoryTaskManager manager;

    @BeforeEach
    void initialize() throws IOException {
        manager = new InMemoryTaskManager();
    }

    @Test
    void test1() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.DONE);
        manager.addTask(task2);
        Task task3 = new Task("Task3", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        manager.addTask(task3);
        TreeSet<Task> tasks = manager.getPrioritizedTasks();

        assertEquals(3, tasks.size());
        assertEquals(task3, tasks.toArray()[0]);
        assertEquals(task1, tasks.toArray()[1]);
        assertEquals(task2, tasks.toArray()[2]);

//        manager.addTask(new Task("Task1", "", Status.DONE, LocalDateTime.now().plusHours(1), Duration.ofHours(1)));
//        Epic epic1 = new Epic("Epic1", "");
//        manager.addEpic(epic1);
//        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
//        manager.addSubtask(subtask11);
//        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask12);
    }

    @Test
    void test2() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.DONE);
        manager.addTask(task2);
        Task task3 = new Task("Task3", "", Status.NEW, LocalDateTime.of(2022,1,1,0,0,0), Duration.ofHours(1));
        manager.addTask(task3);
        Task task4 = new Task("Task4", "", Status.NEW, LocalDateTime.of(2022,1,1,2,0,0), Duration.ofHours(1));
        manager.addTask(task4);

        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.of(2022,1,1,1,0,0), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);

        TreeSet<Task> tasks = manager.getPrioritizedTasks();

        assertEquals(6, tasks.size());
        assertEquals(task3, tasks.toArray()[0]);
        assertEquals(subtask11, tasks.toArray()[1]);
        assertEquals(task4, tasks.toArray()[2]);
        assertEquals(task1, tasks.toArray()[3]);
        assertEquals(task2, tasks.toArray()[4]);

//        manager.addTask(new Task("Task1", "", Status.DONE, LocalDateTime.now().plusHours(1), Duration.ofHours(1)));
//        Epic epic1 = new Epic("Epic1", "");
//        manager.addEpic(epic1);
//        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
//        manager.addSubtask(subtask11);
//        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask12);
    }
}
