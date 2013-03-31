package pt.utl.ist.cm.neartweetEntities.pdu;


public class SpamVotePDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String targetMessageId;
	
	public SpamVotePDU(String userId, String targetMessageId) 
	{
		super(userId);
		
		this.targetMessageId = targetMessageId;
	}

	public String GetTargetMessageId()
	{
		return this.targetMessageId;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processSpamVotePDU(this);
	}
}
