import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasksList;
    HashMap<Integer, Epic> epicsList;
    HashMap<Integer, SubTask> subTasksList;
    static int taskId;

    TaskManager() {
        tasksList = new HashMap<>();
        epicsList = new HashMap<>();
        subTasksList = new HashMap<>();
        taskId = 0;
    }

    public static int generateTaskId() {
        taskId++;
        return taskId;
    }

    //Методы для Task
    public HashMap<Integer, Task> getTasksList() {
        return tasksList;
    }

    public void clearTasksList() {
        tasksList.clear();
    }

    public Task getTaskById(int taskId) {
        return tasksList.get(taskId);
    }

    public void createTask(Task task) {
        tasksList.put(task.getTaskId(), task);
    }

    public void updateTask(int taskId, Task task) {
        tasksList.put(taskId, task);
    }

    public void removeTaskById(int taskId) {
        tasksList.remove(taskId);
    }


    //Методы для Epic
    public HashMap<Integer, Epic> getEpicsList() {
        return epicsList;
    }

    public void clearEpicsList() {
        for (Integer id : epicsList.keySet()) {
            epicsList.get(id).subTasksListOfEpic.clear();
        }
        epicsList.clear();
    }

    public Epic getEpicById(int taskId) {
        return epicsList.get(taskId);
    }

    public void createEpic(Epic epic) {
        epicsList.put(epic.getTaskId(), epic);
    }

    public void updateEpic(int taskId, Epic epic) {
        for (Integer idSubTaskInEpic : epicsList.get(taskId).subTasksListOfEpic.keySet()) {
            subTasksList.remove(idSubTaskInEpic);
        }
        epicsList.put(taskId, epic);
    }

    public void removeEpicById(int taskId) {
        if (epicsList.size() != 0) {
            for (Integer idSubTaskInEpic : epicsList.get(taskId).subTasksListOfEpic.keySet()) {
                subTasksList.remove(idSubTaskInEpic);
            }
            epicsList.get(taskId).subTasksListOfEpic.clear();
            epicsList.remove(taskId);
        }
    }

    public HashMap<Integer, SubTask> getSubTasksListOfEpic(int taskId) {
        return epicsList.get(taskId).subTasksListOfEpic;
    }


    //Методы для SubTask
    public HashMap<Integer, SubTask> getSubTasksList() {
        return subTasksList;
    }

    public void clearSubTasksList() {
        subTasksList.clear();
        for (Integer epicId : epicsList.keySet()) {
            epicsList.get(epicId).subTasksListOfEpic.clear();
            updateEpicStatus(epicId);
        }
    }

    public SubTask getSubTaskById(int taskId) {
        return subTasksList.get(taskId);
    }

    public void createSubTask(SubTask subTask) {
        subTasksList.put(subTask.getTaskId(), subTask);
        epicsList.get(subTask.parentEpicId).subTasksListOfEpic.put(subTask.getTaskId(), subTask);
        updateEpicStatus(subTask.parentEpicId);
    }

    public void updateSubTask(int taskId, SubTask subTask) {
        subTasksList.put(taskId, subTask);
        epicsList.get(subTask.parentEpicId).subTasksListOfEpic.put(taskId, subTask);
        updateEpicStatus(subTask.parentEpicId);
    }

    public void removeSubTaskById(int taskId) {
        if (subTasksList.size() != 0){
            int epicIdForUpdate = subTasksList.get(taskId).parentEpicId;
            subTasksList.remove(taskId);
            updateEpicStatus(epicIdForUpdate);
        }
    }

    public void updateEpicStatus(int taskId) {
        Epic currentEpic = epicsList.get(taskId);
        if (currentEpic.subTasksListOfEpic.size() != 0) {
            SubTask currentSubTask;
            int newStatusCount = 0;
            int doneStatusCount = 0;
            for (Integer idSubTaskInEpic : currentEpic.subTasksListOfEpic.keySet()) {
                currentSubTask = currentEpic.subTasksListOfEpic.get(idSubTaskInEpic);
                if (currentSubTask.getTaskStatus() == TaskStatus.NEW) {
                    newStatusCount++;
                } else if (currentSubTask.getTaskStatus() == TaskStatus.DONE) {
                    doneStatusCount++;
                }
            }
            if (currentEpic.subTasksListOfEpic.size() == newStatusCount) {
                currentEpic.setTaskStatus(TaskStatus.NEW);
            } else if (currentEpic.subTasksListOfEpic.size() == doneStatusCount) {
                currentEpic.setTaskStatus(TaskStatus.DONE);
            } else {
                currentEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        } else {
            currentEpic.setTaskStatus(TaskStatus.NEW);
        }
    }
}
