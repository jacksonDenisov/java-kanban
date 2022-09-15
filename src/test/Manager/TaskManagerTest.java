package test.Manager;

import Manager.TaskManager;
import Model.*;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    protected LocalDateTime startTime1 = LocalDateTime.of(2022, 1, 1, 00, 00);
    protected LocalDateTime startTime2 = LocalDateTime.of(2022, 1, 1, 03, 00);
    protected LocalDateTime startTime3 = LocalDateTime.of(2022, 1, 1, 06, 00);
    protected Duration duration1 = Duration.ofMinutes(5);
    protected Duration duration2 = Duration.ofMinutes(10);
    String name = "name";
    String description = "description";

    @Test
    void getPrioritizedTasksTest() {
        assertEquals(0, taskManager.getPrioritizedTasks().size());
        Task task1 = new Task(name, description, startTime1, duration1);
        Task task2 = new Task(name, description, startTime2, duration2);
        Task task3 = new Task(name, description, startTime3, duration1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task1);
        assertEquals(3, taskManager.getPrioritizedTasks().size());
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskManager.getPrioritizedTasks()) {
            tasks.add(task);
        }
        assertEquals(task1, tasks.get(0));
    }

    //Проверки для Task
    @Test
    void getTasksListTest() {
        assertEquals(0, taskManager.getTasksList().size());
        Task task1 = new Task(name, description, startTime1, duration1);
        Task task2 = new Task(name, description, startTime2, duration2);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(2, taskManager.getTasksList().size());
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskManager.getTasksList()) {
            tasks.add(task);
        }
        assertEquals(task1, tasks.get(0));
    }

    @Test
    void clearTasksListTest() {
        //Проверка при пустом списке
        taskManager.clearTasksList();
        //Проверка при заполненном
        Task task1 = new Task(name, description, startTime1, duration1);
        Task task2 = new Task(name, description, startTime2, duration2);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(2, taskManager.getTasksList().size());
        taskManager.clearTasksList();
        assertEquals(0, taskManager.getTasksList().size());
    }

    @Test
    void getTaskTest() {
        Task task1 = new Task(name, description, startTime1, duration1);
        taskManager.createTask(task1);
        assertEquals(task1, taskManager.getTask(1));
        assertEquals(taskManager.getHistory().size(), 1);
        //Получение несуществующей задачи
        assertNull(taskManager.getTask(5));
    }

    @Test
    void createTask() {
        Task task = new Task(name, description, startTime1, duration1);
        taskManager.createTask(task);
        taskManager.getTask(1);
        assertEquals(1, taskManager.getTasksList().size());
        assertNotNull(taskManager.getTasksList().get(0));
    }

    @Test
    void shouldNotCreateTask2WhenTasksStartTimeIsEquals() {
        Task task1 = new Task(name, description, startTime1, duration1);
        Task task2 = new Task(name, description, startTime1, duration2);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(1, taskManager.getTasksList().size());
        assertEquals(task1, taskManager.getTasksList().get(0));
    }

    @Test
    void updateTaskTest() {
        Task task1 = new Task(name, description, startTime1, duration1);
        Task task2 = new Task(name, description, startTime2, duration2);
        taskManager.createTask(task1);
        task2.setId(1);
        taskManager.updateTask(task2);
        assertEquals(task2, taskManager.getTask(1));
        assertEquals(1, taskManager.getTasksList().size());
    }

    @Test
    void shouldNotUpdateTaskWithIncorrectId() {
        Task task1 = new Task(name, description, startTime1, duration1);
        Task task2 = new Task(name, description, startTime2, duration2);
        taskManager.createTask(task1);
        taskManager.updateTask(task2);
        assertEquals(task1, taskManager.getTask(1));
        assertEquals(1, taskManager.getTasksList().size());
    }

    @Test
    void removeTaskTest() {
        Task task1 = new Task(name, description, startTime1, duration1);
        taskManager.createTask(task1);
        assertEquals(1, taskManager.getTasksList().size());
        taskManager.removeTask(1);
        assertEquals(0, taskManager.getTasksList().size());
        //Удаление задачи с несуществующим номером
        taskManager.removeTask(5);
    }


    //Проверки для SubTask
    @Test
    void getSubTasksListTest() {
        assertEquals(0, taskManager.getSubTasksList().size());
        taskManager.createEpic(new Epic(name, description));
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        SubTask subtask2 = new SubTask(name, description, startTime2, duration2, 1);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        assertEquals(2, taskManager.getSubTasksList().size());
        List<Task> subTasks = new ArrayList<>();
        for (Task subTask : taskManager.getSubTasksList()) {
            subTasks.add(subTask);
        }
        assertEquals(subtask1, subTasks.get(0));
    }

    @Test
    void clearSubTasksListTest() {
        //Проверка при пустом списке
        taskManager.clearSubTasksList();
        //Проверка при заполненном
        taskManager.createEpic(new Epic(name, description));
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        SubTask subtask2 = new SubTask(name, description, startTime2, duration2, 1);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        assertEquals(taskManager.getEpic(1).getSubTasksListOfEpic().size(), 2);
        assertEquals(2, taskManager.getSubTasksList().size());
        taskManager.clearSubTasksList();
        assertEquals(0, taskManager.getSubTasksList().size());
        assertEquals(taskManager.getEpic(1).getSubTasksListOfEpic().size(), 0);
    }

    @Test
    void getSubTaskTest() {
        taskManager.createEpic(new Epic(name, description));
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        taskManager.createSubTask(subtask1);
        assertEquals(subtask1, taskManager.getSubTask(2));
        assertEquals(taskManager.getHistory().size(), 1);
        //Получение несуществующей задачи
        assertNull(taskManager.getSubTask(5));
    }

    @Test
    void createSubTask() {
        taskManager.createEpic(new Epic(name, description));
        assertEquals(0, taskManager.getSubTasksList().size());
        assertEquals(0, taskManager.getEpic(1).getSubTasksListOfEpic().size());
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        taskManager.createSubTask(subtask1);
        assertEquals(1, taskManager.getSubTasksList().size());
        assertEquals(1, taskManager.getEpic(1).getSubTasksListOfEpic().size());
    }

    @Test
    void updateSubTaskTest() {
        taskManager.createEpic(new Epic(name, description));
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        SubTask subtask2 = new SubTask(name, description, startTime2, duration2, 1);
        taskManager.createSubTask(subtask1);
        subtask2.setId(2);
        taskManager.updateSubTask(subtask2);
        assertEquals(subtask2, taskManager.getSubTask(2));
        assertEquals(1, taskManager.getSubTasksList().size());
        assertEquals(1, taskManager.getEpic(1).getSubTasksListOfEpic().size());
    }

    @Test
    void removeSubTaskTest() {
        taskManager.createEpic(new Epic(name, description));
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        taskManager.createSubTask(subtask1);
        assertEquals(1, taskManager.getSubTasksList().size());
        assertEquals(1, taskManager.getEpic(1).getSubTasksListOfEpic().size());
        taskManager.removeSubTask(2);
        assertEquals(0, taskManager.getTasksList().size());
        assertEquals(0, taskManager.getEpic(1).getSubTasksListOfEpic().size());
        //Удаление задачи с несуществующим номером
        taskManager.removeSubTask(5);
    }


    //Проверки для Epic
    @Test
    void getEpicListTest() {
        assertEquals(0, taskManager.getEpicsList().size());
        Epic epic1 = new Epic(name, description);
        Epic epic2 = new Epic(name, description);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        assertEquals(2, taskManager.getEpicsList().size());
        List<Task> epics = new ArrayList<>();
        for (Task epicTemp : taskManager.getEpicsList()) {
            epics.add(epicTemp);
        }
        assertEquals(epic1, epics.get(0));
    }

    @Test
    void clearEpicListTest() {
        //Проверка при пустом списке
        taskManager.clearEpicsList();
        //Проверка при заполненном
        Epic epic1 = new Epic(name, description);
        Epic epic2 = new Epic(name, description);
        SubTask subtask = new SubTask(name, description, startTime1, duration1, 1);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subtask);
        assertEquals(2, taskManager.getEpicsList().size());
        assertEquals(1, taskManager.getSubTasksList().size());
        taskManager.clearEpicsList();
        assertEquals(0, taskManager.getEpicsList().size());
        assertEquals(0, taskManager.getSubTasksList().size());
    }

    @Test
    void getEpicTest() {
        Epic epic1 = new Epic(name, description);
        taskManager.createEpic(epic1);
        assertEquals(epic1, taskManager.getEpic(1));
        assertEquals(taskManager.getHistory().size(), 1);
        //Получение несуществующей задачи
        assertNull(taskManager.getEpic(5));
    }

    @Test
    void createEpicTest() {
        Epic epic1 = new Epic(name, description);
        assertEquals(0, taskManager.getEpicsList().size());
        taskManager.createEpic(epic1);
        assertEquals(1, taskManager.getEpicsList().size());
    }

    @Test
    void updateEpicTest() {
        Epic epic1 = new Epic(name, description);
        Epic epic2 = new Epic(name, description);
        SubTask subtask = new SubTask(name, description, startTime1, duration1, 1);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subtask);
        epic2.setId(1);
        taskManager.updateEpic(epic2);
        assertEquals(epic2, taskManager.getEpic(1));
        assertEquals(1, taskManager.getEpicsList().size());
        assertEquals(subtask.getId(), taskManager.getEpic(1).getSubTasksListOfEpic().get(0));
    }

    @Test
    void removeEpicTest() {
        Epic epic1 = new Epic(name, description);
        SubTask subtask = new SubTask(name, description, startTime1, duration1, 1);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subtask);
        assertEquals(1, taskManager.getEpicsList().size());
        assertEquals(1, taskManager.getSubTasksList().size());
        taskManager.removeEpic(1);
        assertEquals(0, taskManager.getEpicsList().size());
        assertEquals(0, taskManager.getSubTasksList().size());
        //Удаление задачи с несуществующим номером
        taskManager.removeEpic(5);
    }

    @Test
    void epicTimeTest() {
        Epic epic1 = new Epic(name, description);
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        SubTask subtask2 = new SubTask(name, description, startTime2, duration2, 1);
        SubTask subtask3 = new SubTask(name, description, startTime3, duration1, 1);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        taskManager.createSubTask(subtask3);
        assertEquals(subtask1.getStartTime(), taskManager.getEpic(1).getStartTime());
        assertEquals(duration1.plus(duration2).plus(duration1), taskManager.getEpic(1).getDuration());
        assertEquals(subtask3.getEndTime(), taskManager.getEpic(1).getEndTime());
    }


    @Test
    void epicStatusTest() {
        Epic epic = new Epic(name, description);
        SubTask subtask1 = new SubTask(name, description, startTime1, duration1, 1);
        SubTask subtask2 = new SubTask(name, description, startTime2, duration2, 1);
        SubTask subtask3 = new SubTask(name, description, startTime3, duration1, 1);

        //a. Пустой список подзадач.
        taskManager.createEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus());

        //b. Все подзадачи со статусом NEW.
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        assertEquals(TaskStatus.NEW, epic.getStatus());

        //c. Все подзадачи со статусом DONE.
        taskManager.clearSubTasksList();
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        assertEquals(TaskStatus.DONE, epic.getStatus());

        //d. Подзадачи со статусами NEW и DONE.
        taskManager.clearSubTasksList();
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        //e. Подзадачи со статусом IN_PROGRESS.
        taskManager.clearSubTasksList();
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}
