package Manager;

import Model.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history;
    private static int historySize;

    public InMemoryHistoryManager() {
        history = new LinkedList<>();
        historySize = 10;
    }


    @Override
    public void addTaskToHistory(Task task) {
        if (history.size() >= historySize){
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

}
