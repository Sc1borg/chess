package service;

import dataaccess.InMemoryUserDAO;
import model.RegisterRequest;

public class UserService {
    InMemoryUserDAO userDAO = new InMemoryUserDAO();

    public void clear() {
        userDAO.clear();
    }

    public boolean getUsername(String username) {
        return userDAO.getUsername(username);
    }

    public void createUser(RegisterRequest registerRequest) {
        userDAO.createUser(registerRequest);
    }
}
