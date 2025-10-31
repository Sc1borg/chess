package dataaccess;

public interface AuthDAO {
    void clear() throws DataAccessException;

    boolean getAuth(String authToken) throws DataAccessException;

    void saveAuth(String authToken, String username) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

}
