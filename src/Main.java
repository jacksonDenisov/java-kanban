import Manager.FileBackedTasksManager;

import Model.Epic;
import Model.SubTask;
import Model.Task;


import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {

        File file = new File("src/FileTasksManagerData/FileBackedTasksManagerData.csv");
        File backUpFile = new File("src/FileTasksManagerData/BackUp.csv");
        LocalDateTime startTime1 = LocalDateTime.of(2022, 1, 1, 00, 00);
        LocalDateTime startTime2 = LocalDateTime.of(2022, 2, 2, 00, 00);
        LocalDateTime startTime3 = LocalDateTime.of(2022, 3, 3, 00, 00);
        LocalDateTime startTime4 = LocalDateTime.of(2022, 4, 3, 00, 00);
        LocalDateTime startTime5 = LocalDateTime.of(2022, 5, 3, 00, 00);
        LocalDateTime startTime6 = LocalDateTime.of(2022, 6, 3, 00, 00);
        Duration duration1 = Duration.ofMinutes(1);
        Duration duration2 = Duration.ofMinutes(2);
        Duration duration3 = Duration.ofMinutes(3);

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        //InMemoryTaskManager taskManager = new InMemoryTaskManager();

        fileBackedTasksManager.createTask(new Task("Название задачи 1", "Описание задачи 1", startTime1, duration1));
        fileBackedTasksManager.createTask(new Task("Название задачи 2", "Описание задачи 2", startTime2, duration2));
        fileBackedTasksManager.createTask(new Task("Название задачи 3", "Описание задачи 3", startTime3, duration3));

        //Эпик с 3 подзадачами
        fileBackedTasksManager.createEpic(new Epic("Название эпика 4", "Описание эпика 4"));

        fileBackedTasksManager.createSubTask(new SubTask("Название сабтаски 5", "Описание сабтаски 5", startTime3, duration1, 4));
        fileBackedTasksManager.createSubTask(new SubTask("Название сабтаски 6", "Описание сабтаски 6", startTime5, duration2, 4));
        fileBackedTasksManager.createSubTask(new SubTask("Название сабтаски 7", "Описание сабтаски 7", startTime1, duration3, 4));

        //Эпик без подзадач
        //taskManager.createEpic(new Epic("Название эпика 8", "Описание эпика 8"));
        //Collection<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        //System.out.println(prioritizedTasks.toString());

        //Дополнить историю вызова
        //fileBackedTasksManager.getTask(1);
        //fileBackedTasksManager.getTask(2);
        //taskManager.getEpic(4);
        //taskManager.getSubTask(5);
        //taskManager.getSubTask(6);
        //taskManager.getEpic(8);

        //Создать еще один менеджер из бэкапа
        FileBackedTasksManager fileBackedTasksManager1 = fileBackedTasksManager.loadFromFile(file);
        System.out.println("end");

    }
}
