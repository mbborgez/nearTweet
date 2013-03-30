package pt.utl.ist.cm.neartweetclient.connectionTasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import pt.utl.ist.cm.neartweetEntities.PDU.GenericMessagePDU;
import pt.utl.ist.cm.neartweetEntities.PDU.PDU;
import pt.utl.ist.cm.neartweetEntities.PDU.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.PDU.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.PDU.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.PDU.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.PDU.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.PDU.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.PDU.TweetPDU;

public class ClientPDUVisitor extends PDUVisitor {

	private Context context;

	public ClientPDUVisitor(Context context){
		this.context = context;
	}

	@Override
	public void processPollVotePDU(PollVotePDU pdu) {
		// TODO
		Log.i(getClass().toString(), "Received a PollVotePDU  -  " + pdu);
		notifyPDUArrived(pdu, ConnectionStatus.POLL_VOTE_PDU_RECEIVED);
	}

	@Override
	public void processPublishPollPDU(PublishPollPDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a PublishPollPDU  -  " + pdu);
		notifyPDUArrived(pdu, ConnectionStatus.PUBLISH_POLL_PDU_RECEIVED);
	}

	@Override
	public void processRegisterPDU(RegisterPDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a RegisterPDU  -  " + pdu);
		notifyPDUArrived(pdu, ConnectionStatus.REGISTER_PDU_RECEIVED);
	}

	@Override
	public void processReplyPDU(ReplyPDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a ReplyPDU  -  " + pdu);
		notifyPDUArrived(pdu, ConnectionStatus.REPLY_PDU_RECEIVED);
	}

	@Override
	public void processSpamVotePDU(SpamVotePDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a SpamVotePDU  -  " + pdu);
		notifyPDUArrived(pdu, ConnectionStatus.SPAM_VOTE_PDU_RECEIVED);
	}

	@Override
	public void processTweetPDU(TweetPDU pdu) {
		// TODO
		Log.i(getClass().toString(),"Received a TweetPDU  -  " + pdu);
		notifyPDUArrived(pdu, ConnectionStatus.TWEET_PDU_RECEIVED);
	}

	@Override
	public void processGenericMessagePDU(GenericMessagePDU pdu) {
		// TODO Auto-generated method stub
		Log.i(getClass().toString(),"Received a GenericMessagePDU  -  " + pdu);
		notifyPDUArrived(pdu, ConnectionStatus.GENERIC_MESSAGE_PDU_RECEIVED);
	}

	private void notifyPDUArrived(PDU pdu, String pduCode){
		Intent intent = new Intent();
		intent.setAction(pduCode);
        // Puts the status into the Intent
		intent.putExtra(ConnectionStatus.MESSAGE_RECEIVED_DATA, pdu);
	    // Broadcasts the Intent to receivers in this app
	    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
}
