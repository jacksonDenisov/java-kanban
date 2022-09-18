package Manager;

import KV_Client_Server.KVTaskClient;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*Написал API HttpTaskServer  и проверил на FileBackedTasksManager, с сервером затрудняюсь придумать,
        как лучше обмениваться данными задач и как лучше их хранить на сервере*/

public class HTTPTaskManager extends FileBackedTasksManager {
    private KVTaskClient kvTaskClient;
    private String url;


    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        this.url = url;
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        List<String> allTasks = new ArrayList<>();
        for (Task task : tasksList.values()) {
            allTasks.add(taskToString(task));
        }
        for (Epic epic : epicsList.values()) {
            updateEpicStatus(epic.getId());
            updateEpicStartEndTime(epic.getId());
            allTasks.add(taskToString(epic));
        }
        for (SubTask subTask : subTasksList.values()) {
            allTasks.add(taskToString(subTask));
        }
        kvTaskClient.put("tasks", gson.toJson(allTasks));
        kvTaskClient.put("history", gson.toJson(historyToString(getHistoryManager())));
    }


    public HTTPTaskManager loadFromKVServer() throws IOException, InterruptedException {
        HTTPTaskManager newHTTPTaskManager = new HTTPTaskManager(url);
        String tasks = kvTaskClient.load("tasks");
        String history = kvTaskClient.load("history");
        List<String> allTasks = gson.fromJson(tasks, List.class);
        int maxId = 0;
        for (String taskString : allTasks) {
            Task task = fromString(taskString);
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
            switch (task.getType()) {
                case Task:
                    newHTTPTaskManager.tasksList.put(task.getId(), task);
                    newHTTPTaskManager.prioritizedTasks.add(task);
                    break;
                case Epic:
                    newHTTPTaskManager.epicsList.put(task.getId(), (Epic) task);
                    break;
                case SubTask:
                    SubTask subTask = (SubTask) task;
                    newHTTPTaskManager.subTasksList.put(task.getId(), subTask);
                    newHTTPTaskManager.prioritizedTasks.add(subTask);
                    Epic parentEpic = newHTTPTaskManager.epicsList.get(subTask.getParentEpicId());
                    parentEpic.addSubTaskInSubTaskListOfEpic(subTask.getId());
                    newHTTPTaskManager.updateEpicStatus(subTask.getParentEpicId());
                    break;
                default:
                    System.out.println("Не удалось определить тип задачи: " + task.getType());
            }
        }
        /* Создаем историю */
        String[] historyArray = gson.fromJson(history, String.class).split(",");
        for (int i = 0; i < historyArray.length; i++) {
            int id = Integer.parseInt(historyArray[i]);
            if (newHTTPTaskManager.tasksList.containsKey(id)) {
                newHTTPTaskManager.getTask(id);
            } else if (newHTTPTaskManager.epicsList.containsKey(id)) {
                newHTTPTaskManager.getEpic(id);
            } else if (newHTTPTaskManager.subTasksList.containsKey(id)) {
                newHTTPTaskManager.getSubTask(id);
            }
        }
        newHTTPTaskManager.setMaxId(maxId);
        return newHTTPTaskManager;
    }
}
