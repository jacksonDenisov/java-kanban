package Model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task implements Comparable<Task>{
    private int id;
    private String name;
    private String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected TaskType type;

    //Базовый конструктор
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }


    //Базовый конструктор для InMemoryHistoryManager c временными параметрами
    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TaskType.Task;
        this.startTime = startTime;
        this.duration = duration;
    }


    //Расширенный конструктор для создания задачи из файла
    public Task(int id, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.Task;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public int compareTo(Task o) {
        if (this.startTime == null) {
            return 1;
        } else if (o.startTime == null) {
            return -1;
        } else if (this.startTime.isAfter(o.startTime)) {
            return 1;
        } else if(this.startTime.isBefore(o.startTime)) {
            return -1;
        }
        return 0;
    }
}
