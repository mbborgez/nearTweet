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
	public GenericMessagePDU(String userId, String description) {
		super(userId);
		this.description = description;
		this.response = false;
	}
	
	/**
	 * 
	 * @param userId
	 * @param description
	 * @param response
	 */
	public GenericMessagePDU(String userId, String description, boolean response) {
		super(userId);
		this.description = description;
		this.response = response;
	}

	/**
	 * 
	 * @return the description of the generic message
	 */
	public String GetDescription() {
		return this.description;  
	}
	
	/**
	 * 
	 * @return true if the generic message is a response and returns false otherwise
	 */
	public boolean GetResponse() {
		return this.response;  
	}

	@Override
	public void accept(PDUVisitor visitor)  {
		visitor.processGenericMessagePDU(this);
	}
}
