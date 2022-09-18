package Server;

import KV_Client_Server.KVServer;
import Manager.Managers;
import Manager.TaskManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/*Написал API HttpTaskServer  и проверил на FileBackedTasksManager, с сервером затрудняюсь придумать,
        как лучше обмениваться данными задач и как лучше их хранить на сервере*/

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static TaskManager taskManager = Managers.getDefaultFileBackManager();


    public HttpTaskServer() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        //kvServer.stop();

        taskManager = Managers.getDefault();

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new PostsHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }


    static class PostsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
            Gson gson = gsonBuilder.create();
            URI requestURI = httpExchange.getRequestURI();
            String method = httpExchange.getRequestMethod();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            String path = requestURI.getPath();
            String query = requestURI.getQuery();
            String[] pathElements = path.split("/");
            String response = "";
            int responseCode = 200;
            int id = 0;
            if (query != null) {
                id = Integer.parseInt(query.split("\\?")[0].split("=")[1]);
            }

            switch (pathElements.length) {
                case 2:
                    if (method.equals("GET")) {
                        response = gson.toJson(taskManager.getPrioritizedTasks());
                    } else {
                        response = "Недопустимо использование метода " + method + " для данного запроса";
                        responseCode = 400;
                    }
                    break;
                case 3:
                    //История
                    if (pathElements[2].equals("history") && method.equals("GET")) {
                        response = gson.toJson(taskManager.getHistory());
                        /*Задача*/
                    } else if (pathElements[2].equals("task")) {
                        if (method.equals("GET")) {
                            if (query == null) {
                                response = gson.toJson(taskManager.getTasksList());
                            } else {
                                response = gson.toJson(taskManager.getTask(id));
                            }
                        } else if (method.equals("DELETE")) {
                            if (query == null) {
                                taskManager.clearTasksList();
                                response = "Список задач успешно очищен.";
                            } else {
                                taskManager.removeTask(id);
                                response = "Задача " + id + " успешно удалена";
                            }
                        } else if (method.equals("POST")) {
                            if (query == null) {
                                taskManager.createTask(gson.fromJson(body, Task.class));
                                response = "Задача успешно создана.";
                                responseCode = 201;
                            } else {
                                Task task = gson.fromJson(body, Task.class);
                                task.setId(id);
                                taskManager.updateTask(task);
                                response = "Задача успешно обновлена.";
                            }
                        }
                        /*Эпик*/
                    } else if (pathElements[2].equals("epic")) {
                        if (method.equals("GET")) {
                            if (query == null) {
                                response = gson.toJson(taskManager.getEpicsList());
                            } else {
                                response = gson.toJson(taskManager.getEpic(id));
                            }
                        } else if (method.equals("DELETE")) {
                            if (query == null) {
                                taskManager.clearEpicsList();
                                response = "Список эпиков успешно очищен.";
                            } else {
                                taskManager.removeEpic(id);
                                response = "Эпик " + id + " успешно удален";
                            }
                        } else if (method.equals("POST")) {
                            if (query == null) {
                                taskManager.createEpic(gson.fromJson(body, Epic.class));
                                response = "Эпик успешно создан.";
                                responseCode = 201;
                            } else {
                                Epic epic = gson.fromJson(body, Epic.class);
                                epic.setId(id);
                                taskManager.updateEpic(epic);
                                response = "Эпик успешно обновлен.";
                            }
                        }
                        /*Подзадача*/
                    } else if (pathElements[2].equals("subtask")) {
                        if (method.equals("GET")) {
                            if (query == null) {
                                response = gson.toJson(taskManager.getSubTasksList());
                            } else {
                                response = gson.toJson(taskManager.getSubTask(id));
                            }
                        } else if (method.equals("DELETE")) {
                            if (query == null) {
                                taskManager.clearSubTasksList();
                                response = "Список подзадач успешно очищен.";
                            } else {
                                taskManager.removeSubTask(id);
                                response = "Подзадача " + id + " успешно удалена";
                            }
                        } else if (method.equals("POST")) {
                            if (query == null) {
                                taskManager.createSubTask(gson.fromJson(body, SubTask.class));
                                response = "Подадача успешно создана.";
                                responseCode = 201;
                            } else {
                                SubTask subTask = gson.fromJson(body, SubTask.class);
                                subTask.setId(id);
                                taskManager.updateSubTask(subTask);
                                response = "Подзадача успешно обновлена.";
                            }
                        }
                        /*Некорректный запрос*/
                    } else {
                        responseCode = 400;
                    }
                    break;
                case 4:

                    if (query != null) {
                        response = gson.toJson(taskManager.getSubTasksListOfEpic(id));
                    } else {
                        response = "Параметр строки для определения эпика указан некорректно";
                    }
                    break;
                default:
                    responseCode = 400;
            }
            httpExchange.sendResponseHeaders(responseCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

}
