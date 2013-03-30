package pt.utl.ist.cm.neartweetEntities.PDU;

public class GenericMessagePDU extends PDU
{
	private static final long serialVersionUID = 1L;
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
		visitor.processGenericMessagePDU(this);
	}
}
