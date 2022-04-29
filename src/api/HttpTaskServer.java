package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    FileBackedTasksManager manager;

    public HttpTaskServer(FileBackedTasksManager manager) throws IOException {
        this.manager = manager;
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TasksHandler implements HttpHandler {
        FileBackedTasksManager manager;

        public TasksHandler(FileBackedTasksManager manager) {
            this.manager = manager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();
            String[] splitPath = exchange.getRequestURI().getPath().split("/");
            String query = exchange.getRequestURI().getQuery();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String response = "";
            switch (method) {
                case "GET":
                    if (splitPath.length == 3 && splitPath[2].equals("task")) {
                        if (query == null) {
                            exchange.sendResponseHeaders(200, 0);
                            response = gson.toJson(manager.getTasks());
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            int id = Integer.valueOf(query.replaceFirst("id=", ""));
                            Task task = manager.getTask(id);
                            exchange.sendResponseHeaders(200, 0);
                            response = gson.toJson(task);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(body, Task.class);
                    //Если id не передан, то считаем, что это add. Иначе - update
                    if (task.getId() == 0) {
                        manager.addTask(task);
                    }
                    else {
                        manager.updateTask(task);
                    }
                    exchange.sendResponseHeaders(201, 0);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    System.out.println(manager.getTasks());
                    break;
                case "DELETE":
                    if (splitPath.length == 3 && splitPath[2].equals("task")) {
                        if (query == null) {
                            manager.deleteAllTasks();
                            exchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            int id = Integer.valueOf(query.replaceFirst("id=", ""));
                            manager.deleteTask(id);
                            exchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = exchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }
                        break;
                    }

            }
        }
    }
}

