import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SchemaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            DatabaseManager.createDatabase();
            SchemaManager.initializeSchema();
        } catch (DataAccessException ex) {
            log.error("e: ", ex);
        }
        Server server = new Server();
        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}