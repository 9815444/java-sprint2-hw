package manager;

import api.KVTaskClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

public class HTTPTaskManager extends FileBackedTasksManager{
    KVTaskClient client;
    Gson gson;
    public HTTPTaskManager(String url) {
        client = new KVTaskClient(url);
        //this = (HTTPTaskManager) gson.fromJson(client.load("manager"), FileBackedTasksManager.class);
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
}

