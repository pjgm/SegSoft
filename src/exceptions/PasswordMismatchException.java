package exceptions;

public class PasswordMismatchException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "The passwords do not match";
	}
}