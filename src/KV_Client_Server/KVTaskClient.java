package KV_Client_Server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVTaskClient {
    private final String url;
    private final String apiToken;


    public KVTaskClient(String url) {
        this.url = url;
        apiToken = register(url);
        System.out.println(apiToken);
    }


    public void put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                throw new RuntimeException("Ошибка при отправке запроса на загрузку данных");
            }
        } catch (IOException | InterruptedException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public String load(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "load" + key + "?API_TOKEN=" + apiToken))
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                throw new RuntimeException("Ошибка при отправке запроса на загрузку данных");
            }
            return response.body();
        } catch (IOException | InterruptedException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "register"))
                    .build();
            //HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Ошибка при отправке запроса на регистрацию");
            }
            return response.body();
        } catch (IOException | InterruptedException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
