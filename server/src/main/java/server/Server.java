package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SchemaManager;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Server {

    private final Javalin javalin;
    private final Gson gson = new Gson();
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        try {
            DatabaseManager.createDatabase();
            SchemaManager.initializeSchema();
        } catch (DataAccessException ex) {
            log.error("e: ", ex);
        }

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            try {
                userService.clear();
                gameService.clear();
                ctx.status(200);
            } catch (DataAccessException e) {
                String myResult = serializeE(e.getMessage());
                ctx.status(500).json(myResult);
            }
        });


        javalin.post("/user", ctx -> {
            try {
                RegisterRequest registerRequest = gson.fromJson(ctx.body(), RegisterRequest.class);
                LoginResult loginResult = userService.register(registerRequest);
                String myResult = gson.toJson(loginResult);
                ctx.status(200).json(myResult);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: already taken")) {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(403).result(myResult);
                } else if (e.getMessage().equals("Error: bad request")) {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(400).json(myResult);
                } else {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(500).json(myResult);
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
                    String myResult = serializeE(e.getMessage());
                    ctx.status(401).result(myResult);
                } else if (e.getMessage().equals("Error: bad request")) {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(400).result(myResult);
                } else {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(500).json(myResult);
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
                    String myResult = serializeE(e.getMessage());
                    ctx.status(401).json(myResult);
                } else {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(500).json(myResult);
                }
            }
        });


        javalin.get("/game", ctx -> {
            try {
                String auth = ctx.header("authorization");
                ArrayList<GameData> games = gameService.getGames(auth);
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("games", games);
                String myResult = gson.toJson(responseMap);
                ctx.status(200).result(myResult).contentType("application/json");
            } catch (DataAccessException e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(401).json(myResult);
                } else {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(500).json(myResult);
                }
            }
        });

        javalin.post("/game", this::createGame);

        javalin.put("/game", this::joinGame);
    }

    private void createGame(Context ctx) {
        try {
            String auth = ctx.header("authorization");
            CreateGameRequest createGameRequest = gson.fromJson(ctx.body(), CreateGameRequest.class);

            int gameID = gameService.createGame(createGameRequest, auth);
            Map<String, Integer> response = new HashMap<>();
            response.put("gameID", gameID);
            String myResult = gson.toJson(response);
            ctx.status(200).result(myResult);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                String myResult = serializeE(e.getMessage());
                ctx.status(401).json(myResult);
            } else if (e.getMessage().equals("Error: bad request")) {
                String myResult = serializeE(e.getMessage());
                ctx.status(400).json(myResult);
            } else {
                String myResult = serializeE(e.getMessage());
                ctx.status(500).json(myResult);
            }
        }
    }

    private void joinGame(Context ctx) {
        try {
            String auth = ctx.header("authorization");
            JoinGameRequest joinGameRequest = gson.fromJson(ctx.body(), JoinGameRequest.class);

            gameService.joinGame(joinGameRequest, auth);

            ctx.status(200);
        } catch (DataAccessException e) {
            switch (e.getMessage()) {
                case "Error: unauthorized" -> {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(401).json(myResult);
                }
                case "Error: already taken" -> {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(403).json(myResult);
                }
                case "Error: bad request" -> {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(400).json(myResult);
                }
                default -> {
                    String myResult = serializeE(e.getMessage());
                    ctx.status(500).json(myResult);
                }
            }
        }
    }

    private String serializeE(String eMessage) {
        Map<String, String> errorInfo = new HashMap<>();
        errorInfo.put("message", eMessage);
        return gson.toJson(errorInfo);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
