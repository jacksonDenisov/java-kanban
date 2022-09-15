package test.Model;

import Model.Epic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static Epic epic;


    @BeforeAll
    static void beforeAll() {
        epic = new Epic("returnCorrectFieldsEpic", "returnCorrectFieldsEpic description");
    }

    @Test
    void returnCorrectFieldsEpic() {
        epic.addSubTaskInSubTaskListOfEpic(5);
        assertEquals(5, epic.getSubTasksListOfEpic().get(0));
    }


}