package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.InMemoryGameDAO;
import model.CreateGameRequest;
import model.GameData;
import model.JoinGameRequest;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    InMemoryGameDAO gameDAO = new InMemoryGameDAO();
    UserService userService = new UserService();

    public void clear() {
        gameDAO.clear();
    }

    public ArrayList<GameData> getGames(String authToken) throws DataAccessException {
        if (userService.getAuth(authToken)) {
            return gameDAO.getGames();
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public int createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        if (userService.getAuth(authToken)) {
            int gameID = createGameID();
            gameDAO.createGame(new GameData(gameID, "", "", createGameRequest.gameName(), new ChessGame()));
            return gameID;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    private int createGameID() {
        int gameID = 0;
        while (gameDAO.getGame(gameID)) {
            gameID += 1;
        }
        return gameID;
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException {
        //Player color must be "WHITE" or "BLACK"
        if (!Objects.equals(joinGameRequest.playerColor(), "BLACK") && !Objects.equals(joinGameRequest.playerColor(), "White")) {
            throw new DataAccessException("Error: bad request");
        }
        //Check authToken
        if (!userService.getAuth(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        //Check that the game exists
        if (!gameDAO.getGame(joinGameRequest.gameID())) {
            throw new DataAccessException("Error: bad request: ");
        }
        //Get the username and try joining game
        String username = userService.getUsername(authToken);
        if (!gameDAO.joinGame(joinGameRequest.gameID(), joinGameRequest.playerColor(), username)) {
            throw new DataAccessException("Error: already taken");
        }
    }
}
