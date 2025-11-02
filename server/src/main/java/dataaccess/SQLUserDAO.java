package dataaccess;

import model.RegisterRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
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
    public boolean getUsername(String username) throws DataAccessException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        return sqlBoolean(username, sql);
    }

    static boolean sqlBoolean(String injection, String sql) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, injection);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createUser(RegisterRequest userData) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userData.username());
            stmt.setString(2, userData.password());
            stmt.setString(3, userData.email());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String readPassword(String username) throws DataAccessException {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
