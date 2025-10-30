package dataaccess;

import java.sql.SQLException;

public interface AuthDAO {
    void clear() throws SQLException;

    boolean getAuth(String authToken);

    void saveAuth(String authToken, String username);

    void removeAuth(String authToken);

    String getUsername(String authToken);

}
