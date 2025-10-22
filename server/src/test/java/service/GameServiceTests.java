package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.*;
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
    void createGame_failure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake Auth"));
    }

    @Test
    void getGames_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int _ = gameService.createGame(createGameRequest, loginResult.authToken());

        ArrayList<GameData> games = gameService.getGames(loginResult.authToken());
        assertNotNull(games);
    }

    @Test
    void getGames_failure() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult _ = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");

        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake Auth"));
    }

    @Test
    void joinGame_failureAuth() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, "Fake Auth"));
    }

    @Test
    void joinGame_failureColor() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLICK", gameID);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, loginResult.authToken()));
    }

    @Test
    void joinGame_failureNoGame() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int _ = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 1);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, loginResult.authToken()));
    }

    @Test
    void joinGame_failureFull() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        gameService.joinGame(joinGameRequest, loginResult.authToken());

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, loginResult.authToken()));
    }

    @Test
    void joinGame_success() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        gameService.joinGame(joinGameRequest, loginResult.authToken());

        assert gameService.getGames(loginResult.authToken()).contains(new GameData(gameID, "", loginResult.username(), "Urmom", new ChessGame()));
    }
}
