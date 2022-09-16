package test.Manager;

import Manager.InMemoryHistoryManager;
import Model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private static Task task1 = new Task("name1", "description1");
    private static Task task2 = new Task("name2", "description2");
    private static Task task3 = new Task("name3", "description3");

    @BeforeAll
    public static void prepareTasks() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
    }

    @BeforeEach
    public void createNewHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void getHistory() {
        assertEquals(true, historyManager.getHistory().isEmpty());
        historyManager.addTaskToHistory(task1);
        assertEquals(false, historyManager.getHistory().isEmpty());
        historyManager.addTaskToHistory(task2);
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    public void addNewTaskToHistory() {
        historyManager.addTaskToHistory(task1);
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    public void addAnothersTaskToHistory() {
        historyManager.addTaskToHistory(task1);
        assertEquals(task1, historyManager.getHistory().get(0));

        historyManager.addTaskToHistory(task2);
        assertEquals(task2, historyManager.getHistory().get(1));

        historyManager.addTaskToHistory(task3);
        assertEquals(task3, historyManager.getHistory().get(2));
    }

    @Test
    public void addDuplicateTask() {
        historyManager.addTaskToHistory(task1);
        assertEquals(task1, historyManager.getHistory().get(0));
        historyManager.addTaskToHistory(task1);
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    public void removeTheOnlyOneTaskFromHistory() {
        historyManager.addTaskToHistory(task1);
        assertEquals(1, historyManager.getHistory().size());
        historyManager.removeTaskFromHistory(task1.getId());
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void removeTaskFromTheBeginning() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        assertEquals(3, historyManager.getHistory().size());
        //Удаляем задачу из начала
        historyManager.removeTaskFromHistory(task1.getId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
    }

    @Test
    public void removeTaskFromTheEnd() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        assertEquals(3, historyManager.getHistory().size());
        //Удалем задачу из конца
        historyManager.removeTaskFromHistory(task3.getId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task2, historyManager.getHistory().get(1));
    }

    @Test
    public void removeTaskFromTheMiddle() {
        historyManager.addTaskToHistory(task1);
        historyManager.addTaskToHistory(task2);
        historyManager.addTaskToHistory(task3);
        assertEquals(3, historyManager.getHistory().size());
        //Удалем задачу из середины
        historyManager.removeTaskFromHistory(task2.getId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
    }

}