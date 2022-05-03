package manager;

import api.KVTaskClient;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {
    KVTaskClient client;
    //    Gson gson = new Gson();
//    Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Node<Task>>() {
//            }.getType(),
//            new NodeJsonAdapter()).create();
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
//            .registerTypeAdapter(
//                    new TypeToken<Node<Task>>() {
//                    }.getType(),
//                    new NodeJsonAdapter()
//            )
            .registerTypeAdapter(
                    LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, context) -> LocalDateTime.parse(json.getAsString())
            )
            .registerTypeAdapter(
                    LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (srs, typeOfSrs, context) -> new JsonPrimitive(srs.toString())
            )
            .registerTypeAdapter(
                    Duration.class,
                    (JsonDeserializer<Duration>) (json, type, context) -> Duration.parse(json.getAsString())
            )
            .registerTypeAdapter(
                    Duration.class,
                    (JsonSerializer<Duration>) (srs, typeOfSrs, context) -> new JsonPrimitive(srs.toString())
            )
            .create();

    public HTTPTaskManager(String url) {
        client = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        try {
//            client.put("manager", gson.toJson(this, FileBackedTasksManager.class));
            String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
            client.put("tasks", jsonTasks);
            String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
            client.put("epics", jsonEpics);
            String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
            client.put("subtasks", jsonSubtasks);
            String jsonHistory = gson.toJson(historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList()));
            client.put("history", jsonHistory);
            String jsonPrioritizedTasks = gson.toJson(prioritizedTasks.stream().map(Task::getId).collect(Collectors.toList()));
            client.put("prioritizedTasks", jsonPrioritizedTasks);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load() throws IOException, InterruptedException {

        tasks.clear();
        subtasks.clear();
        epics.clear();
        prioritizedTasks.clear();

        ArrayList<Task> tasksArray = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasksArray);

        ArrayList<Epic> epicsArray = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addEpics(epicsArray);

        ArrayList<Subtask> subtasksArray = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addSubtasks(subtasksArray);

        ArrayList<Integer> historyArray = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());

//        for (int i = historyArray.size() - 1; i >= 0; i--) {
        for (int i = 0; i < historyArray.size(); i++) {
            Task currentTask = getTaskAllKind(historyArray.get(i), this);
            historyManager.add(currentTask);
        }


//        if (!value.isEmpty()) {
//            HTTPTaskManager httpTaskManager = (HTTPTaskManager) gson.fromJson(client.load("manager"), FileBackedTasksManager.class);
//        }
    }

    private void addTasks(ArrayList<Task> tasksArray) {
        for (Task task : tasksArray) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    private void addEpics(ArrayList<Epic> epicsArray) {
        for (Task epic : epicsArray) {
            epics.put(epic.getId(), (Epic) epic);
        }
    }

    private void addSubtasks(ArrayList<Subtask> subtasksArray) {
        for (Task subtask : subtasksArray) {
            subtasks.put(subtask.getId(), (Subtask) subtask);
            prioritizedTasks.add(subtask);
        }
    }


}

