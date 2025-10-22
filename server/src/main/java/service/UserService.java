package service;

import dataaccess.DataAccessException;
import dataaccess.InMemoryAuthDAO;
import dataaccess.InMemoryUserDAO;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;

import java.util.UUID;

public class UserService {
    final InMemoryUserDAO userDAO = new InMemoryUserDAO();
    final InMemoryAuthDAO authDAO = new InMemoryAuthDAO();


    public void clear() {
        userDAO.clear();
        authDAO.clear();
    }


    public LoginResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDAO.getUsername(registerRequest.username())) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(registerRequest);
        String authToken = generateAuthToken(registerRequest.username());
        return new LoginResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if (!userDAO.getUsername(loginRequest.username())) {
            throw new DataAccessException("Error: unauthorized");
        }

        String authToken = generateAuthToken(loginRequest.username());
        return new LoginResult(loginRequest.username(), authToken);
    }

    public void logout(String authToken) throws DataAccessException {
        invalidateToken(authToken);
    }

    public String generateAuthToken(String username) {
        String authToken = UUID.randomUUID().toString();
        authDAO.saveAuth(authToken, username);
        return authToken;
    }

    public void invalidateToken(String auth) throws DataAccessException {
        if (authDAO.getAuth(auth)) {
            authDAO.removeAuth(auth);
            return;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public boolean getAuth(String authToken) {
        return authDAO.getAuth(authToken);
    }
}
