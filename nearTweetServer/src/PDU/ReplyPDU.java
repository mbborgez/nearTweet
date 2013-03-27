package PDU;

import PDU.PDUVisitor.PDUVisitor;

public class ReplyPDU extends PDU
{

	private String tweetId;
	private String targetMessageId;
	private String text;
	private String addresse;
	
	public ReplyPDU(String userId, String tweetId, String targetMessageId ,String text, String addresse) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.targetMessageId = targetMessageId;
		this.text = text;
		this.addresse = addresse;		
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

}