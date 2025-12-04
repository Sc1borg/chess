package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryGameDAO implements GameDAO {
    private final static HashMap<Integer, GameData> GAME_DATA_MAP = new HashMap<>();

    @Override
    public void clear() {
        GAME_DATA_MAP.clear();
    }

    @Override
    public ArrayList<GameData> getGames() {
        return new ArrayList<>(GAME_DATA_MAP.values());
    }

    @Override
    public void createGame(GameData gameData) {
        GAME_DATA_MAP.put(gameData.gameID(), gameData);
    }

    public boolean findGame(int gameID) {
        return GAME_DATA_MAP.containsKey(gameID);
    }


    public boolean joinGame(int gameID, String playerColor, String username) {
        GameData gameData = GAME_DATA_MAP.get(gameID);
        GameData newGameData;

        if (playerColor.equals("BLACK") && gameData.blackUsername() == null) {
            newGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else if (playerColor.equals("WHITE") && gameData.whiteUsername() == null) {
            newGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else {
            return false;
        }
        GAME_DATA_MAP.put(gameID, newGameData);
        return true;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return GAME_DATA_MAP.get(gameID);
    }

    @Override
    public void leaveGame(int gameID, String username) throws DataAccessException {
        
    }
}
