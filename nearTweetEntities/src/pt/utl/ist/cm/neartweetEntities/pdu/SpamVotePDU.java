package pt.utl.ist.cm.neartweetEntities.pdu;


public class SpamVotePDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String targetMessageId;
	private String targetUserId;
	
	/**
	 * 
	 * @param userId
	 * @param targetMessageId
	 * @param targerUserId
	 */
	public SpamVotePDU(String userId, String targetMessageId, String targerUserId) 
	{
		super(userId);
		
		this.targetMessageId = targetMessageId;
		this.targetUserId = targerUserId;
	}

	/**
	 * 
	 * @return the id of the message marked as spam
	 */
	public String GetTargetMessageId()
	{
		return this.targetMessageId;
	}

	/**
	 * 
	 * @return the ID of the user marked as spam
	 */
	public String getTargetUserId() {
		return targetUserId;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processSpamVotePDU(this);
	}
}
