package dataaccess;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void clear() throws SQLException {

    }

    @Override
    public boolean getAuth(String authToken) {
        return false;
    }

    @Override
    public void saveAuth(String authToken, String username) {

    }

    @Override
    public void removeAuth(String authToken) {

    }

    @Override
    public String getUsername(String authToken) {
        return "";
    }
}
