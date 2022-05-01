package tests;

import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.HTTPTaskManager;
import manager.Managers;
import manager.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    HTTPTaskManager manager;
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    Gson gson = new Gson();

    @BeforeEach
    void initialize() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = (HTTPTaskManager) Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(10);
        Task task1 = new Task("Task1", "", Status.NEW, start, duration);
        manager.addTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task2 = gson.fromJson(response.body(), Task.class);
        assertEquals(task1, task2);
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(10);
        Task task1 = new Task("Task1", "", Status.NEW, LocalDateTime.of(2022, 1, 1, 1, 1, 1), duration);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.NEW, LocalDateTime.of(2022, 2, 1, 1, 1, 1), duration);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String tasksjson = new GsonBuilder().setPrettyPrinting().create().toJson(manager.getTasks());
        assertEquals(tasksjson, response.body());
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Epic2", "");
        manager.addEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String epicsjson = new GsonBuilder().setPrettyPrinting().create().toJson(manager.getEpics());
        assertEquals(epicsjson, response.body());
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);

        Epic epic2 = new Epic("Epic2", "");
        manager.addEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String subtaskJson = new GsonBuilder().setPrettyPrinting().create().toJson(manager.getSubtasks());
        assertEquals(subtaskJson, response.body());
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic2 = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic1, epic2);
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask112 = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask11, subtask112);
    }

    @Test
    void postTask() throws IOException, InterruptedException {

        Duration duration = Duration.ofMinutes(10);
        Task task1 = new Task("Task1", "", Status.NEW, LocalDateTime.of(2022, 1, 1, 1, 1, 1), duration);
        Task task2 = new Task("Task2", "", Status.NEW, LocalDateTime.of(2022, 2, 1, 1, 1, 1), duration);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        task1.setId(1);
        assertEquals(task1, manager.getTask(1));

        task2.setId(1);
        json = gson.toJson(task2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(task2, manager.getTask(1));

    }

    @Test
    void postEpic() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic1", "");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        epic1 = new Epic(1, "Epic1", Status.NEW, "");
        assertEquals(epic1, manager.getEpic(1));

        Epic epic2 = new Epic(1, "Epic2", Status.NEW, "---");
        json = gson.toJson(epic2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(epic2, manager.getEpic(1));

    }

    @Test
    void postSubtask() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic1", "");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        epic1 = new Epic(1, "Epic1", Status.NEW, "");
        assertEquals(epic1, manager.getEpic(1));

        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subtask11);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        epic1 = new Epic(1, "Epic1", Status.NEW, "");
        Subtask sub = manager.getSubtask(2); //Зависает

//        assertEquals(subtask11, manager.getSubtask(2));

    }

    void addInfo() {
        manager.addTask(new Task("TaskNull", "", Status.DONE));
        manager.addTask(new Task("Task2", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10)));
        manager.addTask(new Task("Task1", "", Status.DONE, LocalDateTime.now().plusHours(1), Duration.ofHours(1)));
        Epic epic1 = new Epic("Epic1", "");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Epic1 Subtask2", "", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask12);
    }

    @AfterEach
    void stopServer() {
        kvServer.stop();
        httpTaskServer.stop();
    }
}
