package PDU;

import PDU.PDUVisitor.PDUVisitor;

public class GenericMessagePDU extends PDU
{
	private String description;
	
	public GenericMessagePDU(String userId, String description) 
	{
		super(userId);
		
		this.description = description;
	}

	public String GetDescription()
	{
		return this.description;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
	}
}
