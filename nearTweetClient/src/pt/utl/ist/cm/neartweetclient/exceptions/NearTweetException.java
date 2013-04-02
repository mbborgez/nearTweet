package pt.utl.ist.cm.neartweetclient.exceptions;

public class NearTweetException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String messageFormat = "[Near Tweet Exception] => Message %s";
	private String message;
	
	public NearTweetException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return String.format(messageFormat, this.message);
	}
	
}
