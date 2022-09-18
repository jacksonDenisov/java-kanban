package KV_Client_Server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*Написал API HttpTaskServer  и проверил на FileBackedTasksManager, с сервером затрудняюсь придумать,
        как лучше обмениваться данными задач и как лучше их хранить на сервере*/

public class KVTaskClient {
    private String url;
    private String apiToken;


    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = url;
        apiToken = register(url);
    }

    public void put(String key, String json) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                .build();
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "load" + key + "?API_TOKEN=" + apiToken))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        return response.body();
    }

    private String register(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "register"))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        return response.body();
    }
}
