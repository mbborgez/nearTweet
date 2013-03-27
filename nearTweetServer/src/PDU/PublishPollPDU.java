package PDU;

import java.util.ArrayList;

import PDU.PDUVisitor.PDUVisitor;

public class PublishPollPDU extends PDU
{

	private String tweetId;
	private String text;
	private ArrayList<String> options;
	
	public PublishPollPDU(String userId, String tweetId, String text, ArrayList<String> options) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.text = text;
		this.options = options;
	}

	public String GetTweetId()
	{
		return this.tweetId;
	}
	
	public String GetText()
	{
		return this.text;
	}
	
	public ArrayList<String> GetOptions()
	{
		return this.options;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processPublishPollPDU(this);
	}
	
}
