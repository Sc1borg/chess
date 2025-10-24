package dataaccess;

public class DatabaseRegistry {
    private static final AuthDAO AUTH_DAO = new InMemoryAuthDAO();
    private static final UserDAO USER_DAO = new InMemoryUserDAO();
    private static final GameDAO gameDAO = new InMemoryGameDAO();

    public static GameDAO getGameDAO() {
        return gameDAO;
    }

    public static UserDAO getUserDao() {
        return USER_DAO;
    }

    public static AuthDAO getAuthDao() {
        return AUTH_DAO;
    }
}
