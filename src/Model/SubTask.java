package Model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int parentEpicId;

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, int parentEpicId) {
        super(name, description, startTime, duration);
        this.type = TaskType.SubTask;
        this.parentEpicId = parentEpicId;
    }

    //Расширенный конструктор для создания эпика из файла
    public SubTask(int id, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration, int parentEpicId) {
        super(id, name, status, description, startTime, duration);
        this.type = TaskType.SubTask;
        this.parentEpicId = parentEpicId;
    }

    public int getParentEpicId() {
        return parentEpicId;
    }

    public void setParentEpicId(int parentEpicId) {
        this.parentEpicId = parentEpicId;
    }
}
