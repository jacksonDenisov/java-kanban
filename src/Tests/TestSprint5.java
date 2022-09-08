package Tests;

import Manager.Managers;
import Manager.TaskManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import Model.TaskStatus;

import java.util.ArrayList;
import java.util.List;


public class TestSprint5 {


    public static void smokeTest() {

        //Тестовый вызов функций для проверки и отладки программы
        //Создание объектов
        TaskManager taskManager = Managers.getDefault();
        //2 Таски
        taskManager.createTask(new Task("Название задачи 1", "Описание задачи 1"));
        taskManager.createTask(new Task("Название задачи 2", "Описание задачи 2"));

        //Эпик с 3 подзадачами
        taskManager.createEpic(new Epic("Название эпика 3", "Описание эпика 3"));

        taskManager.createSubTask(new SubTask("Название сабтаски 4", "Описание сабтаски 4", 3));
        taskManager.createSubTask(new SubTask("Название сабтаски 5", "Описание сабтаски 5", 3));
        taskManager.createSubTask(new SubTask("Название сабтаски 6", "Описание сабтаски 6", 3));

        //Эпик без подзадач
        taskManager.createEpic(new Epic("Название эпика 7", "Описание эпика 7"));

        //Проверка получения истории вызова объектов
        taskManager.getTask(1);
        List<Task> history1 = taskManager.getHistory(); //ожидаю 1 элемент таск1

        taskManager.getEpic(3);
        List<Task> history2 = taskManager.getHistory(); //ожидаю 2 элемента последний эпик3

        taskManager.getEpic(3);
        List<Task> history3 = taskManager.getHistory(); //ожидаю 2 элементы последний эпик3

        taskManager.getSubTask(4);
        List<Task> history4 = taskManager.getHistory(); //ожидаю 3 элемента последний сабтаска4

        taskManager.getTask(1);
        List<Task> history5 = taskManager.getHistory(); // ожидаю 3 эелемента, последний таск1

        taskManager.getEpic(3);
        List<Task> history6 = taskManager.getHistory(); // ожидаю 3 элемента, последний - эпик3

        taskManager.getTask(2);
        List<Task> history7 = taskManager.getHistory(); // ожидаю 4 элемента, последний - задача2

        taskManager.getEpic(7);
        List<Task> history8 = taskManager.getHistory(); //ожидаю 5 элементов, последний - эпик7

        taskManager.getSubTask(5);
        List<Task> history9 = taskManager.getHistory(); //ожидаю 6 элементов последний сабтаска5

        taskManager.getSubTask(6);
        List<Task> history10 = taskManager.getHistory(); //ожидаю 7 элементов последний сабтаска6

        //taskManager.removeTask(1);
        //taskManager.removeEpic(3);
        //taskManager.removeSubTask(6);
        //taskManager.clearSubTasksList();
        //taskManager.clearTasksList();
        //taskManager.clearEpicsList();
        List<Task> history11 = taskManager.getHistory();

        System.out.println(history1);
        System.out.println("end");

    }
}
