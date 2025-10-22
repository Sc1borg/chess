package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.InMemoryGameDAO;
import model.CreateGameRequest;
import model.GameData;

import java.util.ArrayList;

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
}
