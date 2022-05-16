import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, SubTask> subTasksListOfEpic;

    Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subTasksListOfEpic = new HashMap<>();
    }

}
