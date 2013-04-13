package pt.utl.ist.cm.neartweetEntities.pdu;


public class PollVotePDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String targetMessageId;
	private int optionPosition;
	private String tweetId;
	
	public PollVotePDU(String userId, String tweetId, String targetMessageId, int optionPosition) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.targetMessageId = targetMessageId;
		this.optionPosition = optionPosition;
	}

	public String GetTargetMessageId()
	{
		return this.targetMessageId;
	}

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
}
