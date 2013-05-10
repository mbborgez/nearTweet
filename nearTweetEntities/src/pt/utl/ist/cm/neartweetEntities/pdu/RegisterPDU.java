package pt.utl.ist.cm.neartweetEntities.pdu;


public class RegisterPDU extends PDU
{
	private static final long serialVersionUID = 1L;

	/**
	 * This message can be used when a user wants to register in a given server/network
	 * @param userId
	 */
	public RegisterPDU(String id, String userId) 
	{
		super(id, userId, null);
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processRegisterPDU(this);
	}
	
	@Override
	public String toString() {
		return "RegisterPDU: [ " + super.toString() + "]"; 
	}
	
}
