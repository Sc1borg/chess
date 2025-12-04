package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;

    ArrayList<GameData> getGames() throws DataAccessException;

    void createGame(GameData gameData) throws DataAccessException;

    boolean findGame(int gameID) throws DataAccessException;

    boolean joinGame(int gameID, String playerColor, String username) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void leaveGame(int gameID, String username) throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;
}
