package service;


import dataaccess.DataAccessException;
import dataaccess.InMemoryUserDAO;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTests {

    private UserService userService;
    private InMemoryUserDAO userDAO;

    @Test
    void registerUser_success() throws DataAccessException {
        userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assert loginResult.username().equals(registerRequest.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void registerUser_failure() throws DataAccessException {
        userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assertThrows(DataAccessException.class, () -> {
            userService.register(registerRequest);
        });

    }
}