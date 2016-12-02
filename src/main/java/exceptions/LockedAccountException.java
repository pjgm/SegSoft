package main.java.exceptions;

public class LockedAccountException extends Exception {

	private String message;

	public LockedAccountException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}