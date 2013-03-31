package pt.utl.ist.cm.neartweetEntities.pdu;


public class RegisterPDU extends PDU
{
	private static final long serialVersionUID = 1L;

	public RegisterPDU(String userId) 
	{
		super(userId);
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processRegisterPDU(this);
	}
	
}
