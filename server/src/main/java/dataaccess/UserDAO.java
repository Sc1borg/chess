package dataaccess;

import model.RegisterRequest;

public interface UserDAO {
    void clear() throws DataAccessException;

    boolean getUsername(String username) throws DataAccessException;

    void createUser(RegisterRequest userData) throws DataAccessException;

    String readPassword(String username) throws DataAccessException;
}
