import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        /*
        //Тестовый вызов функций для проверки и отладки программы
        //Создание объектов
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Название задачи 1", "Описание задачи 1"));
        taskManager.createTask(new Task("Название задачи 2", "Описание задачи 2"));

        taskManager.createEpic(new Epic("Название эпика 3", "Описание эпика 3"));
        taskManager.createSubTask(new SubTask("Название сабтаски 4", "Описание сабтаски 4", 3));

        taskManager.createEpic(new Epic("Название эпика 5", "Описание эпика 5"));
        taskManager.createSubTask(new SubTask("Название сабтаски 6", "Описание сабтаски 6", 5));
        taskManager.createSubTask(new SubTask("Название сабтаски 7", "Описание сабтаски 7", 5));

        //Изменение статусов объектов
        taskManager.tasksList.get(1).setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.tasksList.get(2).setTaskStatus(TaskStatus.DONE);

        taskManager.subTasksList.get(4).setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.subTasksList.get(6).setTaskStatus(TaskStatus.DONE);
        taskManager.subTasksList.get(7).setTaskStatus(TaskStatus.DONE);

        //Получение списка всех задач
        HashMap<Integer, Task> taskList = taskManager.getTasksList();
        HashMap<Integer, Epic> epicsList = taskManager.getEpicsList();
        HashMap<Integer, SubTask> subTasksList = taskManager.getSubTasksList();

        //Удаление всех задач
        taskManager.clearTasksList();
        taskManager.clearEpicsList();
        taskManager.clearSubTasksList();

        //Получение по идентификатору
        Task task = taskManager.getTaskById(1);
        Epic epic = taskManager.getEpicById(3);
        SubTask subTask = taskManager.getSubTaskById(4);

        //Получение списка всех подзадач определённого эпика
        HashMap<Integer, SubTask> subTasksListOfEpic = taskManager.getSubTasksListOfEpic(5);

        //Удаление объектов по ID
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);
        taskManager.removeSubTaskById(3);

        //Обновление объектов
        taskManager.updateTask(1, new Task("Новое название задачи 1", "Новое описание задачи 1"));
        taskManager.updateEpic(5, new Epic("Новое название эпика 5", "Новое описание эпика 5"));
        taskManager.updateSubTask(4, new SubTask("Новое название сабтаски 4", "Новое описание сабтаски 4", 3));
        System.out.println(taskManager.epicsList.get(3).taskStatus);
        */
    }
}
