package service;

import dataaccess.DataAccessException;
import dataaccess.InMemoryUserDAO;
import model.LoginResult;
import model.RegisterRequest;

public class UserService {
    InMemoryUserDAO userDAO = new InMemoryUserDAO();
    AuthService authService;


    public void clear() {
        userDAO.clear();
    }

    public boolean getUsername(String username) {
        return userDAO.getUsername(username);
    }

    public LoginResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDAO.getUsername(registerRequest.username())) {
            throw new DataAccessException("Error: Username already taken");
        }
        authService = new AuthService();
        userDAO.createUser(registerRequest);
        String authToken = authService.generateAuthToken();
        authService.saveAuth(authToken, registerRequest.username());
        return new LoginResult(registerRequest.username(), authToken);
    }
}
