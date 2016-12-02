package main.java.model;

public interface Account {

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getEmail();

    void setEmail(String email);

    String getEmailPL();

    void setEmailPL(String lvl);

    String getPhone();

    void setPhone(String phone);

    String getPhonePL();

    void setPhonePL(String lvl);

    String getRole();

    void setRole(String role);

    String getPublicInfo();

    void setPublicInfo(String publicInfo);

    String getPIPL();

    void setPIPL(String lvl);

    String getInternalInfo();

    void setInternalInfo(String internalInfo);

    String getIIPL();

    void setIIPL(String lvl);

    String getSecretInfo();

    void setSecretInfo(String secretInfo);

    String getSIPL();

    void setSIPL(String lvl);

    int getLoggedIn();

    void setLoggedIn(int loggedIn);

    int getLocked();

    void setLocked(int locked);

    String getSalt();

    void setSalt(String salt);
}