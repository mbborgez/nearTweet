package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;
import android.util.Log;
public class MemCacheProvider {

	private static String userName;
	
	private static HashMap<String, PDU> memcache = new HashMap<String, PDU>();
	
	private static HashMap<String, PollConversation> pollConversationContainer = new HashMap<String, PollConversation>();

	private static HashMap<String, TweetConversation> tweetConversationContainer = new HashMap<String, TweetConversation>();
	
	private static ArrayList<PDU> inboxMessages = new ArrayList<PDU>();

	private static boolean isGroupOwner;
	

	public static void addTweet(String tweetID, PDU pdu) {
		Log.i(UiMessages.NEARTWEET_TAG, "Memory - registering tweet " + tweetID);
		memcache.put(tweetID, pdu);
	}
	
	public static synchronized void registerPollConversation(PollVotePDU pollVotePdu) {
		if(pollConversationContainer.containsKey(pollVotePdu.getTargetMessageId())){
			pollConversationContainer.get(pollVotePdu.getTargetMessageId()).addVote(pollVotePdu);
		}
	}
	
	public static synchronized void registerPollConversation(PublishPollPDU publishPollPdu) {
		pollConversationContainer.put(publishPollPdu.getId(), new PollConversation(publishPollPdu));
	}
	
	public static synchronized void registerTweetConversation(TweetPDU tweetPdu) {
		tweetConversationContainer.put(tweetPdu.getId(), new TweetConversation(tweetPdu));
	}
	
	public static synchronized void registerTweetConversation(ReplyPDU replyPdu) {
		if(tweetConversationContainer.containsKey(replyPdu.getTargetMessageId())){
			tweetConversationContainer.get(replyPdu.getTargetMessageId()).addMessage(replyPdu);
		}
	}
	
	public static synchronized boolean isMyPoll(String tweetId){
		if(pollConversationContainer.containsKey(tweetId)){
			PublishPollPDU publishPdu = (PublishPollPDU) getTweet(tweetId);
			return publishPdu.getUserId().equals(getUserName());
		}
		return false;
	}
	
	public static synchronized ArrayList<PDU> readInboxMessages() {
		ArrayList<PDU> messages = new ArrayList<PDU>(inboxMessages);
		inboxMessages = new ArrayList<PDU>();
		return messages;
	}
	
	public static synchronized void registerInboxMessage(PDU message) {
		inboxMessages.add(message);
	}
	
	public static synchronized PDU getTweet(String tweetID) {
		return memcache.get(tweetID);
	}
	
	public static boolean hasMessage(String messageID){
	
		return memcache.containsKey(messageID);
	}
	
	public static synchronized Map<String, Integer> getVotesForPoll(String tweetId){
		return isMyPoll(tweetId) ? pollConversationContainer.get(tweetId).getVotes() : null;
	}
	
	public static synchronized PollConversation getPollConversation(String tweetId){
		return pollConversationContainer.containsKey(tweetId) ? pollConversationContainer.get(tweetId) : null;
	}
	
	public static synchronized TweetConversation getTweetConversation(String tweetId){
		return tweetConversationContainer.containsKey(tweetId) ? tweetConversationContainer.get(tweetId) : null;
	}
	
	public static synchronized boolean isEmpty() {
		return memcache.isEmpty();
	}
	
	public static void setUserName(String username){
		MemCacheProvider.userName = username;
	}
	public static String getUserName(){
		return MemCacheProvider.userName;
	}

	public static void setIsGroupOwner(boolean isGroupOwner) {
		MemCacheProvider.isGroupOwner = isGroupOwner;
	}

	public static boolean isGroupOwner() {
		return isGroupOwner;
	}

	public static void setGroupOwner(boolean isGroupOwner) {
		MemCacheProvider.isGroupOwner = isGroupOwner;
	}
	
}
