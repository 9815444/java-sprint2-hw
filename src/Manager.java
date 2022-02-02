import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    int id;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Subtask> subtasks;
    HashMap<Integer, Epic> epics;

    public Manager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //tasks
    public void addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task getTask(int id){

        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        else {
            return null;
        }
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void deleteTask(int id){
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteAllTasks(){
        tasks.clear();
    }

    //epics
    public void addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epic.setStatus("NEW");
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
        epic.epicSubtasks = epics.get(epic.getId()).epicSubtasks;
        epics.put(epic.getId(), epic);
        calcEpicStatus(epic);
    }

    public Epic getEpic(int id){

        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        else {
            return null;
        }
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteEpic(int id){
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.epicSubtasks) {
                subtasks.remove(subtaskId);
            }
            epic.epicSubtasks = new ArrayList<>();
        }
    }

    public void deleteAllEpics(){
        epics.clear();
        subtasks.clear();
    }

    //subtask
    public void addSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        subtask.epic.epicSubtasks.add(id);
        calcEpicStatus(subtask.epic);
    }

    public void calcEpicStatus(Epic epic) {

        if (epic.epicSubtasks.size() == 0) {
            epic.status = "NEW";
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer epicSubtaskId : epic.epicSubtasks) {
            int a = epicSubtaskId;
            String status = subtasks.get(epicSubtaskId).status;
            if (status != "NEW") {
                allTaskIsNew = false;
            }
            if (status != "DONE") {
                allTaskIsDone = false;
            }
        }

        if (allTaskIsDone) {
            epic.status = "DONE";
        }
        else if (allTaskIsNew) {
            epic.status = "NEW";
        }
        else {
            epic.status = "IN_PROGRESS";
        }

    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        calcEpicStatus(subtask.epic);
    }

    public Subtask getSubtask(int id){

        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        }
        else {
            return null;
        }
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteSubtask(int id){
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).epic;
            epic.epicSubtasks.remove((Integer) id);
            calcEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    public void deleteAllSubtask(){
        ArrayList<Epic> epicsForStatusUpdate = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtask.epic.epicSubtasks = new ArrayList<>();
            if (!epicsForStatusUpdate.contains(subtask.epic)) {
                epicsForStatusUpdate.add(subtask.epic);
            }
        }
        subtasks.clear();
        for (Epic epic : epicsForStatusUpdate) {
            epic.status = "NEW";
        }
    }

}
