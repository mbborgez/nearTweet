package pt.utl.ist.cm.neartweetclient.sync;

import pt.utl.ist.cm.neartweetEntities.pdu.*;
import android.content.Context;
import android.util.Log;
import pt.utl.ist.cm.neartweetclient.core.*;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;

public class MessagesReceiverRunnable implements Runnable {
	//Just To check!!!
	private Context context;
	private Peer peer;
	private PDUVisitor visitor;

	public MessagesReceiverRunnable(Context context, Peer peer){
		Log.i(UiMessages.NEARTWEET_TAG, "MessagesReceiverRunnable<Init> peer: " + peer); 
		this.context = context;
		this.peer = peer;
		this.visitor = new PDUHandler(this.context);
	}

	@Override
	public void run() {
		PDU receivedMessage;

		try {
			PDU registerPDU = new RegisterPDU(Actions.createUniqueID(context), Actions.getUserId(context));
			MemCacheProvider.addTweet(registerPDU.getId(), registerPDU);
			Log.i(UiMessages.NEARTWEET_TAG, "sending register pdu: " + registerPDU + " to peer " + peer);
			if(peer.sendPDU(registerPDU)) {
				Log.i(UiMessages.NEARTWEET_TAG, "registerPDU sent with success");;
				//				MemCacheProvider.addTweet(registerPDU.getId(), registerPDU);
				while (true) {
					Log.i(UiMessages.NEARTWEET_TAG, "waiting receive from peer " + peer);
					receivedMessage = peer.receivePDU();
					Log.i(UiMessages.NEARTWEET_TAG, "received a new pdu: " + receivedMessage);
					if (receivedMessage == null) {
						Log.i("CHAT-RECEIVE", "start disconnecting..");
						// end of connection
						break;
					}
					checkReceivedMessage(receivedMessage, peer);
				}
			}
		} catch (Exception e) {
			Log.d("Error reading socket:", e.getMessage());
		}
		Log.i(UiMessages.NEARTWEET_TAG, "The connection has ended with an error - i will remove the peer " + peer + "START");
		Connection.getInstance().removePeer(peer.getDeviceName());
		Log.i(UiMessages.NEARTWEET_TAG, "The connection has ended with an error - i will remove the peer " + peer + "DONE");

	}
	
	protected void checkReceivedMessage(PDU message, Peer peer) {
		Log.i(UiMessages.NEARTWEET_TAG, "checkReceivedMessage: [message: " + message + ", peer: " + peer);
		if(!MemCacheProvider.hasMessage(message.getId())) {
			Log.i("NEARTWEET-RECEIVE", "I do not have this message");
			if(!Connection.getInstance().hasPeer(message.getUserId())) {
				Log.i("NEARTWEET-RECEIVE", "The device is not registered yet");
				if(peer.getDeviceName()==null) {
					Log.i("NEARTWEET-RECEIVE", "The peer has not a name yet: i will name it " + message.getUserId());
					peer.setDeviceName(message.getUserId());
				}
				Connection.getInstance().addPeer(peer);
				Log.i("NEARTWEET-RECEIVE", "The peer is now registered " + peer);
			}
			message.accept(visitor);
		} else {
			Log.i(UiMessages.NEARTWEET_TAG, "i wallready have this message : " + message.getId());
		}
	}
}
