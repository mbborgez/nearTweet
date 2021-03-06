package pt.utl.ist.cm.neartweetclient.utils;

public class Constants {

	private static final String CONSTANT_PREFIX = "pt.utl.ist.cm.neartweetclient";
	
	public static final String GENERIC_MESSAGE_PDU_RECEIVED = CONSTANT_PREFIX + "GENERIC_MESSAGE_PDU_RECEIVED";

	public static final String POLL_VOTE_PDU_RECEIVED = CONSTANT_PREFIX + "POLL_VOTE_PDU_RECEIVED";

	public static final String PUBLISH_POLL_PDU_RECEIVED = CONSTANT_PREFIX + "PUBLISH_POLL_PDU_RECEIVED";

	public static final String REGISTER_PDU_RECEIVED = CONSTANT_PREFIX + "REGISTER_PDU_RECEIVED";

	public static final String REPLY_PDU_RECEIVED = CONSTANT_PREFIX + "REPLY_PDU_RECEIVED";

	public static final String SPAM_VOTE_PDU_RECEIVED = CONSTANT_PREFIX + "SPAM_VOTE_PDU_RECEIVED";

	public static final String TWEET_PDU_RECEIVED = CONSTANT_PREFIX + "TWEET_PDU_RECEIVED";

	public static final String MESSAGE_RECEIVED_DATA = CONSTANT_PREFIX + "MESSAGE_RECEIVED_DATA";

	public static final String ACTION_CONNECTION_STATUS = CONSTANT_PREFIX + "ACTION_CONNECTION_STATUS";

	public static final String CONNECTION_STATUS = CONSTANT_PREFIX + "CONNECTION_STATUS";
	
	public static final Integer CONNECTION_STATUS_OK = 1;
	
	public static final Integer CONNECTION_STATUS_FAIL = -1;
	
	public static final String SERVER_ADDRESS = "10.0.2.2";
	
	public static final Integer SERVER_PORT = 8000;
	
	public static final Integer CONNECTION_LISTENER_PORT = 10001;

}
