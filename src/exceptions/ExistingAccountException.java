package exceptions;

public class ExistingAccountException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "User already exists";
	}
}