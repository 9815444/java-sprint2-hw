package tasks;

import manager.Status;
import manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
    }

    public Subtask(String title, String description, Status status, Epic epic, LocalDateTime startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        this.epic = epic;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, String description, Status status, Epic epic, LocalDateTime startTime, Duration duration) {
        super(id, title, description, status, startTime, duration);
        this.epic = epic;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, String description, Status status, Epic epic) {
        super(id, title, description, status);
        this.epic = epic;
        this.taskType = TaskType.SUBTASK;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
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
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, epic.getId(),
                startTime, duration, getEndTime());
    }
}
