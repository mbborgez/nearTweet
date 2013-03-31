package pt.utl.ist.cm.neartweetEntities.pdu;

import java.io.Serializable;

public abstract class PDU implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	
	public PDU(String userId)
	{
		this.userId = userId;
	}	
	
	public String GetUserId()
	{
		return this.userId;
	}
	
	abstract public void accept(PDUVisitor visitor);
}
