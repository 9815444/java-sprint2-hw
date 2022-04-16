package tasks;

import manager.Status;
import manager.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasks;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, null);
        epicSubtasks = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id, String title, String description) {
        super(id, title, description, null);
        epicSubtasks = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id, String title, Status status, String description) {
        super(id, title, description, null);
        epicSubtasks = new ArrayList<>();
        this.taskType = TaskType.EPIC;
        this.status = status;
    }

    public ArrayList<Integer> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void setEpicSubtasks(ArrayList<Integer> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, "",
                startTime, duration, endTime);
    }
}
