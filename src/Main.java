import api.HttpTaskServer;
import api.KVServer;
import api.KVTaskClient;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalTime.now;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(
                        new TypeToken<Node<Task>>() {
                        }.getType(),
                        new NodeJsonAdapter()
                )
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

//        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("./resources/data.csv"));
//        FileBackedTasksManager manager = (FileBackedTasksManager) Managers.getDefault();
        new KVServer().start();
        HTTPTaskManager manager = (HTTPTaskManager) Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);

//        manager.load();
//        KVTaskClient client = new KVTaskClient("http://localhost:8078/");
//        client.put("manager", gson.toJson(manager, FileBackedTasksManager.class));
//        manager = gson.fromJson(client.load("manager"), FileBackedTasksManager.class); //почему ошибка при восстановлении объекта?
//        int f = 1;
//
//        //Without dates
        manager.addTask(new Task("TaskNull", "", Status.DONE));
        manager.addTask(new Task("Task2", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10)));
        manager.addTask(new Task("Task1", "", Status.DONE, LocalDateTime.now().plusHours(1), Duration.ofHours(1)));
////        Task task21 = new Task("Task21", "", Status.NEW, LocalDateTime.now().plusHours(1), Duration.ofHours(1));
////        task21.setId(2);
////        manager.updateTask(task21);
//
//
//        //With dates
////        manager.addTask(new Task("Task1", "", Status.DONE, LocalDateTime.now(), Duration.ofHours(1)));
////        manager.addTask(new Task("Task2", "", Status.NEW, LocalDateTime.now(), Duration.ofHours(2)));
//
//        Epic epic1 = new Epic("Epic1", "");
//        manager.addEpic(epic1);
////
////        //Without dates
//        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
//        manager.addSubtask(subtask11);
//        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask12);
//        Subtask subtask13 = new Subtask("Epic1 Subtask3", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask13);

        //With dates
//        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1,
//                LocalDateTime.of(2022, 1, 1, 1, 1), Duration.ofHours(4));
//        manager.addSubtask(subtask11);
//        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1,
//                LocalDateTime.of(2022, 5, 1, 1, 1), Duration.ofHours(5));
//        manager.addSubtask(subtask12);
//        Subtask subtask13 = new Subtask("Epic1 Subtask3", "", Status.IN_PROGRESS, epic1);
//        manager.addSubtask(subtask13);
//
//        Epic epic2 = new Epic("Epic2", "");
//        manager.addEpic(epic2);
//
//        System.out.println("tasks = " + manager.getTasks());
//        System.out.println("epics = " + manager.getEpics());
//        System.out.println("subtasks = " + manager.getSubtasks());
//        System.out.printf("prioritizedTasks = \n");
//        manager.getPrioritizedTasks().forEach(System.out::println);
//
//        Epic epic;
//        Task task;
//        Subtask subtask;
//
//        epic = manager.getEpic(3);
//        epic = manager.getEpic(3);
//        epic = manager.getEpic(7);
//        epic = manager.getEpic(7);
//        epic = manager.getEpic(7);
//        epic = manager.getEpic(3);
//
//        task = manager.getTask(1);
//        task = manager.getTask(1);
//        task = manager.getTask(2);
//        task = manager.getTask(9);
//
//        subtask = manager.getSubtask(4);
//
////        manager.deleteEpic(3);
//
//
//
//        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("./resources/data.csv"));
//
//        System.out.println("tasks = " + manager.getTasks());
//        System.out.println("epics = " + manager.getEpics());
//        System.out.println("subtasks = " + manager.getSubtasks());
//        System.out.println("tasks = " + manager.getTasks());
//
//        System.out.println("history = " + fileBackedTasksManager.history());

    }
}
