package dataaccess;

public interface AuthDAO {
    void clear();

    boolean getAuth(String authToken);

    void saveAuth(String authToken, String username);

    void removeAuth(String authToken);

}
