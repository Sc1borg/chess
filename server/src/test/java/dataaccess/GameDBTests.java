package dataaccess;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameDBTests {
    GameDAO gameDAO = new SQLGameDAO();
    GameService gameService = new GameService();
    UserService userService = new UserService();
    private int gameID;
    private LoginResult loginResult;

    @BeforeEach
    void clear() throws DataAccessException {
        gameDAO.clear();
        userService.clear();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        gameID = gameService.createGame(createGameRequest, loginResult.authToken());
    }

    @Test
    void createGameDBSuccess() throws DataAccessException {
        assert gameDAO.getGame(gameID);
    }

    @Test
    void createGameDBFailureDuplicate() {
        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "auth"));
    }

    @Test
    void createGameDBFailureAuth() {
        CreateGameRequest createGameRequest = new CreateGameRequest("JO mama");
        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake auth"));
    }


    @Test
    void getGamesDBSuccess() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.getGames();
        assertNotNull(games);
    }

    @Test
    void getGamesDBFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");

        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake Auth"));
    }

    @Test
    void joinGameDBFailureAuth() {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 12345);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, "Fake Auth"));
    }

    @Test
    void joinGameDBFailureID() {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 12346);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, "Auth"));
    }

    @Test
    void joinGameDBFailureColor() {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLICK", 12345);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, "auth"));
    }

    @Test
    void joinGameDBFailureNoGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 1);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, "auth"));
    }

    @Test
    void joinGameDBFailureFull() throws DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        gameService.joinGame(joinGameRequest, loginResult.authToken());

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, loginResult.authToken()));
    }

    @Test
    void joinGameDBSuccess() throws DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        gameService.joinGame(joinGameRequest, loginResult.authToken());

        assert gameService.getGames(loginResult.authToken()).contains(new GameData(gameID, null, loginResult.username(), "Urmom", new ChessGame()));
    }
}
