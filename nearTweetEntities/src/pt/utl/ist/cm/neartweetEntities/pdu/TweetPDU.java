package pt.utl.ist.cm.neartweetEntities.pdu;


public class TweetPDU extends PDU
{

	private static final long serialVersionUID = 1L;
	private String tweetId;
	private String text;
	private byte[] mediaObject;
	
	public TweetPDU(String userId, String tweetId, String text, byte[] mediaObject) 
	{
		super(userId);
		
		this.tweetId = tweetId;
		this.text = text;
		this.mediaObject = mediaObject;
	}

	public String GetTweetId()
	{
		return this.tweetId;
	}
	
	public String GetText()
	{
		return this.text;
	}
	
	public byte[] GetMediaObject()
	{
		return this.mediaObject;
	}

	@Override
	public void accept(PDUVisitor visitor) 
	{
		visitor.processTweetPDU(this);
	}
	
}
