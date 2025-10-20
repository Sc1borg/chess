package dataaccess;

import model.RegisterRequest;

import java.util.HashMap;

public class InMemoryUserDAO implements UserDAO {
    private final HashMap<String, RegisterRequest> userMap = new HashMap<>();

    @Override
    public void clear() {
        userMap.clear();
    }

    @Override
    public boolean getUsername(String username) {
        return userMap.containsKey(username);
    }

    @Override
    public void createUser(RegisterRequest userData) {
        userMap.put(userData.username(), userData);
    }
}
