package test.Model;

import Model.SubTask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    private static LocalDateTime startTime;
    private static Duration duration;
    private static SubTask subTask;

    @BeforeAll
    public static void beforeAll() {
        startTime = LocalDateTime.of(2022, 1, 1, 00, 00);
        duration = Duration.ofMinutes(5);
        subTask = new SubTask("returnCorrectFieldsSubTask", "returnCorrectFieldsSubTask description", startTime, duration, 5);
    }

    @Test
    public void shouldReturnEpicId() {
        assertEquals(5, subTask.getParentEpicId());
    }

}