package Tests;

import manager.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    @BeforeEach
    public void initializeManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void test1_AddTask() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);

        Task savedTask = manager.getTask(task1.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(),  "Неверное количество задач.");
        assertEquals(task1, tasks.get(1),  "Задачи не совпадают.");
    }

    @Test
    void test2_UpdateTask() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);

        Task task11 = new Task("Task11", "", Status.NEW);
        task11.setId(1);
        manager.updateTask(task11);

        Task updatedTask = manager.getTask(1);

        assertNotNull(updatedTask, "Задача не найдена.");
        assertEquals(task11, updatedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(),  "Неверное количество задач.");
        assertEquals(task11, tasks.get(1),  "Задачи не совпадают.");
    }

    @Test
    void test3_GetTask() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);

        Task task = manager.getTask(1);

        assertNotNull(task, "Задача не найдена.");
        assertEquals(task1, task, "Задачи не совпадают.");
        assertNull(manager.getTask(0), "Получена не пустая задача.");
    }

    @Test
    void test4_GetTasks() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.DONE);
        manager.addTask(task2);

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(),  "Неверное количество задач.");
        assertTrue(task1 == tasks.get(1) && task2 == tasks.get(2),  "Задачи не совпадают.");
    }

    @Test
    void test5_DeleteTask() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.DONE);
        manager.addTask(task2);

        manager.deleteTask(0);
        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertEquals(2, tasks.size(), "Задача удалена, а не должна была.");
        assertTrue(task1 == tasks.get(1) && task2 == tasks.get(2), "Состав задач поменялся.");

        manager.deleteTask(1);
        final HashMap<Integer, Task> tasksAfterDel = manager.getTasks();
        assertEquals(1, tasks.size(), "Задача не удалена.");
        assertEquals(task2, tasksAfterDel.get(2), "После удаления получен неверный состав задач.");
    }

    @Test
    void test6_DeleteAllTask() {
        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.DONE);
        manager.addTask(task2);

        manager.deleteAllTasks();

        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertEquals(0, tasks.size(), "Все задачи не удалены.");
    }

    @Test
    void test7_AddEpic() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Status status = epic1.getStatus();
        Task savedEpic = manager.getEpic(1);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
        Assertions.assertEquals(Status.NEW, status, "Неверный статус если нет подзадач");

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(),  "Неверное количество задач.");
        assertEquals(epic1, epics.get(1),  "Задачи не совпадают.");

        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.NEW, epic1);
        manager.addSubtask(subtask12);
        Assertions.assertEquals(Status.NEW, epic1.getStatus(), "Неверный статус с двумя новыми подзадачами.");

        manager.deleteAllSubtask();
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.DONE, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1));
        Assertions.assertEquals(Status.DONE, epic1.getStatus(), "Неверный статус с двумя выполненными подзадачами.");

        manager.deleteAllSubtask();
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1));
        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Неверный статус с подзадачами NEW + DONE");

        manager.deleteAllSubtask();
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.IN_PROGRESS, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1));
        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Неверный статус с подзадачами IN_PROGRESS");
    }

    @Test
    void test8_UpdateEpic() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1));

        Epic epic11 = new Epic("Epic1New", "");
        epic11.setId(1);

        manager.updateEpic(epic11);
        Epic updatedEpic = manager.getEpic(1);
        assertNotNull(updatedEpic, "Задача не найдена.");
        assertEquals(epic11, updatedEpic, "Задачи не совпадают.");
        
        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(),  "Неверное количество задач.");
        assertEquals(epic11, epics.get(1),  "Задачи не совпадают.");
        Assertions.assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Неверный статус.");
    }

    @Test
    void test9_GetEpic() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1));

        Epic epic = manager.getEpic(1);

        assertNotNull(epic, "Задача не найдена.");
        assertEquals(epic1, epic, "Задачи не совпадают.");
        assertNull(manager.getEpic(0), "Получена не пустая задача.");
    }

    @Test
    void test10_GetEpics() { //
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1));

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(),  "Неверное количество задач.");
        assertTrue(epic1 == epics.get(1),  "Задачи не совпадают.");
    }

    @Test
    void test11_DeleteEpic() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1));

        manager.deleteEpic(0);
        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertEquals(1, epics.size(), "Задача удалена, а не должна была.");

        manager.deleteEpic(1);
        final HashMap<Integer, Epic> epicsAfterDel = manager.getEpics();
        assertEquals(0, epicsAfterDel.size(), "Задача не удалена.");
    }

    @Test
    void test12_DeleteAllTask() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1));

        manager.deleteAllEpics();

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertEquals(0, epics.size(), "Все задачи не удалены.");
    }

}
