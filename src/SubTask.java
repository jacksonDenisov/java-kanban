public class SubTask extends Task {
    int parentEpicId;

    SubTask(String taskName, String taskDescription, int parentEpicId) {
        super(taskName, taskDescription);
        this.parentEpicId = parentEpicId;
    }

}
