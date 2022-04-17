package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public interface TaskManager {

    //tasks
    void addTask(Task task);

    void updateTask(Task task);

    Task getTask(int id);

    HashMap<Integer, Task> getTasks();

    void deleteTask(int id);

    void deleteAllTasks();

    //epics
    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    Epic getEpic(int id);

    HashMap<Integer, Epic> getEpics();

    void deleteEpic(int id);

    void deleteAllEpics();

    //subtask
    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    HashMap<Integer, Subtask> getSubtasks();

    TreeSet<Task> getPrioritizedTasks();

    void deleteSubtask(int id);

    void deleteAllSubtask();

    List<Task> history();

}
