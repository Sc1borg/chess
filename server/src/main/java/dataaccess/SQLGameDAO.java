package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public boolean getGame(int gameID) {
        return false;
    }

    @Override
    public boolean joinGame(int gameID, String playerColor, String username) {
        return false;
    }
}
