package Manager;

import KV_Client_Server.KVTaskClient;
import Model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;


public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    public HTTPTaskManager(String url) {
        this.kvTaskClient = new KVTaskClient(url);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        loadFromKVServer();
    }

    @Override
    protected void save() {
        kvTaskClient.put("tasks", gson.toJson(getTasksList()));
        kvTaskClient.put("epics", gson.toJson(getEpicsList()));
        kvTaskClient.put("subtasks", gson.toJson(getSubTasksList()));
        kvTaskClient.put("history", gson.toJson(historyToString(getHistoryManager())));
    }


    protected void loadFromKVServer() {
        String tasksGson = kvTaskClient.load("tasks");
        String epicsGson = kvTaskClient.load("epics");
        String subtasksGson = kvTaskClient.load("subtasks");
        String historyGson = kvTaskClient.load("history");
        List<String> tasks = gson.fromJson(tasksGson, List.class);
        List<String> epics = gson.fromJson(epicsGson, List.class);
        List<String> subtasks = gson.fromJson(subtasksGson, List.class);
        int maxId = 0;
        if (tasksGson != null) {
            for (String taskString : tasks) {
                Task task = fromString(taskString);
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
                tasksList.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        }
        if (epicsGson != null) {
            for (String epicString : epics) {
                Epic epic = (Epic) fromString(epicString);
                if (epic.getId() > maxId) {
                    maxId = epic.getId();
                }
                epicsList.put(epic.getId(), epic);
            }
        }
        if (subtasksGson != null) {
            for (String subTaskString : subtasks) {
                SubTask subTask = (SubTask) fromString(subTaskString);
                if (subTask.getId() > maxId) {
                    maxId = subTask.getId();
                }
                subTasksList.put(subTask.getId(), subTask);
                prioritizedTasks.add(subTask);
                Epic parentEpic = epicsList.get(subTask.getParentEpicId());
                parentEpic.addSubTaskInSubTaskListOfEpic(subTask.getId());
                updateEpicStatus(subTask.getParentEpicId());
            }
        }
        setMaxId(maxId);
        /* Создаем историю */
        if (historyGson != null) {
            String[] historyArray = gson.fromJson(historyGson, String.class).split(",");
            for (int i = 0; i < historyArray.length; i++) {
                int id = Integer.parseInt(historyArray[i]);
                if (tasksList.containsKey(id)) {
                    getTask(id);
                } else if (epicsList.containsKey(id)) {
                    getEpic(id);
                } else if (subTasksList.containsKey(id)) {
                    getSubTask(id);
                }
            }
        }
    }
}
