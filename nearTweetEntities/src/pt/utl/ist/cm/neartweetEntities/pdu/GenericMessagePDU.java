package pt.utl.ist.cm.neartweetEntities.pdu;

public class GenericMessagePDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String description;
	private boolean response;
	
	/**
	 * 
	 * @param userId
	 * @param description
	 */
	
	public GenericMessagePDU(String id, String userId, String description) {
		super(id, userId, null);
		this.description = description;
		this.response = false;
	}
	
	/**
	 * 
	 * @param userId
	 * @param description
	 * @param response
	 */
	public GenericMessagePDU(String id, String userId, String description, boolean response) {
		super(id, userId, null);
		this.description = description;
		this.response = response;
	}

	/**
	 * 
	 * @return the description of the generic message
	 */
	public String getDescription() {
		return this.description;  
	}
	
	/**
	 * 
	 * @return true if the generic message is a response and returns false otherwise
	 */
	public boolean getResponse() {
		return this.response;  
	}

	@Override
	public void accept(PDUVisitor visitor)  {
		visitor.processGenericMessagePDU(this);
	}
}
