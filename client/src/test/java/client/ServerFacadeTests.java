package client;

import model.CreateGameRequest;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @BeforeEach
    public void setup() {
    }

    @Test
    public void registrationSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("random", "pass", "email");
        var result = serverFacade.register(registerRequest);
        assertTrue(true);
    }

    @Test
    public void registrationFailure() throws Exception {
        try {
            RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
            LoginResult loginResult = serverFacade.register(registerRequest);
        } catch (Exception e) {
            // do Nothing
        }
        RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
        assertThrows(Exception.class, () -> serverFacade.register(registerRequest));
    }

    @Test
    public void logoutFailure() throws Exception {
        LoginResult loginResult = new LoginResult("user", "auth");
        assertThrows(Exception.class, () -> serverFacade.logout(loginResult));
    }

    @Test
    public void logoutSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("randoma", "pass", "email");
        var result = serverFacade.register(registerRequest);
        serverFacade.logout(result);
        assertTrue(true);
    }

    @Test
    public void loginSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("random1", "pass", "email");
        var result = serverFacade.register(registerRequest);
        serverFacade.logout(result);
        LoginRequest loginRequest = new LoginRequest("random1", "pass");
        serverFacade.login(loginRequest);
        assertTrue(true);
    }

    @Test
    public void loginFailure() {
        LoginRequest loginRequest = new LoginRequest("random100", "pass");
        assertThrows(Exception.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    public void createFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        assertThrows(Exception.class, () -> serverFacade.create(createGameRequest, new LoginResult("user", "auth")));
    }

    @Test
    public void createSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("random2", "pass", "email");
        var result = serverFacade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        assertDoesNotThrow(() -> serverFacade.create(createGameRequest, result));
    }

    @Test
    public void listSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("random3", "pass", "email");
        var result = serverFacade.register(registerRequest);
        assertDoesNotThrow(() -> serverFacade.list(result));
    }

}