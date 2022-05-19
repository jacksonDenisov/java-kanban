package Manager;

import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history;
    private static int historySize;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
        historySize = 10;
    }

    public InMemoryHistoryManager(int historySize) {
        history = new ArrayList<>();
        this.historySize = historySize;
    }


    @Override
    public void addTaskToHistory(Task task) {
        if (history.size() < historySize) {
            history.add(task);
        } else {
            history.remove(0);
            for (int i = 0; i < history.size(); i++) {
                history.add(i, history.get(i++));
                history.remove(i++);
            }
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public int getHistorySize() {
        return historySize;
    }

    @Override
    public void setHistorySize(int historySize) {
        InMemoryHistoryManager.historySize = historySize;
    }

}
