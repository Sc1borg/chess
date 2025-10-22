package service;

import dataaccess.DataAccessException;
import model.CreateGameRequest;
import model.GameData;
import model.LoginResult;
import model.RegisterRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTests {
    GameService gameService = new GameService();
    UserService userService = new UserService();

    @Test
    void createGame_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());

        assert gameID == 0;
    }

    @Test
    void createGame_failure() throws DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake Auth"));
    }

    @Test
    void getGames_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());

        ArrayList<GameData> games = gameService.getGames(loginResult.authToken());
        assertNotNull(games);
    }

    @Test
    void getGames_failure() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int _ = gameService.createGame(createGameRequest, loginResult.authToken());

        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake Auth"));
    }
}
