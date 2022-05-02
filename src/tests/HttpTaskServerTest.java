package tests;

import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import manager.*;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskServerTest {

    HTTPTaskManager manager;
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
//    Gson gson = new Gson();
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(
                    new TypeToken<Node<Task>>() {
                    }.getType(),
                    new NodeJsonAdapter())
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

        String tasksjson = gson.toJson(manager.getTasks());
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

        String epicsjson = gson.toJson(manager.getEpics());
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

        String subtaskJson = gson.toJson(manager.getSubtasks());
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
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Epic1 Subtask1", "", Status.DONE, epic1, LocalDateTime.now().plusDays(1), Duration.ofHours(1));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtask11);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        subtask11.setId(2);

        Subtask newSubtask = manager.getSubtask(2);
        assertEquals(2, newSubtask.getId());
        assertEquals("Epic1 Subtask1", newSubtask.getTitle());
        assertEquals("", newSubtask.getDescription());

    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(10);
        Task task1 = new Task("Task1", "", Status.NEW, LocalDateTime.of(2022, 1, 1, 1, 1, 1), duration);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.NEW, LocalDateTime.of(2022, 2, 1, 1, 1, 1), duration);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(10);
        Task task1 = new Task("Task1", "", Status.NEW, LocalDateTime.of(2022, 1, 1, 1, 1, 1), duration);
        manager.addTask(task1);
        Task task2 = new Task("Task2", "", Status.NEW, LocalDateTime.of(2022, 2, 1, 1, 1, 1), duration);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getTask(1));
    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        addInfo();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        addInfo();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getEpic(1));
    }

    @Test
    void deleteSubtasks() throws IOException, InterruptedException {
        addInfo();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        addInfo();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getSubtask(5));
    }

    @Test
    void getEpicSubtasks() throws IOException, InterruptedException {
        addInfo();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<Integer> test2 = gson.fromJson(response.body(), new TypeToken<List<Integer>>() {
        }.getType());
        assertEquals(5, test2.get(0));
        assertEquals(6, test2.get(1));
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        addInfo();
        manager.getTask(1);
        manager.getTask(2);//Тут
        manager.getTask(3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<Task> test2 = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(1, test2.get(0).getId());
        assertEquals(2, test2.get(1).getId());
        assertEquals(3, test2.get(2).getId());
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        addInfo();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ArrayList<Task> test2 = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(2, test2.get(0).getId());
        assertEquals(3, test2.get(1).getId());
        assertEquals(5, test2.get(2).getId());
        assertEquals(1, test2.get(3).getId());
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
