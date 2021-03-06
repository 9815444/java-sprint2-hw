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
        this.taskType = TaskType.TASK;
    }

    public Task(int id, String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        Task task = (Task) obj;
        return id == task.id &&
                title.equals(task.title) &&
                description.equals(task.description) &&
                status.equals(task.status) &&
                taskType.equals(task.taskType) &&
                durEquals(duration,task.duration) &&
                timeEquals(startTime,task.startTime);
    }

    protected boolean durEquals(Duration value1, Duration value2) {
        if ((value1 == null) && (value2 == null)) {
            return true;
        } else if ((value1 == null) || (value2 == null)) {
            return false;
        } else {
            return value1.equals(value2);
        }
    }

    protected boolean timeEquals(LocalDateTime value1, LocalDateTime value2) {
        if ((value1 == null) && (value2 == null)) {
            return true;
        } else if ((value1 == null) || (value2 == null)) {
            return false;
        } else {
            return value1.equals(value2);
        }
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", id, taskType, title, status, description, "",
                startTime, duration, getEndTime());
    }

}
