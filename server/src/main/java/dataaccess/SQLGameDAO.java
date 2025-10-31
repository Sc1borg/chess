package dataaccess;

import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public void createGame(GameData gameData) {
        String sql = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameState) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gameData.gameID());
            stmt.setString(2, gameData.whiteUsername());
            stmt.setString();
        }
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
