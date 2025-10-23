package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryGameDAO implements GameDAO {
    private final static HashMap<Integer, GameData> gameDataMap = new HashMap<>();

    @Override
    public void clear() {
        gameDataMap.clear();
    }

    @Override
    public ArrayList<GameData> getGames() {
        return new ArrayList<>(gameDataMap.values());
    }

    @Override
    public void createGame(GameData gameData) {
        gameDataMap.put(gameData.GameID(), gameData);
    }

    public boolean getGame(int gameID) {
        return gameDataMap.containsKey(gameID);
    }


    public boolean joinGame(int gameID, String playerColor, String username) {
        GameData gameData = gameDataMap.get(gameID);
        GameData newGameData;

        if (playerColor.equals("BLACK") && gameData.blackUsername() == null) {
            newGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else if (playerColor.equals("WHITE") && gameData.whiteUsername() == null) {
            newGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else {
            return false;
        }
        gameDataMap.put(gameID, newGameData);
        return true;
    }
}
