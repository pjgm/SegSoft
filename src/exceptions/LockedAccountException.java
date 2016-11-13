package exceptions;

public class LockedAccountException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;

	public LockedAccountException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}