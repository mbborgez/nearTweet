package pt.utl.ist.cm.neartweetEntities.pdu;


public class PollVotePDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String targetMessageId;
	private int optionPosition;
	private String tweetId;
	private String originalUserId;
	
	/**
	 * @param userId
	 * @param tweetId
	 * @param targetMessageId
	 * @param optionPosition
	 * @param originalUserId
	 */
	public PollVotePDU(String userId, String tweetId, String targetMessageId, int optionPosition, String originalUserId) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.targetMessageId = targetMessageId;
		this.optionPosition = optionPosition;
		this.originalUserId = originalUserId;
	}

	/**
	 * @return the ID of the user who should receive this message. 
	 * The target should be the ID of the user who created the original Poll
	 */
	public String GetTargetMessageId()
	{
		return this.targetMessageId;
	}

	/**
	 * @return ID of the vote
	 */
	public int GetOptionPosition()
	{
		return this.optionPosition;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processPollVotePDU(this);
	}

	public String GetTweetId() {
		return tweetId;
	}

	public String getOriginalUserId() {
		return originalUserId;
	}

	public void setOriginalUserId(String originalUserId) {
		this.originalUserId = originalUserId;
	}
}
