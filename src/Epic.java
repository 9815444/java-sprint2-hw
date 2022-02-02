import java.util.ArrayList;

public class Epic extends Task{

    ArrayList<Integer> epicSubtasks;

    public Epic(String title, String description) {
        super(title, description, "");
        epicSubtasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }



}
