package pt.utl.ist.cm.neartweetclient;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetclient.core.PollContainer;

public class MemCacheProvider {

	private static HashMap<String, PDU> memcache = new HashMap<String, PDU>();
	
	private static PollContainer pollContainer = new PollContainer(); 
	
	public static void addTweet(String tweetID, PDU pdu) {
		memcache.put(tweetID, pdu);
		
		if(pdu instanceof PollVotePDU){
			pollContainer.registerPollVote((PollVotePDU) pdu);
		} else if(pdu instanceof PublishPollPDU){
			pollContainer.registerPoll((PublishPollPDU) pdu);
		}
		
	}
	
	public static PDU getTweet(String tweetID) {
		return memcache.get(tweetID);
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
}
