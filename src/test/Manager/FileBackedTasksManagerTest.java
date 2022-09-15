package test.Manager;

import Manager.FileBackedTasksManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {

    protected FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File("src/FileTasksManagerData/BackUp.csv")));
    }

    LocalDateTime startTime1 = LocalDateTime.of(2022, 1, 1, 00, 00);
    LocalDateTime startTime2 = LocalDateTime.of(2022, 2, 1, 00, 00);
    Duration duration = Duration.ofMinutes(1);
    File file = new File("src/FileTasksManagerData/FileBackedTasksManagerData.csv");

    @Test
    void checkEmptyTaskList() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        //Пустой список задач
        FileBackedTasksManager fileBackedTasksManager2 = fileBackedTasksManager.loadFromFile(file);
        assertNotNull(fileBackedTasksManager2);
        assertEquals(0, fileBackedTasksManager2.getPrioritizedTasks().size());
    }

    @Test
    void checkEpicWithoutSubTasks() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.createEpic(new Epic("Название эпика 1", "Описание эпика 1"));
        fileBackedTasksManager.getEpic(1);
        FileBackedTasksManager fileBackedTasksManager2 = fileBackedTasksManager.loadFromFile(file);
        assertEquals(1, fileBackedTasksManager2.getEpicsList().size());
        assertEquals(1, fileBackedTasksManager2.getHistory().size());
    }

    @Test
    void checkEmptyHistory() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.createTask(new Task("Название задачи 1", "Описание задачи 1", startTime1, duration1));
        FileBackedTasksManager fileBackedTasksManager2 = fileBackedTasksManager.loadFromFile(file);
        assertEquals(0, fileBackedTasksManager2.getHistory().size());
    }

    @Test
    void checkDefault() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.createTask(new Task("Название задачи 1", "Описание задачи 1", startTime1, duration1));
        fileBackedTasksManager.createEpic(new Epic("Название эпика 2", "Описание эпика 2"));
        fileBackedTasksManager.createSubTask(new SubTask("Название подзадачи 3", "Описание подзадачи 3", startTime2, duration1, 2));
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getSubTask(3);
        FileBackedTasksManager fileBackedTasksManager2 = fileBackedTasksManager.loadFromFile(file);
        assertEquals(2, fileBackedTasksManager2.getHistory().size());
        assertEquals(1, fileBackedTasksManager2.getHistory().get(0).getId());
        assertEquals(2, fileBackedTasksManager2.getPrioritizedTasks().size());
    }
}