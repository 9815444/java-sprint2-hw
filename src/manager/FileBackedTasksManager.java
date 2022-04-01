package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private String fileName;

    public FileBackedTasksManager() {
        fileName = "./resources/data.csv";
        file = new File(fileName);
        if (!file.isFile()) {
            try {
                Path path = Files.createFile(Paths.get(fileName));
            } catch (IOException e) {
                System.out.println("Ошибка создания файла.");
            }
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        String data = "";
        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] lines = data.split("\n");
        List<String> epics = new ArrayList<>();
        List<String> subtasks = new ArrayList<>();
        List<String> tasks = new ArrayList<>();
        String lineOfHistory = "";
        boolean isTitle = true;
        boolean itsTask = true;
        for (String line : lines) {
            if (isTitle) {
                isTitle = false;
                continue;
            }
            if (line.equals("\r")) {
                itsTask = false;
                continue;
            }
            if (itsTask) {
                TaskType taskType = TaskType.valueOf(line.split(",")[1]);
                switch (taskType) {
                    case EPIC:
                        epics.add(line);
                        break;
                    case SUBTASK:
                        subtasks.add(line);
                        break;
                    case TASK:
                        tasks.add(line);
                        break;
                }
//                Task task = fromString(line);
            }
            else {
                lineOfHistory = line;
            }
        }

        for (String epicLine : epics) {
            Epic epic = (Epic) fromString(epicLine, TaskType.EPIC, fileBackedTasksManager);
            fileBackedTasksManager.addEpic(epic);
        }
        for (String subtaskLine : subtasks) {
            Subtask subtask = (Subtask) fromString(subtaskLine, TaskType.SUBTASK, fileBackedTasksManager);
            fileBackedTasksManager.addSubtask(subtask);
        }
        for (String taskLine : tasks) {
            Task task = fromString(taskLine, TaskType.TASK, fileBackedTasksManager);
            fileBackedTasksManager.addTask(task);
        }
        return fileBackedTasksManager;
    }

    static private Task fromString(String value, TaskType taskType, FileBackedTasksManager fileBackedTasksManager) {
        String[] dataOfTask = value.split(",", 6);
//        TaskType taskType = TaskType.valueOf(dataOfTask[1]);
        int id = Integer.valueOf(dataOfTask[0]);
        String name = dataOfTask[1];
        Status status = Status.valueOf(dataOfTask[3]);
        String description = dataOfTask[4];
        String epicIdString = dataOfTask[5].trim();

        switch (taskType) {
            case TASK:
                return new Task(id, name, description, status);
            case SUBTASK:
                return new Subtask(id, name, description, status, fileBackedTasksManager.getEpic(Integer.valueOf(epicIdString)));
            case EPIC:
                return new Epic(id, name, description);
            default:
                return null;
        }
    }

    @Override
    public List<Task> history() {
        return super.history();
    }

    void save() {
        try (Writer writer = new FileWriter(file)){
            writer.write("id,type,name,status,description,epic\n");
            HashMap<Integer, String> allTasks = new HashMap<>();
            HashMap<Integer, Task> tasks = super.getTasks();
            for (Integer id : tasks.keySet()) {
                allTasks.put(id, tasks.get(id).toStringFromFile());
            }
            HashMap<Integer, Subtask> subtasks = super.getSubtasks();
            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, subtasks.get(id).toStringFromFile());
            }
            HashMap<Integer, Epic> epics = super.getEpics();
            for (Integer id : epics.keySet()) {
                allTasks.put(id, epics.get(id).toStringFromFile());
            }
            for (String value : allTasks.values()) {
                writer.write(String.format("%s\n", value));
            }
            writer.write("\n");
            List<String> s = new ArrayList<>();
            for (Task task : history()) {
                s.add(String.valueOf(task.getId()));
            }
            String hist = String.join(",", s);
            writer.write(hist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }
}