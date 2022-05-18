package Manager;

import Model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasksList;
    private HashMap<Integer, Epic> epicsList;
    private HashMap<Integer, SubTask> subTasksList;
    private static int id;

    public TaskManager() {
        tasksList = new HashMap<>();
        epicsList = new HashMap<>();
        subTasksList = new HashMap<>();
        id = 0;
    }

    public static int generateId() {
        id++;
        return id;
    }

    //Методы для Model.Task
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(this.tasksList.values());
    }

    public void clearTasksList() {
        tasksList.clear();
    }

    public Task getTaskById(int id) {
        return tasksList.get(id);
    }

    public void createTask(Task task) {
        tasksList.put(task.getId(), task);
    }

    public void updateTask(Task newTask) {
        tasksList.put(newTask.getId(), newTask);
    }

    public void removeTaskById(int id) {
        tasksList.remove(id);
    }


    //Методы для Model.Epic
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(this.epicsList.values());
    }

    public void clearEpicsList() {
        subTasksList.clear();
        epicsList.clear();
    }

    public Epic getEpicById(int id) {
        return epicsList.get(id);
    }

    public void createEpic(Epic epic) {
        epicsList.put(epic.getId(), epic);
    }

    public void updateEpic(Epic newEpic) {
        Epic oldEpic = epicsList.get(newEpic.getId());
        newEpic.setSubTasksListOfEpic(oldEpic.getSubTasksListOfEpic());
        epicsList.put(newEpic.getId(), newEpic);
    }

    public void removeEpicById(int id) {
        for (Integer subTaskInEpicId : epicsList.get(id).getSubTasksListOfEpic()) {
            subTasksList.remove(subTaskInEpicId);
        }
        epicsList.remove(id);
    }

    public ArrayList<Integer> getSubTasksListOfEpic(int id) {
        return epicsList.get(id).getSubTasksListOfEpic();
    }


    //Методы для Model.SubTask
    public ArrayList<SubTask> getSubTasksList() {
        return new ArrayList<>(this.subTasksList.values());
    }

    public void clearSubTasksList() {
        for (Integer id : epicsList.keySet()) {
            epicsList.get(id).getSubTasksListOfEpic().clear();
            updateEpicStatus(id);
        }
        subTasksList.clear();
    }

    public SubTask getSubTaskById(int id) {
        return subTasksList.get(id);
    }

    public void createSubTask(SubTask subTask) {
        subTasksList.put(subTask.getId(), subTask);
        Epic parentEpic = epicsList.get(subTask.getParentEpicId());
        parentEpic.addSubTaskInSubTaskListOfEpic(subTask.getId());
        updateEpicStatus(parentEpic.getId());
    }

    public void updateSubTask(SubTask newSubTask) {
        int id = newSubTask.getId();
        SubTask oldSubTask = subTasksList.get(id);
        if (oldSubTask.getParentEpicId() == newSubTask.getParentEpicId()){
            subTasksList.put(id, newSubTask);
            updateEpicStatus(newSubTask.getParentEpicId());
        } else {
            Epic oldParentEpic = epicsList.get(oldSubTask.getParentEpicId());
            Epic newParentEpic = epicsList.get(newSubTask.getParentEpicId());
            oldParentEpic.removeSubTaskInSubTaskListOfEpic(id);
            newParentEpic.addSubTaskInSubTaskListOfEpic(id);
            subTasksList.put(id, newSubTask);
            updateEpicStatus(newSubTask.getParentEpicId());
            updateEpicStatus(oldSubTask.getParentEpicId());
        }
    }

    public void removeSubTaskById(int id) {
        Epic parentEpic = epicsList.get(subTasksList.get(id).getParentEpicId());
        parentEpic.removeSubTaskInSubTaskListOfEpic(id);
        subTasksList.remove(id);
        updateEpicStatus(parentEpic.getId());
    }

    private void updateEpicStatus(int id) {
        Epic currentEpic = epicsList.get(id);
        if (currentEpic.getSubTasksListOfEpic().size() != 0) {
            SubTask currentSubTask;
            int newStatusCount = 0;
            int doneStatusCount = 0;
            for (Integer subTaskInEpicId : currentEpic.getSubTasksListOfEpic()) {
                currentSubTask = subTasksList.get(subTaskInEpicId);
                if (currentSubTask.getStatus() == TaskStatus.NEW) {
                    newStatusCount++;
                } else if (currentSubTask.getStatus() == TaskStatus.DONE) {
                    doneStatusCount++;
                }
            }
            if (currentEpic.getSubTasksListOfEpic().size() == newStatusCount) {
                currentEpic.setStatus(TaskStatus.NEW);
            } else if (currentEpic.getSubTasksListOfEpic().size() == doneStatusCount) {
                currentEpic.setStatus(TaskStatus.DONE);
            } else {
                currentEpic.setStatus(TaskStatus.IN_PROGRESS);
            }
        } else {
            currentEpic.setStatus(TaskStatus.NEW);
        }
    }
}
