package Manager;

import java.io.File;
import java.io.IOException;


public class Managers {

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HTTPTaskManager("http://localhost:8078/");
    }

    public static FileBackedTasksManager getDefaultFileBackManager() {
        return FileBackedTasksManager.loadFromFile(new File("src/FileTasksManagerData/BackUp.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
