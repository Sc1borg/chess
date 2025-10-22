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


}
