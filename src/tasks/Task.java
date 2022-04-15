package tasks;

import manager.Status;
import manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    protected TaskType taskType;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public Task(String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        //new
        this.taskType = TaskType.TASK;
        //
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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
