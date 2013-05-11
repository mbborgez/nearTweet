package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;

public class TweetConversation {
	private TweetPDU mainMessage;
	private ArrayList<PDU> conversation;
	private ArrayList<PDU> unreadMessages;
	
	public TweetConversation(TweetPDU mainMessage){
		this.mainMessage = mainMessage;
		this.conversation = new ArrayList<PDU>();
		this.unreadMessages = new ArrayList<PDU>();
	}

	public void addMessage(ReplyPDU newMessage){
		if(!hasMessage(newMessage.getId())){
			conversation.add(newMessage);
			unreadMessages.add(newMessage);
		}
	}

	private boolean hasMessage(String id) {
		for(PDU pdu : conversation){
			if(pdu.getId().equals(id)){
				return true;
			}
		}
		return false;
	}

	public void removeMessage(String tweetId){
		if(!mainMessage.getId().equals(tweetId)){
			for(int i=0; i<conversation.size();++i){
				if(conversation.get(i).getId().equals(tweetId)){
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

	public ArrayList<PDU> getConversation() {
		return conversation;
	}

	public void setConversation(ArrayList<PDU> conversation) {
		this.conversation = conversation;
	}

	public boolean isHasUnreadMessages() {
		return !unreadMessages.isEmpty();
	}

	public ArrayList<PDU> readInboxMessages() {
		ArrayList<PDU> messages = new ArrayList<PDU>(unreadMessages);
		unreadMessages = new ArrayList<PDU>();
		return messages;
	}

}
