package Manager;

import Model.*;

import java.util.Collection;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    Collection<Task> getPrioritizedTasks();

    //Методы для Task
    List<Task> getTasksList();

    void clearTasksList();

    Task getTask(int id);

    void createTask(Task task);

    void updateTask(Task newTask);

    void removeTask(int id);

    //Методы для Epic
    List<Epic> getEpicsList();

    void clearEpicsList();

    Epic getEpic(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic newEpic);

    void removeEpic(int id);

    List<Integer> getSubTasksListOfEpic(int id);


    //Методы для SubTask
    List<SubTask> getSubTasksList();

    void clearSubTasksList();

    SubTask getSubTask(int id);

    void createSubTask(SubTask subTask);

    void updateSubTask(SubTask newSubTask);

    void removeSubTask(int id);

}
