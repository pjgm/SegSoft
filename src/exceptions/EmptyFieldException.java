package exceptions;

public class EmptyFieldException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "A required field is empty";
	}
}