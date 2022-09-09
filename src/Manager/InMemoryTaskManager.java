package Manager;

import Model.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasksList;
    private Map<Integer, Epic> epicsList;
    private Map<Integer, SubTask> subTasksList;
    private HistoryManager historyManager;
    private static int id;

    public InMemoryTaskManager() {
        tasksList = new HashMap<>();
        epicsList = new HashMap<>();
        subTasksList = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        id = 0;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    //Методы для Task
    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(this.tasksList.values());
    }

    @Override
    public void clearTasksList() {
        for (int id : tasksList.keySet()) {
            historyManager.removeTaskFromHistory(id);
        }
        tasksList.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.addTaskToHistory(tasksList.get(id));
        return tasksList.get(id);
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasksList.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task newTask) {
        tasksList.put(newTask.getId(), newTask);
    }

    @Override
    public void removeTask(int id) {
        tasksList.remove(id);
        historyManager.removeTaskFromHistory(id);
    }


    //Методы для Epic
    @Override
    public List<Epic> getEpicsList() {
        return new ArrayList<>(this.epicsList.values());
    }

    @Override
    public void clearEpicsList() {
        for (int id : subTasksList.keySet()) {
            historyManager.removeTaskFromHistory(id);
        }
        for (int id : epicsList.keySet()) {
            historyManager.removeTaskFromHistory(id);
        }
        subTasksList.clear();
        epicsList.clear();
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.addTaskToHistory(epicsList.get(id));
        return epicsList.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicsList.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic oldEpic = epicsList.get(newEpic.getId());
        newEpic.setSubTasksListOfEpic(oldEpic.getSubTasksListOfEpic());
        epicsList.put(newEpic.getId(), newEpic);
    }

    @Override
    public void removeEpic(int id) {
        for (Integer subTaskInEpicId : epicsList.get(id).getSubTasksListOfEpic()) {
            subTasksList.remove(subTaskInEpicId);
            historyManager.removeTaskFromHistory(subTaskInEpicId);
        }
        epicsList.remove(id);
        historyManager.removeTaskFromHistory(id);
    }

    @Override
    public List<Integer> getSubTasksListOfEpic(int id) {
        return epicsList.get(id).getSubTasksListOfEpic();
    }


    //Методы для SubTask
    @Override
    public List<SubTask> getSubTasksList() {
        return new ArrayList<>(this.subTasksList.values());
    }

    @Override
    public void clearSubTasksList() {
        for (int id : subTasksList.keySet()) {
            historyManager.removeTaskFromHistory(id);
        }
        for (Integer id : epicsList.keySet()) {
            epicsList.get(id).getSubTasksListOfEpic().clear();
            updateEpicStatus(id);
        }
        subTasksList.clear();
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.addTaskToHistory(subTasksList.get(id));
        return subTasksList.get(id);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasksList.put(subTask.getId(), subTask);
        Epic parentEpic = epicsList.get(subTask.getParentEpicId());
        parentEpic.addSubTaskInSubTaskListOfEpic(subTask.getId());
        updateEpicStatus(subTask.getParentEpicId());
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        int id = newSubTask.getId();
        SubTask oldSubTask = subTasksList.get(id);
        if (oldSubTask.getParentEpicId() == newSubTask.getParentEpicId()) {
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

    @Override
    public void removeSubTask(int id) {
        int parentEpicId = subTasksList.get(id).getParentEpicId();
        Epic parentEpic = epicsList.get(parentEpicId);
        parentEpic.removeSubTaskInSubTaskListOfEpic(id);
        subTasksList.remove(id);
        updateEpicStatus(parentEpicId);
        historyManager.removeTaskFromHistory(id);
    }

    private static int generateId() {
        id++;
        return id;
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
