package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

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

            switch (method) {
                case "GET":
                    if (splitPath.length == 3 && splitPath[2].equals("task")) {
                        if (query.isEmpty()) {

                        } else {
                            int id = Integer.valueOf(query.replaceFirst("id=", ""));
                            Task task = manager.getTask(id);
                            String response = gson.toJson(task);
                            try (OutputStream os = exchange.getResponseBody()){
                                os.write(response.getBytes());
                            }
                        }
                    }
                    break;
            }

        }
    }

}
