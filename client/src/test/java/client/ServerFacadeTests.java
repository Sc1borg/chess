package java.client;

import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import server.ServerFacade;


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

    @BeforeEach
    void registerSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
        serverFacade.register(registerRequest);
    }


    @Test
    public void registerFailure() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
        LoginResult result = serverFacade.register(registerRequest);
        assertTrue(obj instanceof YourClass);
    }

}
