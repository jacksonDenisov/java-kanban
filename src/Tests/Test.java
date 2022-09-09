package Tests;

import Manager.Managers;
import Manager.TaskManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import Model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class Test {


    public static void smokeTest() {
/*
        //Тестовый вызов функций для проверки и отладки программы
        //Создание объектов
        TaskManager taskManager = Managers.getDefault();

        taskManager.createTask(new Task("Название задачи 1", "Описание задачи 1"));
        taskManager.createTask(new Task("Название задачи 2", "Описание задачи 2"));


        taskManager.createEpic(new Epic("Название эпика 3", "Описание эпика 3"));
        taskManager.createSubTask(new SubTask("Название сабтаски 4", "Описание сабтаски 4", 3));

        taskManager.createEpic(new Epic("Название эпика 5", "Описание эпика 5"));
        taskManager.createSubTask(new SubTask("Название сабтаски 6", "Описание сабтаски 6", 5));
        taskManager.createSubTask(new SubTask("Название сабтаски 7", "Описание сабтаски 7", 5));

        //Проверка получения истории вызова объектов
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getEpic(3);
        taskManager.getEpic(3);
        taskManager.getEpic(3);
        taskManager.getEpic(3);
        taskManager.getEpic(5);
        taskManager.getSubTask(4);
        taskManager.getSubTask(6);
        taskManager.getSubTask(7);
        List<Task> history = taskManager.getHistory();

        //Изменение статусов объектов

        taskManager.getTask(1).setStatus(TaskStatus.IN_PROGRESS);
        taskManager.getTask(2).setStatus(TaskStatus.DONE);

        taskManager.getSubTask(4).setStatus(TaskStatus.IN_PROGRESS);
        taskManager.getSubTask(6).setStatus(TaskStatus.DONE);
        taskManager.getSubTask(7).setStatus(TaskStatus.DONE);

        //Получение списка всех задач
        ArrayList<Task> taskList = taskManager.getTasksList();
        ArrayList<Epic> epicsList = taskManager.getEpicsList();
        ArrayList<SubTask> subTasksList = taskManager.getSubTasksList();

        //Получение по идентификатору

        Task task = taskManager.getTask(1);
        Epic epic = taskManager.getEpic(3);
        SubTask subTask = taskManager.getSubTask(4);

        //Получение списка всех подзадач определённого эпика
        ArrayList<Integer> subTasksListOfEpic = taskManager.getSubTasksListOfEpic(5);

        //Обновление объектов
        Task newTask = new Task("Новое название задачи 1", "Новое описание задачи 1");
        newTask.setId(1);
        taskManager.updateTask(newTask);
        Epic newEpic = new Epic("Новое название эпика 5", "Новое описание эпика 5");
        newEpic.setId(5);
        taskManager.updateEpic(newEpic);
        SubTask newSubTask = new SubTask("Новое название сабтаски 4", "Новое описание сабтаски 4", 3);
        newSubTask.setId(4);
        taskManager.updateSubTask(newSubTask);

        //Удаление объектов по ID
        taskManager.removeTask(1);
        taskManager.removeEpic(3);
        taskManager.removeSubTask(6);

        //Удаление всех задач
        taskManager.clearTasksList();
        taskManager.clearEpicsList();
        taskManager.clearSubTasksList();
        System.out.println("end");*/
    }
}
