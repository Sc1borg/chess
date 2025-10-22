package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();

    ArrayList<GameData> getGames(String authToken);
}
