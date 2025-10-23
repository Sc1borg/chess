package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseRegistry;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;

import java.util.UUID;

public class UserService {


    public void clear() {
        DatabaseRegistry.getUserDAO().clear();
        DatabaseRegistry.getAuthDAO().clear();
    }


    public LoginResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (registerRequest.password() == null || registerRequest.username() == null || registerRequest.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (DatabaseRegistry.getUserDAO().getUsername(registerRequest.username())) {
            throw new DataAccessException("Error: already taken");
        }
        DatabaseRegistry.getUserDAO().createUser(registerRequest);
        String authToken = generateAuthToken(registerRequest.username());
        return new LoginResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if ((loginRequest.username() == null || loginRequest.password() == null)) {
            throw new DataAccessException("Error: bad request");
        }
        if (!DatabaseRegistry.getUserDAO().getUsername(loginRequest.username())) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (!DatabaseRegistry.getUserDAO().matchPassword(loginRequest.username(), loginRequest.password())) {
            throw new DataAccessException("Error: unauthorized");
        }
        String authToken = generateAuthToken(loginRequest.username());
        return new LoginResult(loginRequest.username(), authToken);
    }

    public void logout(String authToken) throws DataAccessException {
        invalidateToken(authToken);
    }

    private String generateAuthToken(String username) {
        String authToken = UUID.randomUUID().toString();
        DatabaseRegistry.getAuthDAO().saveAuth(authToken, username);
        return authToken;
    }

    private void invalidateToken(String auth) throws DataAccessException {
        if (DatabaseRegistry.getAuthDAO().getAuth(auth)) {
            DatabaseRegistry.getAuthDAO().removeAuth(auth);
            return;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public boolean getAuth(String authToken) {
        return DatabaseRegistry.getAuthDAO().getAuth(authToken);
    }

    public String getUsername(String authToken) {
        return DatabaseRegistry.getAuthDAO().getUsername(authToken);
    }
}
