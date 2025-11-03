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
    UserDAO userDAO = new SQLUserDAO();
    GameService gameService = new GameService();
    UserService userService = new UserService();

    @BeforeEach
    void clear() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());

        assert gameID == 12345;
        assert gameDAO.getGame(12345);
    }

    @Test
    void createGameFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake Auth"));
    }

    @Test
    void getGamesSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());

        ArrayList<GameData> games = gameService.getGames(loginResult.authToken());
        assertNotNull(games);
    }

    @Test
    void getGamesFailure() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");

        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest, "Fake Auth"));
    }

    @Test
    void joinGameFailureAuth() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, "Fake Auth"));
    }

    @Test
    void joinGameFailureColor() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLICK", gameID);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, loginResult.authToken()));
    }

    @Test
    void joinGameFailureNoGame() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 1);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, loginResult.authToken()));
    }

    @Test
    void joinGameFailureFull() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        gameService.joinGame(joinGameRequest, loginResult.authToken());

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, loginResult.authToken()));
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginResult loginResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("Urmom");
        int gameID = gameService.createGame(createGameRequest, loginResult.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        gameService.joinGame(joinGameRequest, loginResult.authToken());

        assert gameService.getGames(loginResult.authToken()).contains(new GameData(gameID, null, loginResult.username(), "Urmom", new ChessGame()));
    }
}
