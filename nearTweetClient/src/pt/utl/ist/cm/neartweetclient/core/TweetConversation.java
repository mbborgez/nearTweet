package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;

public class TweetConversation {
	private TweetPDU mainMessage;
	private List<ReplyPDU> conversation;
	private boolean hasUnreadMessages;
	
	public TweetConversation(TweetPDU mainMessage){
		this.mainMessage = mainMessage;
		this.conversation = new ArrayList<ReplyPDU>();
		this.hasUnreadMessages = false;
	}

	public void addMessage(ReplyPDU newMessage){
		conversation.add(newMessage);
		this.hasUnreadMessages = true;
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

	public TweetPDU getMainMessage() {
		return mainMessage;
	}

	public void setMainMessage(TweetPDU mainMessage) {
		this.mainMessage = mainMessage;
	}

	public List<ReplyPDU> getConversation() {
		return conversation;
	}

	public void setConversation(List<ReplyPDU> conversation) {
		this.conversation = conversation;
	}

	public boolean isHasUnreadMessages() {
		return hasUnreadMessages;
	}

	public void setHasUnreadMessages(boolean hasUnreadMessages) {
		this.hasUnreadMessages = hasUnreadMessages;
	}
}
