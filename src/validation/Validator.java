package validation;

import java.util.regex.Pattern;

public class Validator {

    private static final String USERNAMEPATTERN = "^[a-z0-9]{2,16}$";
    private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";

    private Pattern usernamePattern;
    private Pattern passwordPattern;

    public Validator() {
        usernamePattern = Pattern.compile(USERNAMEPATTERN);
        passwordPattern = Pattern.compile(PASSWORDPATTERN);
    }

    public boolean validateUsername(String username) {
        return usernamePattern.matcher(username).matches();
    }

    public boolean validatePassword(String password) {
        return passwordPattern.matcher(password).matches();
    }
}
