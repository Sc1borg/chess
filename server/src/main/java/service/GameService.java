package service;

import dataaccess.InMemoryGameDAO;

public class GameService {
    InMemoryGameDAO gameDAO = new InMemoryGameDAO();

    public void clear() {
        gameDAO.clear();
    }
}
