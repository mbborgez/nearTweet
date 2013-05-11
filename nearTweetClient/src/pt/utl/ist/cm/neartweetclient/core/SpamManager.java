package pt.utl.ist.cm.neartweetclient.core;

import java.util.HashMap;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;

public class SpamManager {

	private static final int MAX_NUM_SPAM_VOTES = 2;

	private static HashMap<String, Integer> spamVotes = new HashMap<String, Integer>();
	
	public static void registerSpamVote(SpamVotePDU pdu) {
		spamVotes.put(pdu.getTargetUserId(), numVotes(pdu.getTargetUserId())+1);
	}
	
	public static boolean isBlocked(String userId) {
		return numVotes(userId) > MAX_NUM_SPAM_VOTES;
	}

	private static int numVotes(String userId) {
		return spamVotes.containsKey(userId) ? spamVotes.get(userId) : 0;
	}
	
	public static void unblockUser(String userId) {
		if(spamVotes.containsKey(userId)) {
			spamVotes.remove(userId);
		}
	}

}
