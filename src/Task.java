public class Task {
    String taskName;
    String taskDescription;
    TaskStatus taskStatus;
    private int taskId;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = TaskManager.generateTaskId();
        this.taskStatus = TaskStatus.NEW;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public int getTaskId() {
        return taskId;
    }
}
