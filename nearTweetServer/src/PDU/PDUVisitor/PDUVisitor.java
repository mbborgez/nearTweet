package PDU.PDUVisitor;

import PDU.*;

public abstract class PDUVisitor 
{
	
	public PDUVisitor() {}
	
	abstract public void processPollVotePDU(PollVotePDU pdu);
	
	abstract public void processPublishPollPDU(PublishPollPDU pdu);
	
	abstract public void processRegisterPDU(RegisterPDU pdu);
	
	abstract public void processReplyPDU(ReplyPDU pdu);
	
	abstract public void processSpamVotePDU(SpamVotePDU pdu);
	
	abstract public void processTweetPDU(TweetPDU pdu);
}
