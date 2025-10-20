package service;

import dataaccess.InMemoryAuthDAO;

import java.util.UUID;

public class AuthService {
    private final InMemoryAuthDAO authDAO = new InMemoryAuthDAO();

    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public boolean invalidateToken(String auth) {
        if (authDAO.getAuth(auth)) {
            authDAO.removeAuth(auth);
            return true;
        }
        return false;
    }

    public void clear() {
        authDAO.clear();
    }

    public void saveAuth(String authToken, String username) {
        authDAO.saveAuth(authToken, username);
    }
}
