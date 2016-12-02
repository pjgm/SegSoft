package main.java.model;

public class AccountClass implements Account {

    private String username, password, email, emailpl, phone, phonepl, role, publicInfo, pipl, internalInfo, iipl,
            secretInfo, sipl, salt;
    private int loggedIn, locked;

    public AccountClass() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPL() {
        return emailpl;
    }

    public void setEmailPL(String lvl) {
        this.emailpl = lvl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonePL() {
        return phonepl;
    }

    public void setPhonePL(String lvl) {
        this.phonepl = lvl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPublicInfo() {
        return publicInfo;
    }

    public void setPublicInfo(String publicInfo) {
        this.publicInfo = publicInfo;
    }

    public String getPIPL() {
        return pipl;
    }

    public void setPIPL(String lvl) {
        this.pipl = lvl;
    }

    public String getInternalInfo() {
        return internalInfo;
    }

    public void setInternalInfo(String internalInfo) {
        this.internalInfo = internalInfo;
    }

    public String getIIPL() {
        return iipl;
    }

    public void setIIPL(String lvl) {
        this.iipl = lvl;
    }

    public String getSecretInfo() {
        return secretInfo;
    }

    public void setSecretInfo(String secretInfo) {
        this.secretInfo = secretInfo;
    }

    public String getSIPL() {
        return sipl;
    }

    public void setSIPL(String lvl) {
        this.sipl = lvl;
    }

    public int getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(int loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}