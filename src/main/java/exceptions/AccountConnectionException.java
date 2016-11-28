package exceptions;

public class AccountConnectionException extends Exception {

	@Override
	public String getMessage() {
		return "User has to be logged out";
	}
}