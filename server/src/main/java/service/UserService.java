package service;

import dataaccess.DataAccessException;
import dataaccess.InMemoryUserDAO;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;

public class UserService {
    final InMemoryUserDAO userDAO = new InMemoryUserDAO();
    final AuthService authService = new AuthService();


    public void clear() {
        userDAO.clear();
    }

    public boolean getUsername(String username) {
        return userDAO.getUsername(username);
    }

    public LoginResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDAO.getUsername(registerRequest.username())) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(registerRequest);
        String authToken = authService.generateAuthToken();
        authService.saveAuth(authToken, registerRequest.username());
        return new LoginResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if (!userDAO.getUsername(loginRequest.username())) {
            throw new DataAccessException("Error: unauthorized");
        }

        String authToken = authService.generateAuthToken();
        authService.saveAuth(authToken, loginRequest.username());
        return new LoginResult(loginRequest.username(), authToken);
    }
}
