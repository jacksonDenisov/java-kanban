import Model.*;
import Manager.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //Тестовый вызов функций для проверки и отладки программы
        //Создание объектов
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Название задачи 1", "Описание задачи 1"));
        taskManager.createTask(new Task("Название задачи 2", "Описание задачи 2"));

        taskManager.createEpic(new Epic("Название эпика 3", "Описание эпика 3"));
        taskManager.createSubTask(new SubTask("Название сабтаски 4", "Описание сабтаски 4", 3));

        taskManager.createEpic(new Epic("Название эпика 5", "Описание эпика 5"));
        taskManager.createSubTask(new SubTask("Название сабтаски 6", "Описание сабтаски 6", 5));
        taskManager.createSubTask(new SubTask("Название сабтаски 7", "Описание сабтаски 7", 5));

        //Изменение статусов объектов

        taskManager.getTaskById(1).setStatus(TaskStatus.IN_PROGRESS);
        taskManager.getTaskById(2).setStatus(TaskStatus.DONE);

        taskManager.getSubTaskById(4).setStatus(TaskStatus.IN_PROGRESS);
        taskManager.getSubTaskById(6).setStatus(TaskStatus.DONE);
        taskManager.getSubTaskById(7).setStatus(TaskStatus.DONE);

        //Получение списка всех задач
        ArrayList<Task> taskList = taskManager.getTasksList();
        ArrayList<Epic> epicsList = taskManager.getEpicsList();
        ArrayList<SubTask> subTasksList = taskManager.getSubTasksList();

        //Получение по идентификатору

        Task task = taskManager.getTaskById(1);
        Epic epic = taskManager.getEpicById(3);
        SubTask subTask = taskManager.getSubTaskById(4);

        //Получение списка всех подзадач определённого эпика
        ArrayList<Integer> subTasksListOfEpic = taskManager.getSubTasksListOfEpic(5);

        //Обновление объектов
        taskManager.updateTask(new Task("Новое название задачи 1", "Новое описание задачи 1"));
        taskManager.updateEpic(new Epic("Новое название эпика 5", "Новое описание эпика 5"));
        taskManager.updateSubTask(new SubTask("Новое название сабтаски 4", "Новое описание сабтаски 4", 3));

        //Удаление объектов по ID
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);
        taskManager.removeSubTaskById(6);

        //Удаление всех задач
        taskManager.clearTasksList();
        taskManager.clearEpicsList();
        taskManager.clearSubTasksList();
    }
}
