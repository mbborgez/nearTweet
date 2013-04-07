package pt.utl.ist.cm.neartweetclient;

import java.util.ArrayList;
import java.util.HashMap;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class MemCacheProvider {

	private static HashMap<String, PDU> memcache = new HashMap<String, PDU>();
	
	public static void addTweet(String tweetID, PDU pdu) {
		memcache.put(tweetID, pdu);
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
