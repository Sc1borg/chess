package dataaccess;

import model.RegisterRequest;

import java.util.HashMap;

public class InMemoryUserDAO implements UserDAO {
    private final static HashMap<String, RegisterRequest> USER_MAP = new HashMap<>();

    @Override
    public void clear() {
        USER_MAP.clear();
    }

    @Override
    public boolean getUsername(String username) {
        return USER_MAP.containsKey(username);
    }

    @Override
    public void createUser(RegisterRequest userData) {
        USER_MAP.put(userData.username(), userData);
    }

    @Override
    public boolean matchPassword(String username, String password) {
        System.out.println("In the userDAO");
        return (USER_MAP.get(username).password().equals(password));
    }


}
