package service;


import dataaccess.DataAccessException;
import dataaccess.InMemoryUserDAO;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTests {

    private final UserService userService = new UserService();
    private InMemoryUserDAO userDAO;

    @Test
    void registerUser_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assert loginResult.username().equals(registerRequest.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void registerUser_failure() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assertThrows(DataAccessException.class, () -> {
            userService.register(registerRequest);
        });

    }

    @Test
    void loginUser_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult regResult = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResult loginResult = userService.login(loginRequest);

        assert loginResult.username().equals(loginRequest.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void loginUser_failure() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        assertThrows(DataAccessException.class, () -> {
            userService.login(loginRequest);
        });
    }
}