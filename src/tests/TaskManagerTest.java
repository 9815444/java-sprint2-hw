package tests;

import manager.Status;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    TaskManager manager;

    @BeforeEach
    public abstract void initializeManager();


    @Test
    void test1_AddTask() {

        LocalDateTime startTime = LocalDateTime.of(2022, 1, 1, 1, 1);
        Duration duration = Duration.ofHours(2);

        Task task1 = new Task("Task1", "", Status.DONE, startTime, duration);
        manager.addTask(task1);

        Task savedTask = manager.getTask(task1.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
        assertEquals(task1.getStartTime(), savedTask.getStartTime(), "Задачи не совпадают.");
        assertEquals(task1.getDuration(), savedTask.getDuration(), "Задачи не совпадают.");
        assertEquals(task1.getEndTime(), savedTask.getEndTime(), "Задачи не совпадают.");


        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(1), "Задачи не совпадают.");
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
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task11, tasks.get(1), "Задачи не совпадают.");
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
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertTrue(task1 == tasks.get(1) && task2 == tasks.get(2), "Задачи не совпадают.");
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
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(1), "Задачи не совпадают.");

        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.of(2022,1,1,1,1), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.NEW, epic1, LocalDateTime.of(2022,2,1,1,1), Duration.ofHours(5));
        manager.addSubtask(subtask12);
        Assertions.assertEquals(Status.NEW, epic1.getStatus(), "Неверный статус с двумя новыми подзадачами.");

        Assertions.assertEquals(LocalDateTime.parse("2022-01-01T01:01"), epic1.getStartTime(), "Неверная дата старта");
        Assertions.assertEquals(LocalDateTime.parse("2022-02-01T06:01"), epic1.getEndTime(), "Неверная дата завершения");
        Assertions.assertEquals(Duration.parse("PT749H"), epic1.getDuration(), "Неверная продолжительность");

        manager.deleteAllSubtask();
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        Assertions.assertEquals(Status.DONE, epic1.getStatus(), "Неверный статус с двумя выполненными подзадачами.");

        manager.deleteAllSubtask();
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Неверный статус с подзадачами NEW + DONE");

        manager.deleteAllSubtask();
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.IN_PROGRESS, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Неверный статус с подзадачами IN_PROGRESS");
    }

    @Test
    void test8_UpdateEpic() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));

        Epic epic11 = new Epic("Epic1New", "");
        epic11.setId(1);

        manager.updateEpic(epic11);
        Epic updatedEpic = manager.getEpic(1);
        assertNotNull(updatedEpic, "Задача не найдена.");
        assertEquals(epic11, updatedEpic, "Задачи не совпадают.");

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic11, epics.get(1), "Задачи не совпадают.");
        Assertions.assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Неверный статус.");
    }

    @Test
    void test9_GetEpic() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));

        Epic epic = manager.getEpic(1);

        assertNotNull(epic, "Задача не найдена.");
        assertEquals(epic1, epic, "Задачи не совпадают.");
        assertNull(manager.getEpic(0), "Получена не пустая задача.");
    }

    @Test
    void test10_GetEpics() { //
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertTrue(epic1 == epics.get(1), "Задачи не совпадают.");
    }

    @Test
    void test11_DeleteEpic() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));

        manager.deleteEpic(0);
        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertEquals(1, epics.size(), "Задача удалена, а не должна была.");

        manager.deleteEpic(1);
        final HashMap<Integer, Epic> epicsAfterDel = manager.getEpics();
        assertEquals(0, epicsAfterDel.size(), "Задача не удалена.");
    }

    @Test
    void test12_DeleteAllEpics() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1)));
        manager.addSubtask(new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1)));

        manager.deleteAllEpics();

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertEquals(0, epics.size(), "Все задачи не удалены.");
    }

    @Test
    void test13_AddSubtask() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask12);

        Subtask subtask121 = manager.getSubtask(subtask12.getId());

        assertNotNull(subtask121, "Задача не найдена.");
        assertEquals(subtask12, subtask121, "Задачи не совпадают.");
        assertEquals(epic1, subtask121.getEpic(), "Epic не тот.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask11, subtasks.get(2), "Задачи не совпадают.");
        assertEquals(subtask12, subtasks.get(3), "Задачи не совпадают.");
    }

    @Test
    void test14_UpdateSubtask() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
        manager.addSubtask(subtask2);

        Subtask subtask2New = new Subtask("Epic1 Subtask2New", "", Status.IN_PROGRESS, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
        subtask2New.setId(subtask2.getId());
        manager.updateSubtask(subtask2New);

        assertEquals(epic1, subtask2New.getEpic(), "Epic не тот.");
        assertNotNull(manager.getSubtask(3), "Задача не найдена.");
        assertEquals(subtask2New, manager.getSubtask(3), "Задачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(2), "Задачи не совпадают.");
        assertEquals(subtask2New, subtasks.get(3), "Задачи не совпадают.");
    }

    @Test
    void test15_GetSubtask() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask2);

        Subtask subtask = manager.getSubtask(2);

        assertNotNull(subtask, "Задача не найдена.");
        assertEquals(subtask1, subtask, "Задачи не совпадают.");
        assertNull(manager.getSubtask(0), "Получена не пустая задача.");
    }

    @Test
    void test10_GetSubtasks() { //
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask2);

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertTrue(subtask1 == subtasks.get(2), "Задачи не совпадают.");
        assertTrue(subtask2 == subtasks.get(3), "Задачи не совпадают.");
    }

    @Test
    void test11_DeleteSubtask() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask2);

        manager.deleteSubtask(0);
        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertEquals(2, subtasks.size(), "Задача удалена, а не должна была.");

        manager.deleteSubtask(2);
        final HashMap<Integer, Subtask> subtasksAfterDel = manager.getSubtasks();
        assertEquals(1, subtasksAfterDel.size(), "Задача не удалена.");
    }

    @Test
    void test12_DeleteAllSubtasks() {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask2);

        manager.deleteAllSubtask();

        final HashMap<Integer, Subtask> subtaskHashMap = manager.getSubtasks();
        assertEquals(0, subtaskHashMap.size(), "Все задачи не удалены.");
    }


    @Test
    void test13_AddTaskToHistoryStandart() {
        List<Task> history = manager.history();
        assertEquals(0, history.size(), "История не пустая.");

        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);

        Task task2 = new Task("Task2", "", Status.NEW);
        manager.addTask(task2);

        Task task3 = new Task("Task3", "", Status.NEW);
        manager.addTask(task3);

        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);

        List<Task> exphistory = List.of(task1, task2, task3);
        assertEquals(exphistory, manager.history(), "Неверная история. Стандарт.");
    }

    @Test
    void test14_AddTaskToHistoryDup() {
        manager.deleteAllTasks();

        List<Task> history = manager.history();
        assertEquals(0, history.size(), "История не пустая.");

        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);

        Task task2 = new Task("Task2", "", Status.NEW);
        manager.addTask(task2);

        Task task3 = new Task("Task3", "", Status.NEW);
        manager.addTask(task3);

        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(1);

        List<Task> exphistory = List.of(task2, task1);
        assertEquals(exphistory, manager.history(), "Неверная история. Дублирование.");
    }

    @Test
    void test15_AddTaskToHistoryDelStart() {
        manager.deleteAllTasks();

        List<Task> history = manager.history();
        history = manager.history();
        assertEquals(0, history.size(), "История не пустая.");

        Task task1 = new Task("Task1", "", Status.DONE, LocalDateTime.now(), Duration.ofHours(1));
        manager.addTask(task1);

        Task task2 = new Task("Task2", "", Status.NEW, LocalDateTime.now(), Duration.ofHours(1));
        manager.addTask(task2);

        Task task3 = new Task("Task3", "", Status.NEW, LocalDateTime.now(), Duration.ofHours(1));
        manager.addTask(task3);

        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);

        manager.deleteTask(1);

        List<Task> exphistory = List.of(task2, task3);
        assertEquals(exphistory, manager.history(), "Неверная история.");
    }

    @Test
    void test16_AddTaskToHistoryDelMiddle() {
        manager.deleteAllTasks();

        List<Task> history = manager.history();
        history = manager.history();
        assertEquals(0, history.size(), "История не пустая.");

        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);

        Task task2 = new Task("Task2", "", Status.NEW);
        manager.addTask(task2);

        Task task3 = new Task("Task3", "", Status.NEW);
        manager.addTask(task3);

        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);

        manager.deleteTask(2);

        List<Task> exphistory = List.of(task1, task3);
        assertEquals(exphistory, manager.history(), "Неверная история.");
    }

    @Test
    void test17_AddTaskToHistoryDelEnd() {
        manager.deleteAllTasks();

        List<Task> history = manager.history();
        history = manager.history();
        assertEquals(0, history.size(), "История не пустая.");

        Task task1 = new Task("Task1", "", Status.DONE);
        manager.addTask(task1);

        Task task2 = new Task("Task2", "", Status.NEW);
        manager.addTask(task2);

        Task task3 = new Task("Task3", "", Status.NEW);
        manager.addTask(task3);

        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);

        manager.deleteTask(3);

        List<Task> exphistory = List.of(task1, task2);
        assertEquals(exphistory, manager.history(), "Неверная история.");
    }

    @Test
    void  test18_TaskEndTimeError() {
        LocalDateTime startTime1 = LocalDateTime.of(2022, 1, 1, 1, 1);
        Duration duration1 = Duration.ofHours(2);
        Task task1 = new Task("Task1", "", Status.DONE, startTime1, duration1);
        manager.addTask(task1);

        LocalDateTime startTime2 = LocalDateTime.of(2022, 1, 1, 1, 5);
        Duration duration2 = Duration.ofHours(2);
        Task task2 = new Task("Task2", "", Status.DONE, startTime1, duration1);
        manager.addTask(task2);
    }

}
