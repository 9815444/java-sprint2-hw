package Tests;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager>{

    HistoryManager manager;

    @BeforeEach
    public abstract void initializeManager();

//    @Test
//    void add() {
//        Epic epic1 = new Epic("Epic1", "");
//        manager.addEpic(epic1);
//        Subtask subtask1 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1);
//        manager.addSubtask(subtask1);
//        Subtask subtask2 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1);
//        manager.addSubtask(subtask2);
//
//        manager.deleteSubtask(0);
//        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
//        assertEquals(2, subtasks.size(), "Задача удалена, а не должна была.");
//
//        manager.deleteSubtask(2);
//        final HashMap<Integer, Subtask> subtasksAfterDel = manager.getSubtasks();
//        assertEquals(1, subtasksAfterDel.size(), "Задача не удалена.");
//    }
//
//    @Test
//    void remove() {
//    }
//
//    @Test
//    void getHistory() {
//    }
}