package dataaccess;

import model.RegisterRequest;

public class SQLUserDAO implements UserDAO {
    @Override
    public void clear() {
        
    }

    @Override
    public boolean getUsername(String username) {
        return false;
    }

    @Override
    public void createUser(RegisterRequest userData) {

    }

    @Override
    public String readPassword(String username) {
        return "";
    }
}
