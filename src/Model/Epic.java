package Model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksListOfEpic = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.Epic;
        //subTasksListOfEpic = new ArrayList<>();
    }

    //Расширенный конструктор для создания эпика из файла
    public Epic(int id, String name, TaskStatus status, String description) {
        super(id, name, status, description);
        this.type = TaskType.Epic;
    }

/*    public Epic(String name, String description, TaskType type) {
        super(name, description, type);
        subTasksListOfEpic = new ArrayList<>();
    }*/

    public ArrayList<Integer> getSubTasksListOfEpic() {
        return subTasksListOfEpic;
    }

    public void setSubTasksListOfEpic(ArrayList<Integer> subTasksListOfEpic) {
        this.subTasksListOfEpic = subTasksListOfEpic;
    }

    public void addSubTaskInSubTaskListOfEpic(Integer id) {
        subTasksListOfEpic.add(id);
    }

    public void removeSubTaskInSubTaskListOfEpic(Integer id) {
        subTasksListOfEpic.remove(id);
    }

}
