package service;

import dataaccess.InMemoryUserDAO;

public class UserService {
    InMemoryUserDAO userDAO = new InMemoryUserDAO();

    public void clear() {
        userDAO.clear();
    }
}
