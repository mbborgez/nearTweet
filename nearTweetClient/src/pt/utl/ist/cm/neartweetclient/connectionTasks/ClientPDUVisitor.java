package pt.utl.ist.cm.neartweetclient.connectionTasks;

import pt.utl.ist.cm.neartweetEntities.pdu.GenericMessagePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.utils.Constants;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ClientPDUVisitor extends PDUVisitor {

	private Context context;

	public ClientPDUVisitor(Context context){
		this.context = context;
	}

	@Override
	public void processPollVotePDU(PollVotePDU pdu) {
		// TODO
		Log.i(getClass().toString(), "Received a PollVotePDU  -  " + pdu);
		savePDU(pdu);
		notifyPDUArrived(pdu, Constants.POLL_VOTE_PDU_RECEIVED);
	}

	@Override
	public void processPublishPollPDU(PublishPollPDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a PublishPollPDU  -  " + pdu);
		savePDU(pdu);
		notifyPDUArrived(pdu, Constants.PUBLISH_POLL_PDU_RECEIVED);
	}

	@Override
	public void processRegisterPDU(RegisterPDU pdu) {
		// Does not make sence in the case of the android client.
	}

	@Override
	public void processReplyPDU(ReplyPDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a ReplyPDU  -  " + pdu);
		savePDU(pdu);
		notifyPDUArrived(pdu, Constants.REPLY_PDU_RECEIVED);
	}

	@Override
	public void processSpamVotePDU(SpamVotePDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a SpamVotePDU  -  " + pdu);
		savePDU(pdu);
		notifyPDUArrived(pdu, Constants.SPAM_VOTE_PDU_RECEIVED);
	}

	@Override
	public void processTweetPDU(TweetPDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a TweetPDU  -  " + pdu);
		savePDU(pdu);
		notifyPDUArrived(pdu, Constants.TWEET_PDU_RECEIVED);
	}

	@Override
	public void processGenericMessagePDU(GenericMessagePDU pdu) {
		Log.i(getClass().toString(),"Received a GenericMessagePDU  -  " + pdu);
		savePDU(pdu);
		notifyPDUArrived(pdu, Constants.GENERIC_MESSAGE_PDU_RECEIVED);
	}

	private void savePDU(PDU pdu) {
		InboxSingleton.getInstance().addReceivedMessage(pdu);
	}

	private void notifyPDUArrived(PDU pdu, String pduCode){
		Intent intent = new Intent();
		intent.setAction(pduCode);
        // Puts the status into the Intent
		intent.putExtra(Constants.MESSAGE_RECEIVED_DATA, pdu);
	    // Broadcasts the Intent to receivers in this app
	    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
}
