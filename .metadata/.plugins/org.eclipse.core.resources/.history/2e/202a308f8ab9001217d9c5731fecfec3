package pt.utl.ist.cm.neartweetEntities.pdu;


public class TweetPDU extends PDU
{
	private static final long serialVersionUID = 1L;
	private String text;
	private byte[] mediaObject;
	
	/**
	 * @param userId
	 * @param tweetId
	 * @param text
	 * @param mediaObject
	 */
	public TweetPDU(String userId, String id, String text, byte[] mediaObject) 
	{
		super(id, userId, null);
		
		this.text = text;
		this.mediaObject = mediaObject;
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
