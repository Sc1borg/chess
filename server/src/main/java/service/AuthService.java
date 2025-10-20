package service;
import dataaccess.InMemoryAuthDAO;

import java.util.UUID;

public class AuthService {
    private final InMemoryAuthDAO authDAO = new InMemoryAuthDAO();
    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
    public boolean validateToken(String auth) {
        return authDAO.getAuth(auth);
    }
    public void clear() {
        authDAO.clear();
    }
}
