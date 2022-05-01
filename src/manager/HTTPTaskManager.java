package manager;

import api.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

public class HTTPTaskManager extends FileBackedTasksManager {
    KVTaskClient client;
    Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Node<Task>>(){}.getType(),
            new NodeJsonAdapter()).create();

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

