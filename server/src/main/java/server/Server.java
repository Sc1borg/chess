package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.Javalin;
import model.*;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

public class Server {

    private final Javalin javalin;
    private final Gson gson = new Gson();
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
                RegisterRequest registerRequest = gson.fromJson(ctx.body(), RegisterRequest.class);
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
                LoginRequest loginRequest = gson.fromJson(ctx.body(), LoginRequest.class);
                LoginResult loginResult = userService.login(loginRequest);
                ctx.status(200).json(loginResult);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    ctx.status(401).json(e);
                }
            }
        });


        javalin.get("/game", ctx -> {
            try {
                String auth = ctx.header("authorization");
                ArrayList<GameData> games = gameService.getGames(auth);
                ctx.status(200).json(games);

            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    ctx.status(401).json(e);
                }
            }
        });

        javalin.post("/game", ctx -> {
            try {
                String auth = ctx.header("authorization");
                CreateGameRequest createGameRequest = gson.fromJson(ctx.body(), CreateGameRequest.class);

                int gameId = gameService.createGame(createGameRequest, auth);
                ctx.status(200).json(gameId);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    ctx.status(401).json(e);
                }
            }
        });

        javalin.put("/game", ctx -> {
            try {
                String auth = ctx.header("authorization");
                JoinGameRequest joinGameRequest = gson.fromJson(ctx.body(), JoinGameRequest.class);

                gameService.joinGame(joinGameRequest, auth);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    ctx.status(401).json(e);
                }
                if (e.getMessage().equals("Error: already taken")) {
                    ctx.status(401).json(e);
                }
                if (e.getMessage().equals("Error: bad request")) {
                    ctx.status(401).json(e);
                }
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
