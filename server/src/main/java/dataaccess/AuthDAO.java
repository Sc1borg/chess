package dataaccess;

import java.sql.SQLException;

public interface AuthDAO {
    void clear() throws SQLException, DataAccessException;

    boolean getAuth(String authToken) throws DataAccessException;

    void saveAuth(String authToken, String username) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

}
