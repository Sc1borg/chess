package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.Javalin;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            userService.clear();
            gameService.clear();
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
            try {
                String auth = ctx.header("authorization");
                userService.logout(auth);
                ctx.status(200);
            } catch (DataAccessException e) {
                ctx.status(501).json(e);
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
