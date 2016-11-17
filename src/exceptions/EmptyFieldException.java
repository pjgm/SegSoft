package exceptions;

public class EmptyFieldException extends Exception {

	@Override
	public String getMessage() {
		return "A required field is empty";
	}
}