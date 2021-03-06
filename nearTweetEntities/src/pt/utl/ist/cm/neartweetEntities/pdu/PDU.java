package pt.utl.ist.cm.neartweetEntities.pdu;

import java.io.Serializable;

public abstract class PDU implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String destinationUserId;
	private String id;
	
	/**
	 * 
	 * @param userId
	 */
	public PDU(String id, String userId, String destinationUserId)
	{
		this.id = id;
		this.userId = userId;
		this.destinationUserId = destinationUserId;
	}	
	
	/**
	 * 
	 * @return the ID of the user who sent this message
	 */
	public String getUserId()
	{
		return this.userId;
	}
	
	/**
	 * 
	 * @return the ID of the user to who this message must be sent
	 */
	public String getDestinationUserId()
	{
		return this.destinationUserId;
	}

	public String getId() {
		return id;
	}

	abstract public void accept(PDUVisitor visitor);
	
	@Override
	public String toString() {
		return "id: " + getId() + ", userId: " + getUserId() + ", destinationUserId" + getDestinationUserId();
	}
}
