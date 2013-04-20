package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;

public class PollConversation {
	private PublishPollPDU mainMessage;
	private List<PollVotePDU> conversation;
	private Map<String, Integer> votes;
	private boolean hasUnreadMessages;

	public PollConversation(PublishPollPDU mainMessage){
		this.mainMessage = mainMessage;
		this.conversation = new ArrayList<PollVotePDU>();
		this.hasUnreadMessages = false;
		initVotes(mainMessage);
	}

	private void initVotes(PublishPollPDU mainMessage) {
		votes = new HashMap<String, Integer>();
		for(String voteOption : mainMessage.GetOptions()){
			votes.put(voteOption, 0);
		}
	}

	public void addVote(PollVotePDU newMessage){
		conversation.add(newMessage);
		String selectedVote = mainMessage.GetOptions().get(newMessage.GetOptionPosition());
		int numVotes = 0;
		if(votes.containsKey(selectedVote)){
			numVotes = votes.get(selectedVote);
			votes.remove(selectedVote);
		}
		hasUnreadMessages = true;
		votes.put(selectedVote, numVotes + 1);
	}

	public void removeMessage(String tweetId){
		if(!mainMessage.GetTweetId().equals(tweetId)){
			for(int i=0; i<conversation.size();++i){
				if(conversation.get(i).GetTweetId().equals(tweetId)){
					conversation.remove(i);
					return;
				}
			}
		}
	}

	public PublishPollPDU getMainMessage() {
		return mainMessage;
	}

	public void setMainMessage(PublishPollPDU mainMessage) {
		this.mainMessage = mainMessage;
	}

	public List<PollVotePDU> getConversation() {
		return conversation;
	}
	
	public Map<String,  Integer> getVotes(){
		return votes;
	}	

	public void setConversation(List<PollVotePDU> conversation) {
		this.conversation = conversation;
	}

	public boolean isHasUnreadMessages() {
		return hasUnreadMessages;
	}

	public void setHasUnreadMessages(boolean hasUnreadMessages) {
		this.hasUnreadMessages = hasUnreadMessages;
	}
}
