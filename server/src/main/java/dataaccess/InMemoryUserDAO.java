package dataaccess;

import model.UserData;

import java.util.HashMap;

public class InMemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userMap = new HashMap<String, UserData>();

    @Override
    public void clear() {
        userMap.clear();
    }
}
