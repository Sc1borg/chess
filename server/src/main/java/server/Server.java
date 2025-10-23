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
                String myResult = gson.toJson(loginResult);
                ctx.status(200).json(myResult);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: already taken")) {
                    String myResult = gson.toJson(e);
                    ctx.status(403).json(myResult);
                } else if (e.getMessage().equals("Error: bad request")) {
                    String myResult = gson.toJson(e);
                    ctx.status(400).json(myResult);
                }
            }
        });


        javalin.post("/session", ctx -> {
            try {
                LoginRequest loginRequest = gson.fromJson(ctx.body(), LoginRequest.class);
                LoginResult loginResult = userService.login(loginRequest);
                String myResult = gson.toJson(loginResult);
                ctx.status(200).json(myResult);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    String myResult = gson.toJson(e);
                    ctx.status(401).json(myResult);
                } else if (e.getMessage().equals("Error: bad request")) {
                    String myResult = gson.toJson(e);
                    ctx.status(400).json(myResult);
                }
            }
        });

        javalin.delete("/session", ctx -> {
            try {
                String auth = ctx.header("authorization");
                userService.logout(auth);
                ctx.status(200);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    String myResult = gson.toJson(e);
                    ctx.status(401).json(myResult);
                }
            }
        });


        javalin.get("/game", ctx -> {
            try {
                String auth = ctx.header("authorization");
                ArrayList<GameData> games = gameService.getGames(auth);
                String myResult = gson.toJson(games);
                ctx.status(200).json(myResult);

            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    String myResult = gson.toJson(e);
                    ctx.status(401).json(myResult);
                }
            }
        });

        javalin.post("/game", ctx -> {
            try {
                String auth = ctx.header("authorization");
                CreateGameRequest createGameRequest = gson.fromJson(ctx.body(), CreateGameRequest.class);

                int gameId = gameService.createGame(createGameRequest, auth);
                String myResult = gson.toJson(gameId);
                ctx.status(200).json(myResult);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    String myResult = gson.toJson(e);
                    ctx.status(401).json(myResult);
                }
            }
        });

        javalin.put("/game", ctx -> {
            try {
                String auth = ctx.header("authorization");
                JoinGameRequest joinGameRequest = gson.fromJson(ctx.body(), JoinGameRequest.class);

                gameService.joinGame(joinGameRequest, auth);

                ctx.status(200);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    String myResult = gson.toJson(e);
                    ctx.status(401).json(myResult);
                }
                if (e.getMessage().equals("Error: already taken")) {
                    String myResult = gson.toJson(e);
                    ctx.status(401).json(myResult);
                }
                if (e.getMessage().equals("Error: bad request")) {
                    String myResult = gson.toJson(e);
                    ctx.status(401).json(myResult);
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
