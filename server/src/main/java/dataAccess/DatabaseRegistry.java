package dataAccess;

public class DatabaseRegistry {
    private static final AuthDAO authDAO = new InMemoryAuthDAO();
    private static final UserDAO userDAO = new InMemoryUserDAO();
    private static final GameDAO gameDAO = new InMemoryGameDAO();

    public static GameDAO getGameDAO() {
        return gameDAO;
    }

    public static UserDAO getUserDAO() {
        return userDAO;
    }

    public static AuthDAO getAuthDAO() {
        return authDAO;
    }
}
