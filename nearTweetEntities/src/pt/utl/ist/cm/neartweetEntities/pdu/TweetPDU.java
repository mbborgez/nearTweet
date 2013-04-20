package pt.utl.ist.cm.neartweetEntities.pdu;


public class TweetPDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String tweetId;
	private String text;
	private byte[] mediaObject;
	
	/**
	 * @param userId
	 * @param tweetId
	 * @param text
	 * @param mediaObject
	 */
	public TweetPDU(String userId, String tweetId, String text, byte[] mediaObject) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.text = text;
		this.mediaObject = mediaObject;
	}

	/**
	 * 
	 * @return unique ID of this message
	 */
	public String GetTweetId()
	{
		return this.tweetId;
	}
	
	/**
	 * 
	 * @return content of the tweet message
	 */
	public String GetText()
	{
		return this.text;
	}
	
	/**
	 * 
	 * @return media object that is being sent
	 */
	public byte[] GetMediaObject()
	{
		return this.mediaObject;
	}
	/**
	 * 
	 * @return true if this message has a media object atached and false otherwise
	 */
	public boolean hasMediaObject() {
		return this.mediaObject!=null && this.mediaObject.length>0;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processTweetPDU(this);
	}
	
}
