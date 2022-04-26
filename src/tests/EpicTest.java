package tests;

import manager.Managers;
import manager.Status;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {

    @Test
    void EpicTestWhenListOfSubtasksIsEmpty(){

        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Status status = epic1.getStatus();
        Assertions.assertEquals(Status.NEW, status);

    }

    @Test
    void EpicTestWhenAllSubtasksHaveStatusIsNew(){

        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask12);
        Status status = epic1.getStatus();
        Assertions.assertEquals(Status.NEW, status);

    }

    @Test
    void EpicTestWhenAllSubtasksHaveStatusIsDone(){

        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask12);
        Status status = epic1.getStatus();
        Assertions.assertEquals(Status.DONE, status);
    }

    @Test
    void EpicTestWhenSubtasksHaveStatusIsDoneAndNew(){

        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.NEW, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.DONE, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask12);
        Status status = epic1.getStatus();
        Assertions.assertEquals(Status.IN_PROGRESS, status);
    }

    @Test
    void EpicTestWhenAllSubtasksHaveStatusIsInProgress(){

        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.IN_PROGRESS, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1, LocalDateTime.now(), Duration.ofHours(1));
        manager.addSubtask(subtask12);
        Status status = epic1.getStatus();
        Assertions.assertEquals(Status.IN_PROGRESS, status);
    }

}