package tasks;

public class Subtask extends Task {
    public Epic epic;

    public Subtask(String title, String description, String status) {
        super(title, description, status);
    }

    public Subtask(String title, String description, String status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic=" + epic +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
