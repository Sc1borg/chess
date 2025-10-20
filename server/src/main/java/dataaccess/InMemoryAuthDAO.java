package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class InMemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataMap = new HashMap<String, AuthData>();

    @Override
    public void clear() {
        authDataMap.clear();
    }
}
