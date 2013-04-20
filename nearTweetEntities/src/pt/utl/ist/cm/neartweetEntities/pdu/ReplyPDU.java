package pt.utl.ist.cm.neartweetEntities.pdu;


public class ReplyPDU extends PDU {

	private static final long serialVersionUID = 1L;
	private String tweetId;
	private String targetMessageId;
	private String text;
	private String targetUserId;
	private Boolean isBroadcast;
	
	
	/**
	 * @param userId
	 * @param tweetId
	 * @param targetMessageId
	 * @param text
	 * @param targetUserId
	 * @param isBroadcast
	 */
	public ReplyPDU(String userId, String tweetId, String targetMessageId, String text, String targetUserId, Boolean isBroadcast) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.targetMessageId = targetMessageId;
		this.text = text;
		this.targetUserId = targetUserId;		
		this.isBroadcast = isBroadcast;
	}
	
	/**
	 * 
	 * @return unique id of this message
	 */
	public String GetTweetId()
	{
		return this.tweetId;
	}
	/**
	 * 
	 * @return ID of the message to which we are replying
	 */
	public String GetTargetMessageId()
	{
		return this.targetMessageId;
	}
	
	/**
	 * 
	 * @return content of the reply message
	 */
	public String GetText()
	{
		return this.text;
	}
	
	/**
	 * 
	 * @return ID of the user who created the original tweet message
	 */
	public String getTargetUserId()
	{
		return this.targetUserId;
	}

	/**
	 * 
	 * @return true if the user is replying in broadcast 
	 * and false if the message is to be sent only to the user who created the original tweet message
	 */
	public Boolean getIsBroadcast() {
		return isBroadcast;
	}
	
	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processReplyPDU(this);
	}

}
