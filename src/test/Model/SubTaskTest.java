package test.Model;

import Model.SubTask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    static LocalDateTime startTime;
    static Duration duration;
    static SubTask subTask;

    @BeforeAll
    static void beforeAll() {
        startTime = LocalDateTime.of(2022, 1, 1, 00, 00);
        duration = Duration.ofMinutes(5);
        subTask = new SubTask("returnCorrectFieldsSubTask", "returnCorrectFieldsSubTask description", startTime, duration, 5);
    }

    @Test
    public void shouldReturnEpicId() {
        assertEquals(5, subTask.getParentEpicId());
    }

}