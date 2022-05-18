package Model;

public class SubTask extends Task {
    private int parentEpicId;

    public SubTask(String taskName, String taskDescription, int parentEpicId) {
        super(taskName, taskDescription);
        this.parentEpicId = parentEpicId;
    }

    public int getParentEpicId() {
        return parentEpicId;
    }

    public void setParentEpicId(int parentEpicId) {
        this.parentEpicId = parentEpicId;
    }
}
