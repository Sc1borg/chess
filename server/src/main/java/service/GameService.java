package service;

import dataaccess.DataAccessException;
import dataaccess.InMemoryGameDAO;
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
            return gameDAO.getGames(authToken);
        }
        throw new DataAccessException("Error: unauthorized");
    }
}
