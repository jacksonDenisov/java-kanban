package test.Manager;

import Manager.InMemoryTaskManager;
import Manager.TaskManager;
import Model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {


    private InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @BeforeEach
    public void resetManager() {
        this.taskManager = new InMemoryTaskManager();
    }
}