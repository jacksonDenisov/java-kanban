package Manager;

import java.io.File;


public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getDefaultFileBackManager() {
        return FileBackedTasksManager.loadFromFile(new File("src/FileTasksManagerData/BackUp.csv"));
    }

    public static TaskManager getDefaultHTTPTaskManager() {
        return new HTTPTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
