package model;

public interface Account {

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getEmail();

    void setEmail(String email);

    String getPhone();

    void setPhone(String phone);

    int getLoggedIn();

    void setLoggedIn(int loggedIn);

    int getLocked();

    void setLocked(int locked);

    String getSalt();

    void setSalt(String salt);

    String getRole();

    void setRole(String role);

    String getBio();

    void setBio(String bio);

    String getSecretInfo();

    void setSecretInfo(String secretInfo);

}
