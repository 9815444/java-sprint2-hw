package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    FileBackedTasksManager manager;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

            String response = "";
            switch (method) {
                case "GET":
                    get(exchange, splitPath, query);
                    break;
                case "POST":
                    post(exchange, splitPath, response);
                    break;
                case "DELETE":
                    delete(exchange, splitPath, query, response);
                    break;
            }
        }

        private void delete(HttpExchange exchange, String[] splitPath, String query, String response) throws IOException {
            if (splitPath.length == 3) {
                if (splitPath[2].equals("task")) {
                    deleteTasks(exchange, query, response);
                }
                if (splitPath[2].equals("epic")) {
                    deleteEpics(exchange, query, response);
                }
                if (splitPath[2].equals("subtask")) {
                    deleteSubtasks(exchange, query, response);
                }
            }
        }

        private void post(HttpExchange exchange, String[] splitPath, String response) throws IOException {
            if (splitPath.length == 3) {
                if (splitPath[2].equals("task")) {
                    postTasks(exchange, response);
                }
                if (splitPath[2].equals("epic")) {
                    postEpics(exchange, response);
                }
                if (splitPath[2].equals("subtask")) {
                    postSubtask(exchange, response);
                }
            }
        }

        private void get(HttpExchange exchange, String[] splitPath, String query) throws IOException {
            if (splitPath.length == 2) {
                String response = gson.toJson(manager.getPrioritizedTasks());
                sendResponse(exchange, 200, response);
            }
            if (splitPath.length == 3) {
                if (splitPath[2].equals("task")) {
                    getTasks(exchange, query);
                }
                if (splitPath[2].equals("epic")) {
                    getEpics(exchange, query);
                }
                if (splitPath[2].equals("subtask")) {
                    getSubtask(exchange, query);
                }
                if (splitPath[2].equals("history")) {
                    String response = gson.toJson(manager.history());
                    sendResponse(exchange, 200, response);
                }
            }
            if (splitPath.length == 4) {
                getEpicSubTasks(exchange, query);
            }
        }

        private void getEpicSubTasks(HttpExchange exchange, String query) throws IOException {
            String response;
            if (query == null) { //Все задачи
                response = gson.toJson(manager.getTasks());
                sendResponse(exchange, 404, response);
            } else { //Определенная
                int id = Integer.valueOf(query.replaceFirst("id=", ""));
                Epic epic = manager.getEpic(id);
                response = gson.toJson(epic.getEpicSubtasks());
                sendResponse(exchange, 200, response);
            }
        }

        private void deleteTasks(HttpExchange exchange, String query, String response) throws IOException {
            if (query == null) {
                manager.deleteAllTasks();
                sendResponse(exchange, 200, response);
            } else {
                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                manager.deleteTask(id);
                sendResponse(exchange, 200, response);
            }
        }

        private void deleteEpics(HttpExchange exchange, String query, String response) throws IOException {
            if (query == null) {
                manager.deleteAllEpics();
                sendResponse(exchange, 200, response);
            } else {
                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                manager.deleteEpic(id);
                sendResponse(exchange, 200, response);
            }
        }

        private void deleteSubtasks(HttpExchange exchange, String query, String response) throws IOException {
            if (query == null) {
                manager.deleteAllSubtask();
                sendResponse(exchange, 200, response);
            } else {
                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                manager.deleteSubtask(id);
                sendResponse(exchange, 200, response);
            }
        }

        private void postTasks(HttpExchange exchange, String response) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(body, Task.class);
            //Если id не передан, то считаем, что это add. Иначе - update
            if (task.getId() == 0) {
                manager.addTask(task);
            } else {
                manager.updateTask(task);
            }
            sendResponse(exchange, 201, response);
            System.out.println(manager.getTasks());
        }

        private void postEpics(HttpExchange exchange, String response) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);
            //Если id не передан, то считаем, что это add. Иначе - update
            if (epic.getId() == 0) {
                manager.addEpic(epic);
            } else {
                manager.updateEpic(epic);
            }
            sendResponse(exchange, 201, response);
            System.out.println(manager.getTasks());
        }

        private void postSubtask(HttpExchange exchange, String response) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            //Если id не передан, то считаем, что это add. Иначе - update
            if (subtask.getId() == 0) {
                manager.addTask(subtask);
            } else {
                manager.updateTask(subtask);
            }
            sendResponse(exchange, 201, response);
            System.out.println(manager.getTasks());
        }

        private void getTasks(HttpExchange exchange, String query) throws IOException {
            String response;
            if (query == null) { //Все задачи
                response = gson.toJson(manager.getTasks());
                sendResponse(exchange, 200, response);
            } else { //Определенная
                int id = Integer.valueOf(query.replaceFirst("id=", ""));
                Task task = manager.getTask(id);
                response = gson.toJson(task);
                sendResponse(exchange, 200, response);
            }
        }

        private void getEpics(HttpExchange exchange, String query) throws IOException {
            String response;
            if (query == null) { //Все задачи
                response = gson.toJson(manager.getEpics());
                sendResponse(exchange, 200, response);
            } else { //Определенная
                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                Task task = manager.getEpic(id);
                response = gson.toJson(task);
                sendResponse(exchange, 200, response);
            }
        }

        private void getSubtask(HttpExchange exchange, String query) throws IOException {
            String response;
            if (query == null) { //Все задачи
                response = gson.toJson(manager.getSubtasks());
                sendResponse(exchange, 200, response);
            } else { //Определенная
                int id = Integer.parseInt(query.replaceFirst("id=", ""));
                Task task = manager.getSubtask(id);
                response = gson.toJson(task);
                sendResponse(exchange, 200, response);
            }
        }
    }

    private static void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

