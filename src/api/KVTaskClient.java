package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String API_KEY = "DEBUG";
    private final String url;
    private final HttpClient client;

    public KVTaskClient(String url) {
        this.url = url;
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "register"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            API_KEY = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "save/" + key + "?API_KEY=" + API_KEY))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "load/" + key + "?API_KEY=" + API_KEY))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        return response.body();
    }
}
