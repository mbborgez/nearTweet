package pt.utl.ist.cm.neartweetclient.exceptions;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class ErrorSendingMessageException extends NearTweetException {

	private static final String ERROR_MESSAGE = "Error sending message";

	public ErrorSendingMessageException(PDU pdu) {
		super(ERROR_MESSAGE + " with pdu: " + pdu );
	}
	
	public ErrorSendingMessageException() {
		super(ERROR_MESSAGE);
	}

	private static final long serialVersionUID = 1L;

}
