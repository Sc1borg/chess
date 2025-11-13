package server;


import com.google.gson.Gson;
import model.LoginRequest;
import model.RegisterRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public void register(RegisterRequest user) {
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        return handleResponse(response);
    }

    public void login(LoginRequest user) {
        var request = buildRequest("POST", "/session", user);
        var response = sendRequest(request);
        return handleResponse(response);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }
}
