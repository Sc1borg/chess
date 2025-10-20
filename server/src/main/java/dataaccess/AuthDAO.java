package dataaccess;

public interface AuthDAO {
    void clear();
    boolean getAuth(String authToken);

}
