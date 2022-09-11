package Model;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    protected TaskType type;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TaskType.Task;
    }

    //Расширенный конструктор для создания задачи из файла
    public Task(int id, String name, TaskStatus status, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.Task;
    }
/*   //Конструктор для создания задачи с типом
    public Task(String name, String description, TaskType type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = TaskStatus.NEW;
    }*/


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

}
