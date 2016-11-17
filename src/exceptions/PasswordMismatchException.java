package exceptions;

public class PasswordMismatchException extends Exception {

	@Override
	public String getMessage() {
		return "The passwords do not match";
	}
}