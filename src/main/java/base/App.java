package base;

import java.io.IOException;
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

    public static void main(String[] args) {
        config.Server.loadInputs();
        config.Server.setProp("port", "3000", "Server starting port");
        System.out.println("Hello World!");
        makeHttpRequest();
        try {
            Server.run();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

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

}
