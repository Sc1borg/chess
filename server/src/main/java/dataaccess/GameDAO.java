package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;

    ArrayList<GameData> getGames();

    void createGame(GameData gameData);

    boolean getGame(int gameID);

    boolean joinGame(int gameID, String playerColor, String username);
}
