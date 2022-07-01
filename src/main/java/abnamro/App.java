package abnamro;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {
    public static void makeHttpRequest() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://randomuser.me/api/"))
                .build();
        client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept((data) -> {
                    JSONObject result = new JSONObject(data);
                    JSONArray ja = result.getJSONArray("results");

                    for (int i = 0; i < ja.length(); ++i) {
                        JSONObject jo = ja.getJSONObject(i);
                        String email = jo.getString("email");
                        System.out.println(email);
                    }

                })
                .join();

    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        makeHttpRequest();
    }
}
