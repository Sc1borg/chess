package server;

import com.google.gson.Gson;
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
            RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            if (userService.getUsername(registerRequest.username())) {
                ctx.status(403).result("Error: username already taken");
            }
            userService.createUser(registerRequest);

            String authToken = authService.generateAuthToken();
            authService.saveAuth(authToken, registerRequest.username());
            LoginResult response = new LoginResult(registerRequest.username(), authToken);

            ctx.status(200).json(response);
        });


        javalin.post("/session", ctx -> {
            LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
            if (!userService.getUsername(loginRequest.username())) {
                ctx.status(401).result("message: Error no user found");
            }
            String authToken = authService.generateAuthToken();
            authService.saveAuth(authToken, loginRequest.username());
            LoginResult response = new LoginResult(loginRequest.username(), authToken);

            ctx.status(200).json(response);
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
