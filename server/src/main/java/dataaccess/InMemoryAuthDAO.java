package dataaccess;

import java.util.HashMap;

public class InMemoryAuthDAO implements AuthDAO {
    private final static HashMap<String, String> AUTH_DATA_MAP = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        AUTH_DATA_MAP.clear();
    }

    @Override
    public boolean getAuth(String authToken) {
        return AUTH_DATA_MAP.containsKey(authToken);
    }

    @Override
    public void saveAuth(String authToken, String username) {
        AUTH_DATA_MAP.put(authToken, username);
    }

    @Override
    public void removeAuth(String authToken) {
        AUTH_DATA_MAP.remove(authToken);
    }


    public String getUsername(String authToken) {
        return AUTH_DATA_MAP.get(authToken);
    }
}
