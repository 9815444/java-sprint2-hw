package tasks;

import manager.Status;
import manager.TaskType;

import java.util.List;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    protected TaskType taskType;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        //new
        this.taskType = TaskType.TASK;
        //
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, "");
    }
}
