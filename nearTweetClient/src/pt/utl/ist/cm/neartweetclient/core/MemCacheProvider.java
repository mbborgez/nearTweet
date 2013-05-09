package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.core.PollConversation;
public class MemCacheProvider {

	private static String userName;
	
	private static HashMap<String, PDU> memcache = new HashMap<String, PDU>();
	
	private static HashMap<String, PollConversation> pollConversationContainer = new HashMap<String, PollConversation>();

	private static HashMap<String, TweetConversation> tweetConversationContainer = new HashMap<String, TweetConversation>();
	
	private static ArrayList<PDU> tweetsStream = new ArrayList<PDU>();
	
	public static void addTweet(String tweetID, PDU pdu) {
		memcache.put(tweetID, pdu);
		
		if(pdu instanceof TweetPDU){
			TweetPDU tweetPdu = (TweetPDU) pdu;
			tweetsStream.add(tweetPdu);
			tweetConversationContainer.put(tweetPdu.getId(), new TweetConversation(tweetPdu));
		} else if(pdu instanceof ReplyPDU){
			ReplyPDU replyPdu = (ReplyPDU) pdu;
			if(tweetConversationContainer.containsKey(replyPdu.getTargetMessageId())){
				tweetConversationContainer.get(replyPdu.getTargetMessageId()).addMessage(replyPdu);
			}
		} else if(pdu instanceof PollVotePDU){
			PollVotePDU pollVotePdu = (PollVotePDU) pdu;
			if(pollConversationContainer.containsKey(pollVotePdu.getTargetMessageId())){
				pollConversationContainer.get(pollVotePdu.getTargetMessageId()).addVote(pollVotePdu);
			}
		} else if(pdu instanceof PublishPollPDU){
			PublishPollPDU publishPollPdu = (PublishPollPDU) pdu;
			tweetsStream.add(publishPollPdu);
			pollConversationContainer.put(publishPollPdu.getId(), new PollConversation(publishPollPdu));
		}
	}
	
	public static boolean isMyPoll(String tweetId){
		if(pollConversationContainer.containsKey(tweetId)){
			PublishPollPDU publishPdu = (PublishPollPDU) getTweet(tweetId);
			return publishPdu.getUserId().equals(getUserName());
		}
		return false;
	}
	public static PDU getTweet(String tweetID) {
		return memcache.get(tweetID);
	}
	
	public static boolean hasMessage(String messageID){
	
		return memcache.containsKey(messageID);
	}
	
	public static Map<String, Integer> getVotesForPoll(String tweetId){
		return isMyPoll(tweetId) ? pollConversationContainer.get(tweetId).getVotes() : null;
	}
	
	public static PollConversation getPollConversation(String tweetId){
		return pollConversationContainer.containsKey(tweetId) ? pollConversationContainer.get(tweetId) : null;
	}
	
	public static TweetConversation getTweetConversation(String tweetId){
		return tweetConversationContainer.containsKey(tweetId) ? tweetConversationContainer.get(tweetId) : null;
	}
	
	public static ArrayList<PDU> getTweetsStream() {
		return tweetsStream;
	}
	
	public static boolean isEmpty() {
		return memcache.isEmpty();
	}
	
	public static void setUserName(String username){
		MemCacheProvider.userName = username;
	}
	public static String getUserName(){
		return MemCacheProvider.userName;
	}
}
