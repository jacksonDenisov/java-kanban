package Model;

import java.util.Comparator;

public class TaskStartTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {

        if (task1.startTime == null) {
            return 1;
        } else if (task2.startTime == null) {
            return -1;
        } else if (task1.startTime.isAfter(task2.startTime)) {
            return 1;
        } else {
            return -1;
        }
    }
}
