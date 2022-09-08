package Manager;

import Model.Task;

import java.util.List;

public interface HistoryManager {

    void addTaskToHistory(Task task);

    void removeTaskFromHistory(int id);

    List<Task> getHistory();

}
