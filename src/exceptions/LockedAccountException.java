package exceptions;

public class LockedAccountException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "The account is locked";
	}

}