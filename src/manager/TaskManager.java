package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;

public interface TaskManager {

    //tasks
    public void addTask(Task task);

    public void updateTask(Task task);

    public Task getTask(int id);

    public HashMap<Integer, Task> getTasks();

    public void deleteTask(int id);

    public void deleteAllTasks();

    //epics
    public void addEpic(Epic epic);

    public void updateEpic(Epic epic);

    public Epic getEpic(int id);

    public HashMap<Integer, Epic> getEpics();

    public void deleteEpic(int id);

    public void deleteAllEpics();

    //subtask
    public void addSubtask(Subtask subtask);

    public void updateSubtask(Subtask subtask);

    public Subtask getSubtask(int id);

    public HashMap<Integer, Subtask> getSubtasks();

    public void deleteSubtask(int id);

    public void deleteAllSubtask();

}
