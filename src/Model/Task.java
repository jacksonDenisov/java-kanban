package Model;

import Manager.InMemoryTaskManager;

public class Task {
    private String name;
    private String description;
    private TaskStatus status;
    private int id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = InMemoryTaskManager.generateId();
        this.status = TaskStatus.NEW;
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
}
