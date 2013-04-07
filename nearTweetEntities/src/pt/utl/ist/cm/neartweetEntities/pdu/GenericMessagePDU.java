package pt.utl.ist.cm.neartweetEntities.pdu;

public class GenericMessagePDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String description;
	private boolean response;
	
	public GenericMessagePDU(String userId, String description) {
		super(userId);
		this.description = description;
		this.response = false;
	}
	
	public GenericMessagePDU(String userId, String description, boolean response) {
		super(userId);
		this.description = description;
		this.response = response;
	}

	public String GetDescription() {
		return this.description;  
	}
	
	public boolean GetResponse() {
		return this.response;  
	}

	@Override
	public void accept(PDUVisitor visitor)  {
		visitor.processGenericMessagePDU(this);
	}
}
