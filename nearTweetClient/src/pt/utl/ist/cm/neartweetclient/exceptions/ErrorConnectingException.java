package pt.utl.ist.cm.neartweetclient.exceptions;

public class ErrorConnectingException extends NearTweetException {
	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "Error connecting";
	
	public ErrorConnectingException(String message) {
		super(message);
	}

	public ErrorConnectingException() {
		super(MESSAGE);
	}

}
