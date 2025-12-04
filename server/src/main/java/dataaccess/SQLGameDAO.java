package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    private final Gson gson = new Gson();

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM game";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        String sql = "SELECT * FROM game";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    games.add(new GameData(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"),
                            resultSet.getString("blackUsername"), resultSet.getString("gameName"),
                            gson.fromJson(resultSet.getString("gameState"), ChessGame.class)));
                }
                return games;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        String gameJson = gson.toJson(gameData.game());
        String sql = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameState) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameData.gameID());
            stmt.setString(2, gameData.whiteUsername());
            stmt.setString(3, gameData.blackUsername());
            stmt.setString(4, gameData.gameName());
            stmt.setString(5, gameJson);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean findGame(int gameID) throws DataAccessException {
        String sql = "SELECT 1 FROM game WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();
                return new GameData(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"),
                        resultSet.getString("blackUsername"), resultSet.getString("gameName"),
                        gson.fromJson(resultSet.getString("gameState"), ChessGame.class));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean joinGame(int gameID, String playerColor, String username) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();
                if (playerColor.equals("BLACK") && resultSet.getString("blackUsername") == null) {
                    try (PreparedStatement stmt1 = conn.prepareStatement("UPDATE game SET blackUsername =? WHERE gameID = ?")) {
                        stmt1.setString(1, username);
                        stmt1.setInt(2, gameID);
                        stmt1.executeUpdate();
                    }
                } else if (playerColor.equals("WHITE") && resultSet.getString("whiteUsername") == null) {
                    try (PreparedStatement stmt2 = conn.prepareStatement("UPDATE game SET whiteUsername =? WHERE gameID = ?")) {
                        stmt2.setString(1, username);
                        stmt2.setInt(2, gameID);
                        stmt2.executeUpdate();
                    }
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return true;
    }

    public void leaveGame(int gameID, String username) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();
                if (username.equals(resultSet.getString("whiteUsername"))) {
                    try (PreparedStatement stmt1 = conn.prepareStatement("UPDATE game SET whiteUsername =? WHERE gameID = ?")) {
                        stmt1.setString(1, null);
                        stmt1.setInt(2, gameID);
                        stmt1.executeUpdate();
                    }
                } else if (username.equals(resultSet.getString("blackUsername"))) {
                    try (PreparedStatement stmt2 = conn.prepareStatement("UPDATE game SET blackUsername =? WHERE gameID = ?")) {
                        stmt2.setString(1, null);
                        stmt2.setInt(2, gameID);
                        stmt2.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        String gameJson = gson.toJson(game.game());
        String sql = "UPDATE game SET game =? WHERE gameID =?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gameJson);
            stmt.setInt(2, game.gameID());
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
