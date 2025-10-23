package dataAccess;

import java.util.HashMap;

public class InMemoryAuthDAO implements AuthDAO {
    private final static HashMap<String, String> authDataMap = new HashMap<>();

    @Override
    public void clear() {
        authDataMap.clear();
    }

    @Override
    public boolean getAuth(String authToken) {
        return authDataMap.containsKey(authToken);
    }

    @Override
    public void saveAuth(String authToken, String username) {
        authDataMap.put(authToken, username);
    }

    @Override
    public void removeAuth(String authToken) {
        authDataMap.remove(authToken);
    }


    public String getUsername(String authToken) {
        return authDataMap.get(authToken);
    }
}
