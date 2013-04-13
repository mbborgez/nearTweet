package main.java.com.cm.server.visitors;

import java.io.ObjectOutputStream;

import main.java.com.cm.server.handlers.RequestHandler;

import pt.utl.ist.cm.neartweetEntities.pdu.GenericMessagePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;


public class ServerDispatcher extends PDUVisitor {
	private RequestHandler connectionHandler;
	
	public ServerDispatcher(RequestHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}

	@Override
	public void processPollVotePDU(PollVotePDU pdu) 
	{
		System.out.println("### PollVotePDU received. Response selected: " + pdu.GetOptionPosition());
		
		if(connectionHandler.memory.VerifyIfUserExists(pdu.GetUserId()))
		{
			System.out.println("### PollVotePDU received - userExists " + pdu.GetUserId());
			if(connectionHandler.memory.VerifyIfPollExists(pdu.GetTargetMessageId()))
			{
				System.out.println("### PollVotePDU received - poll " + pdu.GetTargetMessageId() + " exists ");
				String pollOwnerUser = this.connectionHandler.memory.GetUserFromPollID(pdu.GetTargetMessageId());
				System.out.println("### PollVotePDU received - poll " + pdu.GetTargetMessageId() + " owner is " + pollOwnerUser);
				ObjectOutputStream userStream = connectionHandler.memory.GetUserStream(pollOwnerUser);
				System.out.println("### PollVotePDU received - poll " + pdu.GetTargetMessageId() + " userStream is " + userStream);
				if(userStream != null) {
					System.out.println("### PollVotePDU received - poll sending directPDU to user - start ");
					connectionHandler.sendDirectedPDU(pdu, userStream);
					System.out.println("### PollVotePDU received - poll sending directPDU to user - end");
				}
				else {
					connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Poll owner isn't registered anymore!"), this.connectionHandler.connection);
					System.out.println("--- Poll owner isn't registered!");
				}
			} else {
				connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Unrecognized target tweet ID!"), this.connectionHandler.connection);
				System.out.println("--- Unrecognized target tweet ID!");
			}
		} else {
			connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "You are not registered!"), this.connectionHandler.connection);
			System.out.println("--- User doesn't exists!");
		}
		
	}

	@Override
	public void processPublishPollPDU(PublishPollPDU pdu) {
		System.out.println("### PublishPollPDU received. tweetId: " + pdu.GetTweetId());
		
		if(connectionHandler.memory.VerifyIfUserExists(pdu.GetUserId())) {
			if(! connectionHandler.memory.VerifyIfPollExists(pdu.GetTweetId())) {
				connectionHandler.memory.InsertPoll(pdu.GetUserId(), pdu.GetTweetId(), pdu.GetText(), pdu.GetOptions());
				connectionHandler.broadcastPDU(pdu);
			} else {
				connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Tweet ID entered already exists!"), this.connectionHandler.connection);
				System.out.println("--- Tweet ID already exists!");
			}
		}
		else {
			connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "You are not registered!"), this.connectionHandler.connection);
			System.out.println("--- User doesn't exists!");
		}		
	}

	@Override
	public void processRegisterPDU(RegisterPDU pdu) {
		System.out.println("[nearTweet Server] - Register Request: " + pdu.GetUserId());
		if(!connectionHandler.memory.VerifyIfUserExists(pdu.GetUserId())) { 
			connectionHandler.memory.InsertUser(pdu.GetUserId(), this.connectionHandler.connection);
			connectionHandler.broadcastPDU(
					new GenericMessagePDU(pdu.GetUserId(), "User " + pdu.GetUserId() + " enter on the network", true));
			System.out.println("[nearTweet Server] - User "  + pdu.GetUserId() + " has been registered on the Server!");
		} else {
			connectionHandler.sendDirectedPDU(
					new GenericMessagePDU(pdu.GetUserId(), "The name choosed already exists!"), 
					this.connectionHandler.connection);
			System.out.println("--- This user already exists!");
		}
	}

	@Override
	public void processReplyPDU(ReplyPDU pdu) {
		System.out.println("### ReplyPDU received. Response: " + pdu.GetText());
		
		if(connectionHandler.memory.VerifyIfUserExists(pdu.GetUserId())) {
			if(connectionHandler.memory.VerifyIfTweetExists(pdu.GetTargetMessageId())) {
				String tweetOwnerUser = this.connectionHandler.memory.GetUserFromTweetID(pdu.GetTargetMessageId());
				ObjectOutputStream userStream = connectionHandler.memory.GetUserStream(tweetOwnerUser);
				if(userStream != null) {
					connectionHandler.sendDirectedPDU(pdu, userStream);
				} else {
					connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Tweet owner isn't registered!"), this.connectionHandler.connection);
					System.out.println("--- Tweet owner isn't registered!");
				}
			} else {
				connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Unrecognized target tweet ID!"), this.connectionHandler.connection);
				System.out.println("--- Unrecognized target tweet ID!");
			}	
		}
		else {
			connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "You are not registered!"), this.connectionHandler.connection);
			System.out.println("--- User doesn't exists!");
		}
	}

	@Override
	public void processSpamVotePDU(SpamVotePDU pdu) {
		System.out.println("### SpamVotePDU received from userId: " + pdu.GetUserId());
		
		if(connectionHandler.memory.VerifyIfUserExists(pdu.GetUserId())) {
			String tweetId = pdu.GetTargetMessageId();
			if(this.connectionHandler.memory.VerifyIfTweetExists(tweetId)) {
				String userId = this.connectionHandler.memory.GetUserFromTweetID(tweetId);
				if(this.connectionHandler.memory.UserSpamVote(userId) == 1) {
					connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "You will be removed! Spam votes on your tweets reached the limit!!!"), this.connectionHandler.memory.GetUserStream(userId));
					this.connectionHandler.memory.RemoveUser(this.connectionHandler.memory.GetUserStream(userId));
					System.out.println("--- Removed user: " + userId);
				}
				if(this.connectionHandler.memory.TweetSpamVote(tweetId) == 1) {
					connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Your tweet will be removed because Spam votes on it has reached the limit!!!"), this.connectionHandler.memory.GetUserStream(userId));
					this.connectionHandler.memory.RemoveTweet(tweetId);
					System.out.println("--- Removed tweet: " + tweetId);
				}
			} else {
				connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Unrecognized target tweet ID!"), this.connectionHandler.connection);
				System.out.println("--- Unrecognized target tweet ID!");
			}	
		} else {
			connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "You are not registered!"), this.connectionHandler.connection);
			System.out.println("--- User doesn't exists!");
		}
	}

	@Override
	public void processTweetPDU(TweetPDU pdu) {
		if(connectionHandler.memory.VerifyIfUserExists(pdu.GetUserId())) {
			if(! connectionHandler.memory.VerifyIfTweetExists(pdu.GetTweetId())) {
				System.out.println("### TweetPDU received. tweetId: " + pdu.GetTweetId());
				connectionHandler.memory.InsertTweet(pdu.GetUserId(), pdu.GetTweetId(), pdu.GetText(), pdu.GetMediaObject());
				connectionHandler.broadcastPDU(pdu); 
			} else {
				connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "Tweet ID entered already exists!"), this.connectionHandler.connection);
				System.out.println("--- Tweet ID already exists!");
			}
		} else {
			connectionHandler.sendDirectedPDU(new GenericMessagePDU(pdu.GetUserId(), "You are not registered!"), this.connectionHandler.connection);
			System.out.println("--- User doesn't exists!");
		}	
	}

	@Override
	public void processGenericMessagePDU(GenericMessagePDU pdu) {
		System.out.println("--- Generic message " + pdu.GetDescription());
	}
}
