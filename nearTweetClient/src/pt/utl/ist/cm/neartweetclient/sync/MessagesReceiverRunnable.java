package pt.utl.ist.cm.neartweetclient.sync;

import pt.utl.ist.cm.neartweetEntities.pdu.*;
import android.content.Context;
import android.util.Log;
import pt.utl.ist.cm.neartweetclient.core.*;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import pt.utl.ist.cm.neartweetclient.utils.UiMessages;

public class MessagesReceiverRunnable implements Runnable {

	private Context context;
	private Peer peer;
	private PDUVisitor visitor;
	String clientName = null;

	public MessagesReceiverRunnable(Context context, Peer peer){
		this.context = context;
		this.peer = peer;
		this.visitor = new PDUHandler(this.context);
	}

	@Override
	public void run() {
		PDU receivedMessage;

		try {
			peer.sendPDU(new RegisterPDU(Actions.createUniqueID(context), Actions.getUserId(context)));
			while (true) {
				Log.i(UiMessages.NEARTWEET_TAG, "waiting receive");
				receivedMessage = peer.receivePDU();
				Log.i(UiMessages.NEARTWEET_TAG, "received a new pdu: " + receivedMessage);
				if (receivedMessage == null) {
					Log.i("CHAT-RECEIVE", "start disconnecting..");
					// end of connection
					break;
				}
//				if (clientName == null) { 
//					Log.i(UiMessages.NEARTWEET_TAG, "registering new peer: " + receivedMessage.getUserId());
//					// the first message is a connect message
//					clientName = receivedMessage.getUserId();
					if(!Connection.getInstance().hasPeer(receivedMessage.getUserId())) {
						peer.setDeviceName(receivedMessage.getUserId());
						Connection.getInstance().addPeer(peer);
					}
//				} else {
					checkReceivedMessage(receivedMessage);
//				}
			}
		} catch (Exception e) {
			Log.d("Error reading socket:", e.getMessage());
		}

		Connection.getInstance().removePeer(clientName);
	}

	protected void checkReceivedMessage(PDU message) {
		if(MemCacheProvider.hasMessage(message.getId())){
			Log.i("NEARTWEET-RECEIVE", "I do not have this message -> i will broadcast it");
			message.accept(visitor);
			Connection.getInstance().broadcastPDU(message);
		} else {
			Log.i(UiMessages.NEARTWEET_TAG, "i wallready have this message : " + message.getId());
		}
	}
}
