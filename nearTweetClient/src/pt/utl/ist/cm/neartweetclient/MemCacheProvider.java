package pt.utl.ist.cm.neartweetclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.core.TweetConversation;
import pt.utl.ist.cm.neartweetclient.core.TweetConversation;
public class MemCacheProvider {

	private static String userName;
	
	private static HashMap<String, PDU> memcache = new HashMap<String, PDU>();
	
	private static HashMap<String, PollConversation> pollConversationContainer = new HashMap<String, PollConversation>();

	private static HashMap<String, TweetConversation> tweetConversationContainer = new HashMap<String, TweetConversation>();
	
	public static void addTweet(String tweetID, PDU pdu) {
		memcache.put(tweetID, pdu);
		
		if(pdu instanceof TweetPDU){
			TweetPDU tweetPdu = (TweetPDU) pdu;
			tweetConversationContainer.put(tweetPdu.GetTweetId(), new TweetConversation(tweetPdu));
		} else if(pdu instanceof ReplyPDU){
			ReplyPDU replyPdu = (ReplyPDU) pdu;
			if(tweetConversationContainer.containsKey(replyPdu.GetTargetMessageId())){
				tweetConversationContainer.get(replyPdu.GetTargetMessageId()).addMessage(replyPdu);
			}
		} else if(pdu instanceof PollVotePDU){
			PollVotePDU pollVotePdu = (PollVotePDU) pdu;
			if(pollConversationContainer.containsKey(pollVotePdu.GetTargetMessageId())){
				pollConversationContainer.get(pollVotePdu.GetTargetMessageId()).addVote(pollVotePdu);
			}
		} else if(pdu instanceof PublishPollPDU){
			PublishPollPDU publishPollPdu = (PublishPollPDU) pdu;
			pollConversationContainer.put(publishPollPdu.GetTweetId(), new PollConversation(publishPollPdu));
		}
	}
	
	public static boolean isMyPoll(String tweetId){
		if(pollConversationContainer.containsKey(tweetId)){
			PublishPollPDU publishPdu = (PublishPollPDU) getTweet(tweetId);
			return publishPdu.GetUserId().equals(getUserName());
		}
		return false;
	}
	public static PDU getTweet(String tweetID) {
		return memcache.get(tweetID);
	}
	
	public static Map<String, Integer> getVotesForPoll(String tweetId){
		return isMyPoll(tweetId) ? pollConversationContainer.get(tweetId).getVotes() : null;
	}
	
	public static TweetConversation getTweetConversation(String tweetId){
		return tweetConversationContainer.containsKey(tweetId) ? tweetConversationContainer.get(tweetId) : null;
	}
	
	public static ArrayList<PDU> toArrayList() {
		ArrayList<PDU> list = new ArrayList<PDU>();
		for(PDU pdu : memcache.values()) {
			list.add(0, pdu);
		}
		return list;
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
