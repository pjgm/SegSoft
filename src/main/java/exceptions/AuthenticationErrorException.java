package main.java.exceptions;

public class AuthenticationErrorException extends Exception {

	@Override
	public String getMessage() {
		return "Wrong authentication information";
	}
}