package model;

public interface Account {

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    int getLoggedIn();

    void setLoggedIn(int loggedIn);

    int getLocked();

    void setLocked(int locked);

    String getSalt();

    void setSalt(String salt);

    String getRole();

    void setRole(String role);

}
