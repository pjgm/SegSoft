package main.java.exceptions;

public class AccountConnectionException extends Exception {

	@Override
	public String getMessage() {
		return "User has to be logged out";
	}
}