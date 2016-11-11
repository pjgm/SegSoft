package exceptions;

public class UndefinedAccountException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "User does not exist";
	}
}