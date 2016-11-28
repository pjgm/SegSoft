package main.java.validation;

import org.apache.commons.validator.routines.EmailValidator;
import java.util.regex.Pattern;

public class Validator {

    private static final String USERNAMEPATTERN = "^[a-z0-9]{2,16}$";
    private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";
    private static final String PHONEPATTERN = "\\d+";

    private Pattern usernamePattern;
    private Pattern passwordPattern;
    private Pattern phonePattern;

    public Validator() {
        usernamePattern = Pattern.compile(USERNAMEPATTERN);
        passwordPattern = Pattern.compile(PASSWORDPATTERN);
        phonePattern = Pattern.compile(PHONEPATTERN);
    }

    public boolean validateUsername(String username) {
        return usernamePattern.matcher(username).matches();
    }

    public boolean validatePassword(String password) {
        return passwordPattern.matcher(password).matches();
    }

    public boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean validatePhone(String phone) {
        return phonePattern.matcher(phone).matches();
    }
}
