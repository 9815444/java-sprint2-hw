package manager;

import api.KVTaskClient;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTaskManager extends FileBackedTasksManager {
    KVTaskClient client;
    //    Gson gson = new Gson();
//    Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Node<Task>>() {
//            }.getType(),
//            new NodeJsonAdapter()).create();
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

    public HTTPTaskManager(String url) {
        client = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        try {
            client.put("manager", gson.toJson(this, FileBackedTasksManager.class));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load() throws IOException, InterruptedException {
        String value = client.load("manager");
        if (!value.isEmpty()) {
            HTTPTaskManager httpTaskManager = (HTTPTaskManager) gson.fromJson(client.load("manager"), FileBackedTasksManager.class);
        }
    }


}

