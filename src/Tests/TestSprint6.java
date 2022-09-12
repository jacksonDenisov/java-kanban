package Tests;

import Manager.FileBackedTasksManager;
import Model.*;

import java.io.File;


public class TestSprint6 {
    public static void smokeTest() {

        File file = new File("src/TasksManagerData/FileBackedTasksManagerData.csv");
        File backUpFile = new File("src/TasksManagerData/BackUp.csv");


        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        fileBackedTasksManager.createTask(new Task("Название задачи 1", "Описание задачи 1"));
        fileBackedTasksManager.createTask(new Task("Название задачи 2", "Описание задачи 2"));

        //Эпик с 3 подзадачами
        fileBackedTasksManager.createEpic(new Epic("Название эпика 3", "Описание эпика 3"));

        fileBackedTasksManager.createSubTask(new SubTask("Название сабтаски 4", "Описание сабтаски 4", 3));
        fileBackedTasksManager.createSubTask(new SubTask("Название сабтаски 5", "Описание сабтаски 5", 3));
        fileBackedTasksManager.createSubTask(new SubTask("Название сабтаски 6", "Описание сабтаски 6", 3));

        //Эпик без подзадач
        fileBackedTasksManager.createEpic(new Epic("Название эпика 7", "Описание эпика 7"));


        //Дополнить историю вызова
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getTask(2);
        //fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getSubTask(4);
        //fileBackedTasksManager.getSubTask(6);
        //fileBackedTasksManager.getEpic(7);

            //Создать еще один менеджер из бэкапа
        FileBackedTasksManager fileBackedTasksManager1 = fileBackedTasksManager.loadFromFile(file);
        System.out.println("end");
    }
}
