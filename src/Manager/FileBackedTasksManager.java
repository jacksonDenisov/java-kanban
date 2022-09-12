package Manager;

import Exceptions.ManagerSaveException;
import Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;


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
            throw new ManagerSaveException("Ошибка записи");
        }
        //Создание задач из отдельной строки
        int maxId = 0;
        for (int i = 1; i < history.size() - 2; i++) {
            Task task = fileBackedTasksManager.fromString(history.get(i));
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
            switch (task.getType()) {
                case Task:
                    fileBackedTasksManager.tasksList.put(task.getId(), task);
                    break;
                case Epic:
                    fileBackedTasksManager.epicsList.put(task.getId(), (Epic) task);
                    break;
                case SubTask:
                    SubTask subTask = (SubTask) task;
                    fileBackedTasksManager.subTasksList.put(task.getId(), subTask);
                    Epic parentEpic = fileBackedTasksManager.epicsList.get(subTask.getParentEpicId());
                    parentEpic.addSubTaskInSubTaskListOfEpic(subTask.getId());
                    fileBackedTasksManager.updateEpicStatus(subTask.getParentEpicId());
                    break;
                default:
                    System.out.println("Не удалось определить тип задачи: " + task.getType());
            }
        }
        //Создание истории
        List<Integer> historyList = historyFromString(history.get(history.size() - 1));
        for (int i = 0; i < historyList.size(); i++) {
            int id = historyList.get(i);
            if (fileBackedTasksManager.tasksList.containsKey(id)) {
                fileBackedTasksManager.getTask(id);
            } else if (fileBackedTasksManager.epicsList.containsKey(id)) {
                fileBackedTasksManager.getEpic(id);
            } else if (fileBackedTasksManager.subTasksList.containsKey(id)) {
                fileBackedTasksManager.getSubTask(id);
            }
        }
        fileBackedTasksManager.setMaxId(maxId);
        return fileBackedTasksManager;
    }


    private void setMaxId(int id) {
        FileBackedTasksManager.id = id;
    }


    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,parentEpic" + "\n");
            for (Task task : tasksList.values()) {
                fileWriter.write(taskToString(task) + "\n");
            }
            for (Epic epic : epicsList.values()) {
                fileWriter.write(taskToString(epic) + "\n");
            }
            for (SubTask subTask : subTasksList.values()) {
                fileWriter.write(taskToString(subTask) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи");
        }
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
            int parentEpicId = subTasksList.get(task.getId()).getParentEpicId();
            result += "," + parentEpicId;
        }
        return result;
    }


    private Task fromString(String value) {
        Task task = null;
        String[] elements = value.split(",");
        switch (elements[1]) {
            case "Task":
                task = new Task(Integer.parseInt(elements[0]), elements[2], TaskStatus.valueOf(elements[3]),
                        elements[4]);
                break;
            case "Epic":
                task = new Epic(Integer.parseInt(elements[0]), elements[2], TaskStatus.valueOf(elements[3]),
                        elements[4]);
                break;
            case "SubTask":
                task = new SubTask(Integer.parseInt(elements[0]), elements[2], TaskStatus.valueOf(elements[3]),
                        elements[4], Integer.parseInt(elements[5]));
                break;
            default:
                System.out.println("Не удалось создать задачу из строки: " + value);
        }
        return task;
    }

}