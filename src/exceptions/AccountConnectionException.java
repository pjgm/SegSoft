package exceptions;

public class AccountConnectionException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "User has to be logged out";
	}
}