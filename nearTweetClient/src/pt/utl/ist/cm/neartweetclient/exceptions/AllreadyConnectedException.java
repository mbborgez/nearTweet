package pt.utl.ist.cm.neartweetclient.exceptions;


public class AllreadyConnectedException extends NearTweetException {

	private static final String MESSAGE = "Allready connected. Disconected first";

	public AllreadyConnectedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public AllreadyConnectedException() {
		super(MESSAGE);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
