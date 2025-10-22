package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.Javalin;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final AuthService authService = new AuthService();
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            userService.clear();
            gameService.clear();
            authService.clear();
            ctx.status(200);
        });


        javalin.post("/user", ctx -> {
            try {
                RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
                LoginResult loginResult = userService.register(registerRequest);
                ctx.status(200).json(loginResult);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: already taken")) {
                    ctx.status(403).json(e);
                } else {
                    ctx.status(500);
                }
            }
        });


        javalin.post("/session", ctx -> {
            try {
                LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
                LoginResult loginResult = userService.login(loginRequest);
                ctx.status(200).json(loginResult);
            } catch (DataAccessException e) {
                ctx.status(403).json(e);
            }
        });


        javalin.delete("/session", ctx -> {
            String auth = ctx.header("authorization");

            boolean success = authService.invalidateToken(auth);
            if (success) {
                ctx.status(200);
            } else {
                ctx.status(501).result("Error: Description of error");
            }
        });


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
