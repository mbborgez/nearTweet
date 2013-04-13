package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;

public class PollContainer {

	private HashMap<String, List<Integer>> pollVotes = new HashMap<String, List<Integer>>();
	private String userId;
	
	public List<Integer> getVotesFor(String tweetId){
		return hasPoll(tweetId) ? pollVotes.get(tweetId) : null;
	}
	
	public Integer getVotesFor(String tweetId, int option){
		if(hasPoll(tweetId)){
			List<Integer> votes = getVotesFor(tweetId);
			return votes!=null ? votes.get(option) : null;
		} else {
			return null;
		}
	}
	
	private List<Integer> createNewPollVotes(int size) {
		ArrayList<Integer> newPollVotes = new ArrayList<Integer>(size);
		for(int i=0; i<size; ++i){
			newPollVotes.add(0);
		}
		return newPollVotes;
	}
	
	public void registerPoll(PublishPollPDU pdu){
		if(!hasPoll(pdu.GetTweetId())){
			Log.i("DEBUG", "registerPoll - " + pdu.GetText());
			pollVotes.put(pdu.GetTweetId(), createNewPollVotes(pdu.GetOptions().size()));
		}
	}

	public void registerPollVote(PollVotePDU pdu){
		if(hasPoll(pdu.GetTargetMessageId())){
			Log.i("DEBUG", "registerPollVote - " + pdu.GetOptionPosition());
			Integer numVotes = getVotesFor(pdu.GetTargetMessageId(), pdu.GetOptionPosition()) + 1;
			pollVotes.get(pdu.GetTargetMessageId()).set(pdu.GetOptionPosition(), numVotes);
		}
	}
	
	public boolean hasPoll(String tweetId){
		Log.i("DEBUG", "PollVotes - " + pollVotes);
		return pollVotes.containsKey(tweetId);
	}
	
}
