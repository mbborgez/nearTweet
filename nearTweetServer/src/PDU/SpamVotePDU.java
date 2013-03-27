package PDU;

import PDU.PDUVisitor.PDUVisitor;

public class SpamVotePDU extends PDU
{
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
