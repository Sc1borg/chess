package dataaccess;


import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDBTests {

    private final UserService userService = new UserService();
    private final UserDAO userDAO = new SQLUserDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();
    private LoginResult loginResult;

    @BeforeEach
    void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();

        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        loginResult = userService.register(registerRequest);
    }

    @Test
    void registerUserSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username1", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        assert loginResult.username().equals(registerRequest.username());
    }

    @Test
    void registerUserFailureDuplicate() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");

        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));

    }

    @Test
    void registerUserFailureUsername() {
        RegisterRequest registerRequest = new RegisterRequest(null, "password", "email");

        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));

    }

    @Test
    void registerUserFailurePassword() {
        RegisterRequest registerRequest = new RegisterRequest("username1", null, "email");

        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));
    }

    @Test
    void registerUserFailureEmail() {
        RegisterRequest registerRequest = new RegisterRequest("username1", "password", null);

        assertThrows(DataAccessException.class, () -> userService.register(registerRequest));

    }

    @Test
    void loginUserSuccess() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResult loginResult = userService.login(loginRequest);

        assert loginResult.username().equals(loginRequest.username());
    }

    @Test
    void loginUserFailure() {
        LoginRequest loginRequest = new LoginRequest("username1", "password");

        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    void loginUserFailurePassword() {
        LoginRequest loginRequest = new LoginRequest("username", null);

        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    void loginUserFailureUsername() {
        LoginRequest loginRequest = new LoginRequest(null, "password");

        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

    @Test
    void logoutUserSuccess() throws DataAccessException {
        userService.logout(loginResult.authToken());
        assertFalse(userService.getAuth(loginResult.authToken()));
    }

    @Test
    void logoutUserFailure() {
        assertThrows(DataAccessException.class, () -> userService.logout("Invalid Auth"));
    }
}