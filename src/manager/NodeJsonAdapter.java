package manager;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeJsonAdapter extends TypeAdapter<Node<Task>> {
    final Gson gson = new Gson();

    @Override
    public void write(JsonWriter jsonWriter, Node<Task> node) throws IOException {
        jsonWriter.beginArray();
        while (node != null) {
            gson.toJson(gson.toJsonTree(node.data), jsonWriter);
            node = node.next;
        }
        jsonWriter.endArray();
    }

    @Override
    public Node<Task> read(JsonReader jsonReader) throws IOException {
        ArrayList<Node<Task>> list = gson.fromJson(jsonReader, new TypeToken<ArrayList<Node<Task>>>(){}.getType());
        for (int i = 1; i < list.size(); i++) {
            var prev = list.get(i-1);
            var curr = list.get(i-1);
            prev.next = curr;
            curr.prev = prev;
        }
        return list.stream().findFirst().orElse(null);
    }
}
