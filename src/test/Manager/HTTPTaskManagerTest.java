package test.Manager;

import KV_Client_Server.KVServer;

import Model.Epic;
import Model.SubTask;
import Model.Task;
import Server.HttpTaskServer;

import Server.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {

    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private HttpResponse<String> response;
    private static URI url;
    private static String json;
    private static HttpRequest.BodyPublisher body;
    private static HttpClient client;
    private static HttpResponse.BodyHandler handler;
    private static Gson gson;
    private static Gson gsonWithoutAdapter;
    private static Task task;
    private static Epic epic;
    private static SubTask subTask;

    @BeforeAll
    public static void setUpBefore() {
        gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).create();
        gsonWithoutAdapter = new GsonBuilder().setPrettyPrinting().create();
        client = HttpClient.newHttpClient();
        handler = HttpResponse.BodyHandlers.ofString();
        task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 1, 1, 00, 00), Duration.ofMinutes(1));
        epic = new Epic("Название эпика 2", "Описание эпика 2");
        subTask = new SubTask("Название подзадачи 3", "Описание подзадачи 3", LocalDateTime.of(2023, 1, 1, 00, 00), Duration.ofMinutes(1), 1);
    }


    @BeforeEach
    public void startServers() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void stopServers() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    public HttpRequest buildPostRequest(URI url, String json) {
        body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPOST = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        return requestPOST;
    }

    public HttpRequest buildGetRequest(URI url) {
        HttpRequest requestGET = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        return requestGET;
    }

    public HttpRequest buildDeleteRequest(URI url) {
        HttpRequest requestDELETE = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        return requestDELETE;
    }


    @Test
    public void postTaskTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void postEpicAndSubTaskTest() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        json = gsonWithoutAdapter.toJson(epic);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void getTasksListTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/task/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void getEpicAndSubTaskTest() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        json = gsonWithoutAdapter.toJson(epic);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        url = URI.create("http://localhost:8080/tasks/subtask/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void getPrioritizedTasksTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        url = URI.create("http://localhost:8080/tasks/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    public void clearTasksListTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что список не пуст
        url = URI.create("http://localhost:8080/tasks/task");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        url = URI.create("http://localhost:8080/tasks/task/");
        try {
            response = client.send(buildDeleteRequest(url), handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что список пустой
        url = URI.create("http://localhost:8080/tasks/task");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    public void clearEpicAndSubTaskListTest() {
        //Создаем эпик и подзадачу
        url = URI.create("http://localhost:8080/tasks/epic/");
        json = gsonWithoutAdapter.toJson(epic);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что список не пуст
        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        //Проверяем, что список не пуст
        url = URI.create("http://localhost:8080/tasks/subtask/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        //Очищаем списки
        url = URI.create("http://localhost:8080/tasks/subtask/");
        try {
            response = client.send(buildDeleteRequest(url), handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildDeleteRequest(url), handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что списки очищены
        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
        url = URI.create("http://localhost:8080/tasks/subtask/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());

    }


    @Test
    public void removeTaskByIdTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что список не пустой
        url = URI.create("http://localhost:8080/tasks/task");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        try {
            response = client.send(buildDeleteRequest(url), handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        //Проверяем, что задача удалена
        url = URI.create("http://localhost:8080/tasks/task");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    public void removeEpicAndSubTaskByIdTest() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        json = gsonWithoutAdapter.toJson(epic);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        //Проверяем, что список не пуст
        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        //Проверяем, что список не пуст
        url = URI.create("http://localhost:8080/tasks/subtask/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        //Удаляем подзадачу и эпик
        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        try {
            response = client.send(buildDeleteRequest(url), handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        try {
            response = client.send(buildDeleteRequest(url), handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что подзадача и эпик удалены
        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
        url = URI.create("http://localhost:8080/tasks/subtask/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }


    @Test
    public void updateTaskByIdTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем, что список не пустой
        url = URI.create("http://localhost:8080/tasks/task");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());


        json = gson.toJson(new Task("Название задачи 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 1, 00, 00), Duration.ofMinutes(1)));
        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        try {
            response = client.send(buildPostRequest(url, json), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(201, response.statusCode());
        assertEquals("Задача успешно обновлена.", response.body());
    }


    @Test
    public void updateEpicAndSubTaskByIdTest() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        json = gsonWithoutAdapter.toJson(epic);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        //Проверяем, что список не пуст
        url = URI.create("http://localhost:8080/tasks/epic/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        //Проверяем, что список не пуст
        url = URI.create("http://localhost:8080/tasks/subtask/");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        json = gsonWithoutAdapter.toJson(new Epic("Новое название эпика 3", "Новое описание эпика 2"));
        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        try {
            response = client.send(buildPostRequest(url, json), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(201, response.statusCode());
        assertEquals("Эпик успешно обновлен.", response.body());

        json = gson.toJson(new SubTask("Новое название подзадачи 3", "Новое описание подзадачи 3", LocalDateTime.of(2026, 1, 1, 00, 00), Duration.ofMinutes(1), 1));
        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        try {
            response = client.send(buildPostRequest(url, json), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(201, response.statusCode());
        assertEquals("Подзадача успешно обновлена.", response.body());
    }


    @Test
    public void getTaskByIdTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/task/?=1");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
        assertTrue(response.body().contains("Название задачи 1"));
    }

    @Test
    public void getEpicAndSubTaskByIdTest() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        json = gsonWithoutAdapter.toJson(epic);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/epic/?=1");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
        assertNotNull(response.body());
        assertTrue(response.body().contains("Название эпика 2"));

        url = URI.create("http://localhost:8080/tasks/subtask/?=2");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        assertTrue(response.body().contains("Название подзадачи 3"));
    }


    @Test
    public void getSubTaskListOfEpicTest() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        json = gsonWithoutAdapter.toJson(epic);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        url = URI.create("http://localhost:8080/tasks/epic/?=1");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
        assertNotNull(response.body());
        assertTrue(response.body().contains("subTasksListOfEpic\": [\n" +
                "    2"));
    }

    @Test
    public void getHistoryTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        json = gson.toJson(task);
        try {
            response = client.send(buildPostRequest(url, json), handler);
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        //Проверяем что задачи нет в истории
        url = URI.create("http://localhost:8080/tasks/history");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertFalse(response.body().contains("\"id\": 1"));

        url = URI.create("http://localhost:8080/tasks/task/?=1");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
        assertTrue(response.body().contains("Название задачи 1"));

        url = URI.create("http://localhost:8080/tasks/history");
        try {
            response = client.send(buildGetRequest(url), handler);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(response.body().contains("\"id\": 1"));
    }


}