package dataaccess;

import model.GameData;

import java.util.HashMap;

public class InMemoryGameDAO implements GameDAO {
    private final HashMap<String, GameData> gameDataMap = new HashMap<>();

    @Override
    public void clear() {
        gameDataMap.clear();
    }
}
