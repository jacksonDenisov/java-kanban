package Model;

public class SubTask extends Task {
    private int parentEpicId;

    public SubTask(String taskName, String taskDescription, int parentEpicId) {
        super(taskName, taskDescription);
        this.type = TaskType.SubTask;
        this.parentEpicId = parentEpicId;
    }

    //Расширенный конструктор для создания эпика из файла
    public SubTask(int id, String name, TaskStatus status, String description, int parentEpicId) {
        super(id, name, status, description);
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
