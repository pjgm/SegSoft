package exceptions;

public class ExistingAccountException extends Exception {

	@Override
	public String getMessage() {
		return "User already exists";
	}
}