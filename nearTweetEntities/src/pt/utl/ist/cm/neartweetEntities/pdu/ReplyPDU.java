package pt.utl.ist.cm.neartweetEntities.pdu;


public class ReplyPDU extends PDU {

	private static final long serialVersionUID = 1L;
	private String tweetId;
	private String targetMessageId;
	private String text;
	private String addresse;
	private Boolean isBroadcast;
	
	
	//TODO:
	//CHANGE THE NAME OF THE "addresse" PROPERTY
	
	public ReplyPDU(String userId, String tweetId, String targetMessageId, String text, String addresse, Boolean isBroadcast) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.targetMessageId = targetMessageId;
		this.text = text;
		this.addresse = addresse;		
		this.isBroadcast = isBroadcast;
	}
	
	public String GetTweetId()
	{
		return this.tweetId;
	}
	
	public String GetTargetMessageId()
	{
		return this.targetMessageId;
	}
	
	public String GetText()
	{
		return this.text;
	}
	
	public String GetAddresse()
	{
		return this.addresse;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processReplyPDU(this);
	}

	public Boolean getIsBroadcast() {
		return isBroadcast;
	}

	public void setIsBroadcast(Boolean isBroadcast) {
		this.isBroadcast = isBroadcast;
	}

}
