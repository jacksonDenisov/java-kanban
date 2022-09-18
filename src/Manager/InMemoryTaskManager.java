package Manager;

import Exceptions.ManagerTimeAvailableException;
import Model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasksList;
    protected Map<Integer, Epic> epicsList;
    protected Map<Integer, SubTask> subTasksList;
    protected static int id;
    protected HistoryManager historyManager;
    protected Set<Task> prioritizedTasks;


    public InMemoryTaskManager() {
        tasksList = new HashMap<>();
        epicsList = new HashMap<>();
        subTasksList = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>();
        id = 0;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }


    //Методы для Task
    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(this.tasksList.values());
    }

    @Override
    public void clearTasksList() {
        for (int id : tasksList.keySet()) {
            prioritizedTasks.remove(tasksList.get(id));
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
        try {
            checkIfTimeAvailable(task);
            task.setId(generateId());
            tasksList.put(task.getId(), task);
            prioritizedTasks.add(task);
        } catch (ManagerTimeAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Task newTask) {
        if (tasksList.containsKey(newTask.getId())) {
            try {
                checkIfTimeAvailable(newTask);
                prioritizedTasks.remove(tasksList.get(newTask.getId()));
                tasksList.put(newTask.getId(), newTask);
                prioritizedTasks.add(newTask);
            } catch (ManagerTimeAvailableException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Такого ключа задачи не существует!");
        }
    }

    @Override
    public void removeTask(int id) {
        if (tasksList.containsKey(id)) {
            prioritizedTasks.remove(tasksList.get(id));
            tasksList.remove(id);
            historyManager.removeTaskFromHistory(id);
        } else {
            System.out.println("Не удалось найти задачу с указанным id: " + id);
        }
    }


    //Методы для Epic
    @Override
    public List<Epic> getEpicsList() {
        return new ArrayList<>(this.epicsList.values());
    }

    @Override
    public void clearEpicsList() {
        for (int id : subTasksList.keySet()) {
            prioritizedTasks.remove(subTasksList.get(id));
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
        if (epicsList.containsKey(newEpic.getId())) {
            try {
                Epic oldEpic = epicsList.get(newEpic.getId());
                newEpic.setSubTasksListOfEpic(oldEpic.getSubTasksListOfEpic());
                epicsList.put(newEpic.getId(), newEpic);
            } catch (ManagerTimeAvailableException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Такого ключа эпика не существует!");
        }


    }

    @Override
    public void removeEpic(int id) {
        try {
            for (Integer subTaskInEpicId : epicsList.get(id).getSubTasksListOfEpic()) {
                prioritizedTasks.remove(subTasksList.get(subTaskInEpicId));
                subTasksList.remove(subTaskInEpicId);
                historyManager.removeTaskFromHistory(subTaskInEpicId);
            }
            epicsList.remove(id);
            historyManager.removeTaskFromHistory(id);
        } catch (NullPointerException e) {
            System.out.println("Не удалось найти и удалить Epic с id: " + id);
        }
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
            prioritizedTasks.remove(subTasksList.get(id));
            historyManager.removeTaskFromHistory(id);
        }
        for (Integer id : epicsList.keySet()) {
            epicsList.get(id).getSubTasksListOfEpic().clear();
            updateEpicStatus(id);
            updateEpicStartEndTime(id);
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
        try {
            checkIfTimeAvailable(subTask);
            subTask.setId(generateId());
            subTasksList.put(subTask.getId(), subTask);
            prioritizedTasks.add(subTask);
            Epic parentEpic = epicsList.get(subTask.getParentEpicId());
            parentEpic.addSubTaskInSubTaskListOfEpic(subTask.getId());
            updateEpicStatus(subTask.getParentEpicId());
            updateEpicStartEndTime(subTask.getParentEpicId());
        } catch (ManagerTimeAvailableException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void updateSubTask(SubTask newSubTask) {
        try {
            checkIfTimeAvailable(newSubTask);
            int id = newSubTask.getId();
            SubTask oldSubTask = subTasksList.get(id);
            prioritizedTasks.remove(oldSubTask);
            if (oldSubTask.getParentEpicId() == newSubTask.getParentEpicId()) {
                subTasksList.put(id, newSubTask);
                prioritizedTasks.add(newSubTask);
                updateEpicStatus(newSubTask.getParentEpicId());
                updateEpicStartEndTime(newSubTask.getParentEpicId());
            } else {
                Epic oldParentEpic = epicsList.get(oldSubTask.getParentEpicId());
                Epic newParentEpic = epicsList.get(newSubTask.getParentEpicId());
                oldParentEpic.removeSubTaskInSubTaskListOfEpic(id);
                newParentEpic.addSubTaskInSubTaskListOfEpic(id);
                subTasksList.put(id, newSubTask);
                prioritizedTasks.add(newSubTask);
                updateEpicStatus(newSubTask.getParentEpicId());
                updateEpicStartEndTime(newSubTask.getParentEpicId());
                updateEpicStatus(oldSubTask.getParentEpicId());
                updateEpicStartEndTime(oldSubTask.getParentEpicId());
            }
        } catch (ManagerTimeAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubTask(int id) {
        try {
            int parentEpicId = subTasksList.get(id).getParentEpicId();
            Epic parentEpic = epicsList.get(parentEpicId);
            parentEpic.removeSubTaskInSubTaskListOfEpic(id);
            prioritizedTasks.remove(subTasksList.get(id));
            subTasksList.remove(id);
            updateEpicStatus(parentEpicId);
            updateEpicStartEndTime(parentEpicId);
            historyManager.removeTaskFromHistory(id);
        } catch (NullPointerException e) {
            System.out.println("Не удалось найти и удалить SubTask с id: " + id);
        }

    }

    protected void updateEpicStatus(int id) {
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


    protected void updateEpicStartEndTime(int id) {
        Epic currentEpic = epicsList.get(id);
        if (currentEpic.getSubTasksListOfEpic().isEmpty()) {
            currentEpic.setStartTime(null);
            currentEpic.setEndTime(null);
            currentEpic.setDuration(null);
            return;
        }
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        Duration duration = Duration.ZERO;
        for (int subTaskId : currentEpic.getSubTasksListOfEpic()) {
            duration = duration.plus(subTasksList.get(subTaskId).getDuration());
            if (subTasksList.get(subTaskId).getStartTime().isBefore(startTime)) {
                startTime = subTasksList.get(subTaskId).getStartTime();
            }
            if (subTasksList.get(subTaskId).getEndTime().isAfter(endTime)) {
                endTime = subTasksList.get(subTaskId).getEndTime();
            }
        }
        currentEpic.setDuration(duration);
        currentEpic.setStartTime(startTime);
        currentEpic.setEndTime(endTime);
    }

    protected void checkIfTimeAvailable(Task task) {
        for (Task taskTemp : prioritizedTasks) {
            // a.start < b.end && a.end > b.start тогда пересекаются и время недоступно
            if (taskTemp.getStartTime().isBefore(task.getEndTime()) &&
                    taskTemp.getEndTime().isAfter(task.getStartTime())) {
                throw new ManagerTimeAvailableException("Задача " + task.getName()
                        + " пересекается по времени с задачей " + taskTemp.getName());
            }
        }
    }

    private static int generateId() {
        id++;
        return id;
    }
}
