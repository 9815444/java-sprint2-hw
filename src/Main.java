import manager.InMemoryTaskManager;
import manager.Managers;
import manager.Status;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        //epics
        Epic epic1 = new Epic("Epic №1", "");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Epic №2", "");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Epic2 Subtask1", "", Status.DONE, epic2);
        manager.addSubtask(subtask21);

        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());

        Subtask subtask12New = new Subtask("Epic1 Subtask2 modified", "", Status.NEW, epic1);
        subtask12New.setId(3);
        manager.updateSubtask(subtask12New);

        System.out.println("epics = " + manager.getEpics());
        System.out.println("subtasks = " + manager.getSubtasks());

//        manager.deleteEpic(4);
//        System.out.println("epics = " + manager.getEpics());
//        System.out.println("subtasks = " + manager.getSubtasks());
//
//        manager.deleteSubtask(2);
//        System.out.println("epics = " + manager.getEpics());
//        System.out.println("subtasks = " + manager.getSubtasks());

        Epic epicTest;
        epicTest = manager.getEpic(1);
        epicTest = manager.getEpic(1);
        epicTest = manager.getEpic(1);
        epicTest = manager.getEpic(4);
        epicTest = manager.getEpic(1);
        epicTest = manager.getEpic(4);
        Subtask subtaskTest;
        subtaskTest= manager.getSubtask(2);
        subtaskTest= manager.getSubtask(3);
        subtaskTest= manager.getSubtask(2);
        subtaskTest= manager.getSubtask(3);
        subtaskTest= manager.getSubtask(2);
        subtaskTest= manager.getSubtask(3);
        subtaskTest= manager.getSubtask(2);
        subtaskTest= manager.getSubtask(3);

        List<Task> taskHistory = manager.history();

    }
}
