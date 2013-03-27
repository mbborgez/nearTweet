package PDU;

import PDU.PDUVisitor.PDUVisitor;

public class RegisterPDU extends PDU
{

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
