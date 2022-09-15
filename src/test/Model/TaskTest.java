package test.Model;

import Model.*;

import static Model.TaskStatus.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    static LocalDateTime startTime;
    static Duration duration;
    static Task task;

    @BeforeAll
    static void beforAll() {
        startTime = LocalDateTime.of(2022, 1, 1, 00, 00);
        duration = Duration.ofMinutes(5);
        task = new Task("returnCorrectFieldsTask", "returnCorrectFieldsTask description", startTime, duration);
    }

    @Test
    void returnCorrectFieldsTask() {
        assertEquals("returnCorrectFieldsTask", task.getName());
        assertEquals("returnCorrectFieldsTask description", task.getDescription());
        assertEquals(NEW, task.getStatus());
        assertEquals(TaskType.Task, task.getType());
        assertEquals(startTime, task.getStartTime());
        assertEquals(duration, task.getDuration());
    }

    @Test
    void shouldReturn202201010005endTime() {
        LocalDateTime endTime = task.getEndTime();
        assertEquals(endTime, LocalDateTime.of(2022, 1, 1, 00, 05));
    }
}