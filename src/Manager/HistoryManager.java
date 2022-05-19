package Manager;

import Model.Task;

import java.util.List;

public interface HistoryManager {

    void addTaskToHistory(Task task);

    List<Task> getHistory();

    int getHistorySize();

    void setHistorySize(int historySize);
}
