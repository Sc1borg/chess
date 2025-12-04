package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseRegistry;
import model.CreateGameRequest;
import model.GameData;
import model.JoinGameRequest;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    UserService userService = new UserService();

    public void clear() throws DataAccessException {
        DatabaseRegistry.getGameDao().clear();
    }

    public ArrayList<GameData> getGames(String authToken) throws DataAccessException {
        if (userService.getAuth(authToken)) {
            return DatabaseRegistry.getGameDao().getGames();
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public int createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        if (createGameRequest.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (userService.getAuth(authToken)) {
            int gameID = createGameID();
            DatabaseRegistry.getGameDao().createGame(new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame()));
            return gameID;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    private int createGameID() throws DataAccessException {
        int gameID = 12345;
        while (DatabaseRegistry.getGameDao().findGame(gameID)) {
            gameID += 1;
        }
        return gameID;
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException {
        //Player color must be "WHITE" or "BLACK"
        if (!Objects.equals(joinGameRequest.playerColor(), "BLACK") && !Objects.equals(joinGameRequest.playerColor(), "WHITE")) {
            throw new DataAccessException("Error: bad request");
        }
        //Check authToken
        if (!userService.getAuth(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        //Check that the game exists
        if (!DatabaseRegistry.getGameDao().findGame(joinGameRequest.gameID())) {
            throw new DataAccessException("Error: bad request");
        }
        //Get the username and try joining game
        String username = userService.getUsername(authToken);
        if (!DatabaseRegistry.getGameDao().joinGame(joinGameRequest.gameID(), joinGameRequest.playerColor(), username)) {
            throw new DataAccessException("Error: already taken");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return DatabaseRegistry.getGameDao().getGame(gameID);
    }

    public void updateGame(GameData game) {
        DatabaseRegistry.getGameDao().updateGame(game);
    }
}
