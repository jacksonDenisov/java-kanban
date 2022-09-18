package test.Manager;

import KV_Client_Server.KVServer;
import Manager.FileBackedTasksManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class CreateFileForHTTPTesting {

    LocalDateTime startTime1 = LocalDateTime.of(2022, 1, 1, 00, 00);
    LocalDateTime startTime2 = LocalDateTime.of(2022, 2, 1, 00, 00);
    private Duration duration = Duration.ofMinutes(1);

    @Test
    public void create() {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(
                new File("src/FileTasksManagerData/BackUp.csv"));


        fileBackedTasksManager.createTask(new Task("Название задачи 1", "Описание задачи 1", startTime1, duration));
        fileBackedTasksManager.createEpic(new Epic("Epic name 2", "Epic description 2"));
        fileBackedTasksManager.createSubTask(new SubTask("SubTask name 3", "SubTask description 3", startTime2, duration, 2));

        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getEpic(2);
    }

    @Test
    public void startKVServer() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        kvServer.stop();
    }
}
