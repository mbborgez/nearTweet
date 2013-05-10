package pt.utl.ist.cm.neartweetEntities.pdu;

import java.util.ArrayList;


public class PublishPollPDU extends PDU
{

	private static final long serialVersionUID = 1L;
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
		super(tweetId, userId, null);
		this.text = text;
		this.options = options;
	}

	/**
	 * 
	 * @return description of the poll
	 */
	public String getText()
	{
		return this.text;
	}
	
	/**
	 * 
	 * @return options in which the other users can vote
	 */
	public ArrayList<String> getOptions()
	{
		return this.options;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processPublishPollPDU(this);
	}
	
	@Override
	public String toString() {
		return "PublishPollPDU: [ " + super.toString() + ", text: " + getText() + ", options: " + getOptions() + "]"; 
	}
}
