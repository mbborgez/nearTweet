package main.java.com.cm.server.visitors;

import java.util.ArrayList;
import java.util.List;

import main.java.com.cm.server.handlers.RequestHandler;
import pt.utl.ist.cm.neartweetEntities.pdu.GenericMessagePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;

public class SlimServerDispatcher extends PDUVisitor {
	private RequestHandler connectionHandler;

	public SlimServerDispatcher(RequestHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}

	@Override
	public void processPollVotePDU(PollVotePDU pdu) 
	{
		System.out.println("### PollVotePDU received - poll " + pdu.GetTargetMessageId() + " exists ");
		connectionHandler.sendDirectedPDU(pdu, pdu.getOriginalUserId());
	}

	@Override
	public void processPublishPollPDU(PublishPollPDU pdu) {
		System.out.println("### PublishPollPDU received. tweetId: " + pdu.GetTweetId());
		connectionHandler.broadcastPDU(pdu);
	}

	@Override
	public void processRegisterPDU(RegisterPDU pdu) {
		System.out.println("[nearTweet Server] - Register Request: " + pdu.GetUserId());
		if(!connectionHandler.memory.VerifyIfUserExists(pdu.GetUserId())) { 
			connectionHandler.memory.InsertUser(pdu.GetUserId(), this.connectionHandler.connection);
			
			List<String> usersExceptMe = new ArrayList<String>(connectionHandler.memory.getUsers());
			usersExceptMe.remove(pdu.GetUserId());
			
			connectionHandler.sendDirectedPDU(pdu, usersExceptMe);
			connectionHandler.broadcastPDU(new GenericMessagePDU(pdu.GetUserId(), "User " + pdu.GetUserId() + " enter on the network", true));
			System.out.println("[nearTweet Server] - User "  + pdu.GetUserId() + " has been registered on the Server!");
		} else {
			GenericMessagePDU errorPdu = new GenericMessagePDU(pdu.GetUserId(), "The name choosed already exists!");
			connectionHandler.sendToConnectedUser(errorPdu);
			System.out.println("--- This user already exists!");
		}
	}

	@Override
	public void processReplyPDU(ReplyPDU pdu) {
		if(!pdu.getIsBroadcast()){
			connectionHandler.sendDirectedPDU(pdu, pdu.getTargetUserId());
		} else {
			connectionHandler.broadcastPDU(pdu);
		}
	}

	@Override
	public void processSpamVotePDU(SpamVotePDU pdu) {
		System.out.println("### SpamVotePDU received from userId: " + pdu.GetUserId());
		connectionHandler.broadcastPDU(pdu);
	}

	@Override
	public void processTweetPDU(TweetPDU pdu) {
		System.out.println("### TweetPDU received. tweetId: " + pdu.GetTweetId());
		connectionHandler.broadcastPDU(pdu); 
	}

	@Override
	public void processGenericMessagePDU(GenericMessagePDU pdu) {
		System.out.println("--- Generic message " + pdu.GetDescription());
	}
}
