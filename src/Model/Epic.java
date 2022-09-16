package Model;


import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private LocalDateTime endTime;
    private ArrayList<Integer> subTasksListOfEpic = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.Epic;
        this.status = TaskStatus.NEW;
    }

    //Расширенный конструктор для создания эпика из файла
    public Epic(int id, String name, TaskStatus status, String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, description);
        this.endTime = endTime;
        this.type = TaskType.Epic;
        setId(id);
        setStatus(status);
        setStartTime(startTime);
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

}
