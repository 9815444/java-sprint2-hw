package tests;

import api.HttpTaskServer;
import api.KVServer;
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

public class HttpTaskServerTest {

    HTTPTaskManager manager;
    KVServer kvServer;

    @BeforeEach
    void initialize() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = (HTTPTaskManager) Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        addInfo();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
    }
}
