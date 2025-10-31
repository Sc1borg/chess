package dataaccess;

public class DatabaseRegistry {
    private static final AuthDAO AUTH_DAO = new SQLAuthDAO();
    private static final UserDAO USER_DAO = new SQLUserDAO();
    private static final GameDAO GAME_DAO = new InMemoryGameDAO();

    public static GameDAO getGameDao() {
        return GAME_DAO;
    }

    public static UserDAO getUserDao() {
        return USER_DAO;
    }

    public static AuthDAO getAuthDao() {
        return AUTH_DAO;
    }
}
