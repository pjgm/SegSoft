package exceptions;

public class UndefinedAccountException extends Exception {

	@Override
	public String getMessage() {
		return "User does not exist";
	}
}