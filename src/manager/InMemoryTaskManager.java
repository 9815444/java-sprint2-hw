package manager;

import exceptions.TaskStartTimeException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;
    protected HistoryManager historyManager;
    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        Comparator<Task> comparator = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartTime() == null && o2.getStartTime() == null) return 1;
                else if (o1.getStartTime() == null && o2.getStartTime() != null) return 1;
                else if (o1.getStartTime() != null && o2.getStartTime() == null) return -1;
                else if (o1.getStartTime().equals(o2.getStartTime())) return 1;
                else return (int) -Duration.between(o1.getStartTime(), o2.getStartTime()).toMillis();
            }
        };
        prioritizedTasks = new TreeSet<Task>(comparator);
    }

    boolean isTaskStartTimeException() {
        LocalDateTime prevEndTime = null;
        for (Task prioritizedTask : prioritizedTasks) {
            if (prevEndTime == null) {
                prevEndTime = prioritizedTask.getEndTime();
                continue;
            }
            if (prioritizedTask.getEndTime() == null) {
                continue;
            }
            if (prevEndTime.isAfter(prioritizedTask.getStartTime())) {
                return true;
            } else {
                prevEndTime = prioritizedTask.getEndTime();
            }
        }
        return false;
    }

    //tasks
    @Override
    public void addTask(Task task) {
        try {
            task.setId(++id);
            tasks.put(id, task);
            prioritizedTasks.add(task);
            if (isTaskStartTimeException()) {
                throw new TaskStartTimeException("Пересечение задач.");
            }
        } catch (TaskStartTimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Task task) {
        try {
            prioritizedTasks.remove(tasks.get(task.getId()));
            prioritizedTasks.add(task);
            tasks.put(task.getId(), task);
            if (isTaskStartTimeException()) {
                throw new TaskStartTimeException("Пересечение задач.");
            }
        } catch (TaskStartTimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.getOrDefault(id, null);
        historyManager.add(task);
        return task;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            prioritizedTasks.remove(tasks.get(id));
            historyManager.remove(id);
        }
        tasks.clear();
    }

    //epics
    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epic.setStatus(Status.NEW);
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epic.setEpicSubtasks(epics.get(epic.getId()).getEpicSubtasks());
        epics.put(epic.getId(), epic);
        calcEpicStatus(epic);
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.getOrDefault(id, null);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            historyManager.remove(id);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epic.setEpicSubtasks(new ArrayList<>());
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        subtasks.clear();
    }

    //subtask
    @Override
    public void addSubtask(Subtask subtask) {
        try {
            subtask.setId(++id);
            subtasks.put(id, subtask);
            prioritizedTasks.add(subtask);
            subtask.getEpic().getEpicSubtasks().add(id);
            calcEpicStatus(subtask.getEpic());
            if (isTaskStartTimeException()) {
                throw new TaskStartTimeException("Пересечение задач.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        try {
            prioritizedTasks.remove(subtasks.get(subtask.getId()));
            prioritizedTasks.add(subtask);
            subtasks.put(subtask.getId(), subtask);
            calcEpicStatus(subtask.getEpic());
            if (isTaskStartTimeException()) {
                throw new TaskStartTimeException("Пересечение задач.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.getOrDefault(id, null);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            prioritizedTasks.remove(subtasks.get(id));
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtasks().remove((Integer) id);
            calcEpicStatus(epic);
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllSubtask() {
        ArrayList<Epic> epicsForStatusUpdate = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
            subtask.getEpic().setEpicSubtasks(new ArrayList<>());
            if (!epicsForStatusUpdate.contains(subtask.getEpic())) {
                epicsForStatusUpdate.add(subtask.getEpic());
            }
        }
        subtasks.clear();
        for (Epic epic : epicsForStatusUpdate) {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    private void calcEpicStatus(Epic epic) {

        ArrayList<Integer> subtaskIdArrayList = epic.getEpicSubtasks();

        if (subtaskIdArrayList.size() == 0) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;
        LocalDateTime epicsStartTime = null;
        LocalDateTime epicsEndTime = null;

        for (Integer epicSubtaskId : subtaskIdArrayList) {
            Subtask currentSubtask = subtasks.get(epicSubtaskId);
            Status status = currentSubtask.getStatus();
            if (!(status == Status.NEW)) {
                allTaskIsNew = false;
            }
            if (!(status == Status.DONE)) {
                allTaskIsDone = false;
            }
            if (currentSubtask.getStartTime() != null) {
                if (epicsStartTime == null || currentSubtask.getStartTime().isBefore(epicsStartTime)) {
                    epicsStartTime = currentSubtask.getStartTime();
                }
            }
            if (currentSubtask.getEndTime() != null) {
                if (epicsEndTime == null || currentSubtask.getEndTime().isAfter(epicsEndTime)) {
                    epicsEndTime = currentSubtask.getEndTime();
                }
            }
        }

        epic.setStartTime(epicsStartTime);
        epic.setEndTime(epicsEndTime);
        if (epicsStartTime == null || epicsEndTime == null) epic.setDuration(null);
        else epic.setDuration(Duration.between(epicsStartTime, epicsEndTime));

        if (allTaskIsDone) {
            epic.setStatus(Status.DONE);
        } else if (allTaskIsNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

}
