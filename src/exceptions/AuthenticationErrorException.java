package exceptions;

public class AuthenticationErrorException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Wrong authentication information";
	}
}