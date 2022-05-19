package Model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksListOfEpic;

    public Epic(String name, String description) {
        super(name, description);
        subTasksListOfEpic = new ArrayList<>();
    }

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
