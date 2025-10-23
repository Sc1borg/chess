package dataaccess;

import model.RegisterRequest;

public interface UserDAO {
    void clear();

    boolean getUsername(String username);

    void createUser(RegisterRequest userData);

    boolean matchPassword(String username, String password);
}
