package Manager;

import Exceptions.ManagerSaveException;
import Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private File file;


    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }


    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }


    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }


    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }


    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }


    @Override
    public void clearTasksList() {
        super.clearTasksList();
        save();
    }


    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }


    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }


    @Override
    public void clearEpicsList() {
        super.clearEpicsList();
        save();
    }


    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }


    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        save();
    }


    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }


    @Override
    public void clearSubTasksList() {
        super.clearSubTasksList();
        save();
    }


    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        List<String> history = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while (fileReader.ready()) {
                history.add(fileReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Создание задач из отдельной строки
        for (int i = 1; i < history.size() - 2; i++) {
            Task task = fileBackedTasksManager.fromString(history.get(i));
            switch (task.getType()) {
                case Task:
                    fileBackedTasksManager.createTask(task);
                    //fileBackedTasksManager.getTask(task.getId());
                    break;
                case SubTask:
                    fileBackedTasksManager.createSubTask((SubTask) task);
                    break;
                case Epic:
                    fileBackedTasksManager.createEpic((Epic) task);
                    break;
                default:
                    System.out.println("Не удалось определить тип задачи: " + task.getType());
            }
        }
        //Создание истории
        List<Integer> historyList = historyFromString(history.get(history.size() - 1));
        List<Task> allTasks = fileBackedTasksManager.getAllTasks();
        for (int i = 0; i < historyList.size(); i++) {
            Task task = null;
            for (Task tempTask : allTasks) {
                if (tempTask.getId() == historyList.get(i)) {
                    task = tempTask;
                    break;
                }
            }
            switch (task.getType()) {
                case Task:
                    fileBackedTasksManager.getTask(task.getId());
                    break;
                case SubTask:
                    fileBackedTasksManager.getSubTask(task.getId());
                    break;
                case Epic:
                    fileBackedTasksManager.getEpic(task.getId());
                    break;
                default:
                    System.out.println("Не удалось определить тип задачи: " + task.getType());
            }
        }
        //fileBackedTasksManager.save();
        return fileBackedTasksManager;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,parentEpic" + "\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(taskToString(task) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи");
        }
    }


    private List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        TasksIdComparator tasksIdComparator = new TasksIdComparator();
        for (Task task : getTasksList()) {
            allTasks.add(task);
        }
        for (Epic epic : getEpicsList()) {
            allTasks.add(epic);
        }
        for (SubTask subTask : getSubTasksList()) {
            allTasks.add(subTask);
        }
        allTasks.sort(tasksIdComparator);
        return allTasks;
    }


    private static String historyToString(HistoryManager manager) {
        String result = "";
        List<Task> taskList = new ArrayList<>(manager.getHistory());
        if (!taskList.isEmpty()) {
            result += taskList.get(0).getId();
            for (int i = 1; i < taskList.size(); i++) {
                result += "," + taskList.get(i).getId();
            }
        }
        return result;
    }


    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] elements = value.split(",");
        for (int i = 0; i < elements.length; i++) {
            history.add(Integer.parseInt(elements[i]));
        }
        return history;
    }


    private String taskToString(Task task) {
        String result = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription();
        if (task.getType() == TaskType.SubTask) {
            int parentEpicId = 0;
            for (SubTask subTask : getSubTasksList()) {
                if (subTask.getId() == task.getId()) {
                    parentEpicId = subTask.getParentEpicId();
                }
            }
            result += "," + parentEpicId;
        }
        return result;
    }


    private Task fromString(String value) {
        Task task = null;
        String[] elements = value.split(",");
        switch (elements[1]) {
            case "Task":
                task = new Task(Integer.parseInt(elements[0]), elements[2], stringToTaskStatus(elements[3]),
                        elements[4]);
                break;
            case "Epic":
                task = new Epic(Integer.parseInt(elements[0]), elements[2], stringToTaskStatus(elements[3]),
                        elements[4]);
                break;
            case "SubTask":
                task = new SubTask(Integer.parseInt(elements[0]), elements[2], stringToTaskStatus(elements[3]),
                        elements[4], Integer.parseInt(elements[5]));
                break;
            default:
                System.out.println("Не удалось создать задачу из строки: " + value);
        }
        return task;
    }


    private TaskStatus stringToTaskStatus(String value) {
        TaskStatus taskStatus = null;
        switch (value) {
            case "NEW":
                taskStatus = TaskStatus.NEW;
                break;
            case "IN_PROGRESS":
                taskStatus = TaskStatus.IN_PROGRESS;
                break;
            case "DONE":
                taskStatus = TaskStatus.DONE;
                break;
            default:
                System.out.println("Такого статуса пока не существует: " + value);
        }
        return taskStatus;
    }

}