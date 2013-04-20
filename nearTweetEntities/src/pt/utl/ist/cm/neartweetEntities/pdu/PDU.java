package pt.utl.ist.cm.neartweetEntities.pdu;

import java.io.Serializable;

public abstract class PDU implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String userId;
	
	/**
	 * 
	 * @param userId
	 */
	public PDU(String userId)
	{
		this.userId = userId;
	}	
	
	/**
	 * 
	 * @return the ID of the user who sent this message
	 */
	public String GetUserId()
	{
		return this.userId;
	}
	
	abstract public void accept(PDUVisitor visitor);
}
