package test.Model;

import Model.Task;
import Model.TaskStartTimeComparator;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskStartTimeComparatorTest {

    TaskStartTimeComparator taskStartTimeComparator = new TaskStartTimeComparator();
    LocalDateTime startTime1 = LocalDateTime.of(2022, 1, 1, 00, 05);
    LocalDateTime startTime2 = LocalDateTime.of(2022, 1, 1, 00, 00);
    Duration duration = Duration.ofMinutes(5);
    Task task1 = new Task("name", "description", startTime1, duration);
    Task task2 = new Task("name2", "description2", startTime2, duration);
    Task task3 = new Task("name2", "description2", null, null);

    @Test
    void shouldReturnOneForTask1IsAfterTask2() {
        assertEquals(1, taskStartTimeComparator.compare(task1, task2));
    }

    @Test
    void shouldReturnMinusOneForTask1IsBeforeTask2() {
        assertEquals(-1, taskStartTimeComparator.compare(task2, task1));
    }

    @Test
    void shouldReturnOneForTask1IsNullAndTask2NotNull() {
        assertEquals(1, taskStartTimeComparator.compare(task1, task2));
    }

    @Test
    void shouldReturnMinusOneForTask2IsNullAndTask1NotNull() {
        assertEquals(-1, taskStartTimeComparator.compare(task2, task1));
    }
}