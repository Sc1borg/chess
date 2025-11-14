package server;


import com.google.gson.Gson;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public LoginResult register(RegisterRequest user) throws Exception {
        var request = buildRequest("POST", "/user", user, null);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public LoginResult login(LoginRequest user) throws Exception {
        var request = buildRequest("POST", "/session", user, null);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void create(CreateGameRequest gameReq, LoginResult user) throws Exception {
        var request = buildRequest("POST", "/game", gameReq, user.authToken());
        var response = sendRequest(request);
        if (!checkResponse(response)) {
            throw new Exception("Failed to create game");
        }
    }

    public Map<String, ArrayList> list(LoginResult user) throws Exception {
        var request = buildRequest("GET", "/game", null, user.authToken());
        var response = sendRequest(request);
        return handleResponse(response, Map.class);
    }

    public void join(JoinGameRequest joinReq, LoginResult user) throws Exception {
        var request = buildRequest("PUT", "/game", joinReq, user.authToken());
        var response = sendRequest(request);
        if (!checkResponse(response)) {
            throw new Exception("Failed to join game");
        }
    }

    public void logout(LoginResult user) throws Exception {
        var request = buildRequest("GET", "/game", null, user.authToken());
        var response = sendRequest(request);
        if (!checkResponse(response)) {
            throw new Exception("Failed to logout");
        }
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest req) throws Exception {
        try {
            return client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new Exception(body);
            }

            throw new Exception("other failure: " + status);
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean checkResponse(HttpResponse<String> response) throws Exception {
        var status = response.statusCode();
        return isSuccessful(status);
    }

    private boolean isSuccessful(int status) {
        return (status / 100 == 2);
    }
}