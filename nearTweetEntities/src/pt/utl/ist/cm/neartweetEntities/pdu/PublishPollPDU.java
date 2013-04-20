package pt.utl.ist.cm.neartweetEntities.pdu;

import java.util.ArrayList;


public class PublishPollPDU extends PDU
{

	private static final long serialVersionUID = 1L;
	private String tweetId;
	private String text;
	private ArrayList<String> options;

	/**
	 * @param userId
	 * @param tweetId
	 * @param text
	 * @param options
	 */
	public PublishPollPDU(String userId, String tweetId, String text, ArrayList<String> options) 
	{
		super(userId);
		this.tweetId = tweetId;
		this.text = text;
		this.options = options;
	}

	/**
	 * 
	 * @return Unique ID of the message
	 */
	public String GetTweetId()
	{
		return this.tweetId;
	}
	
	/**
	 * 
	 * @return description of the poll
	 */
	public String GetText()
	{
		return this.text;
	}
	
	/**
	 * 
	 * @return options in which the other users can vote
	 */
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
