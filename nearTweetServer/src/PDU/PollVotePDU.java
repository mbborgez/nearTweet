package PDU;

import PDU.PDUVisitor.PDUVisitor;

public class PollVotePDU extends PDU
{
	private String targetMessageId;
	private int optionPosition;
	
	public PollVotePDU(String userId, String targetMessageId, int optionPosition) 
	{
		super(userId);
		
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
}
