package service;


import dataAccess.DataAccessException;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {

    private final UserService userService = new UserService();

    @BeforeEach
    void clear() {
        userService.clear();
    }

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

        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));

    }

    @Test
    void loginUser_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult1 = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResult loginResult = userService.login(loginRequest);

        assert loginResult.username().equals(loginRequest.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void loginUser_failure() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    void logoutUser_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assertNotNull(loginResult.authToken());

        userService.logout(loginResult.authToken());
        assertFalse(userService.getAuth(loginResult.authToken()));
    }

    @Test
    void logoutUser_failure() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assertThrows(DataAccessException.class, () -> userService.logout("Invalid Auth"));
    }
}