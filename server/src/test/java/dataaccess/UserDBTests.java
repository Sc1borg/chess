package dataaccess;


import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserDBTests {

    private final UserService userService = new UserService();
    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();

    @BeforeEach
    void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    void registerUserSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assert loginResult.username().equals(registerRequest.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void registerUserFailure() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));

    }

    @Test
    void loginUserSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult1 = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResult loginResult = userService.login(loginRequest);

        assert loginResult.username().equals(loginRequest.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void loginUserFailure() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    void logoutUserSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assertNotNull(loginResult.authToken());

        userService.logout(loginResult.authToken());
        assertFalse(userService.getAuth(loginResult.authToken()));
    }

    @Test
    void logoutUserFailure() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assertThrows(DataAccessException.class, () -> userService.logout("Invalid Auth"));
    }
}