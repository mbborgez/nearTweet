package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import android.util.Log;

public class SpamManager {

	private static final int MAX_NUM_SPAM_VOTES = 2;

	private static HashMap<String, List<SpamVotePDU>> spamVotes = new HashMap<String, List<SpamVotePDU>>();
	
	private static List<String> blockedUsers = new ArrayList<String>();
	
	public static void registerSpamVote(SpamVotePDU pdu) {
		Log.i("DEBUG", "registerSpamVote - [targetMessageId: " + pdu.getTargetMessageId() + ", userTargetId: " + pdu.getTargetUserId());
		List<SpamVotePDU> userSpamVotes = spamVotes.containsKey(pdu.getTargetUserId()) ? 
											spamVotes.get(pdu.getTargetUserId()) : new ArrayList<SpamVotePDU>();
		userSpamVotes.add(pdu);
		spamVotes.put(pdu.getTargetUserId(), userSpamVotes);
		
		if(spamVotes.get(pdu.getTargetUserId()).size()>=MAX_NUM_SPAM_VOTES) {
			blockUser(pdu.getTargetUserId());
		}
	}
	
	public static boolean isBlocked(String userId) {
		return blockedUsers.contains(userId);
	}
	
	public static void blockUser(String userId) {
		Log.i("DEBUG", "SpamManager - The user " + userId + " is now blocked ");
		blockedUsers.add(userId);
	}
	
	public static void unblockUser(String userId) {
		if(blockedUsers.contains(userId)) {
			spamVotes.remove(userId);
			blockedUsers.remove(userId);
		}
	}

}
