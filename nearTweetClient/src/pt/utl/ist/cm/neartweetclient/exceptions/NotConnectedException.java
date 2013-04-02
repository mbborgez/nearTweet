package pt.utl.ist.cm.neartweetclient.exceptions;


public class NotConnectedException extends NearTweetException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Error - Not Connected.";

	public NotConnectedException() {
		super(MESSAGE);
		// TODO Auto-generated constructor stub
	}
	
	public NotConnectedException(String message){
		super(message);
	}


}
