package Manager;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    //Методы для Task
    ArrayList<Task> getTasksList();

    void clearTasksList();

    Task getTask(int id);

    void createTask(Task task);

    void updateTask(Task newTask);

    void removeTask(int id);

    //Методы для Epic
    ArrayList<Epic> getEpicsList();

    void clearEpicsList();

    Epic getEpic(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic newEpic);

    void removeEpic(int id);

    ArrayList<Integer> getSubTasksListOfEpic(int id);


    //Методы для SubTask
    ArrayList<SubTask> getSubTasksList();

    void clearSubTasksList();

    SubTask getSubTask(int id);

    void createSubTask(SubTask subTask);

    void updateSubTask(SubTask newSubTask);

    void removeSubTask(int id);

}
