package server;

import dataaccess.InMemoryAuthDAO;
import dataaccess.InMemoryGameDAO;
import dataaccess.InMemoryUserDAO;
import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final InMemoryAuthDAO authDAO = new InMemoryAuthDAO();
    private final InMemoryGameDAO gameDAO = new InMemoryGameDAO();
    private final InMemoryUserDAO userDAO = new InMemoryUserDAO();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();
            ctx.status(200).result("Database cleared");
        });
        javalin.post("/user", ctx -> {

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
