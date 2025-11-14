package server;


import com.google.gson.Gson;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public LoginResult register(RegisterRequest user) throws Exception {
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public LoginResult login(LoginRequest user) throws Exception {
        var request = buildRequest("POST", "/session", user);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void create(String gameName) throws Exception {
        var request = buildRequest("POST", "/game", gameName);
        var response = sendRequest(request);
    }

    public void list() throws Exception {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
//        return handleResponse(response, )
    }

    public void join(String ID, String color) throws Exception {
        var request = buildRequest("PUT", "/game", null);
        var response = sendRequest(request);
    }

    public void logout() throws Exception {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
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

    private boolean isSuccessful(int status) {
        return (status / 100 == 2);
    }
}