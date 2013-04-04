package pt.utl.ist.cm.neartweetclient.connectionTasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;

public class PDUHandler extends PDUVisitor {
	
	private Context context;
	public PDUHandler(Context c) {
		this.context = c;
	}
	@Override
	public void processPollVotePDU(PollVotePDU pdu) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processPublishPollPDU(PublishPollPDU pdu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processRegisterPDU(RegisterPDU pdu) {
		// NOT NEEDED
		
	}

	@Override
	public void processReplyPDU(ReplyPDU pdu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processSpamVotePDU(SpamVotePDU pdu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processTweetPDU(TweetPDU pdu) {
		Intent intent = new Intent();
		intent.setAction("new_tweets");
		intent.putExtra("tweet", pdu.GetText());
		System.out.println("Broadcasting");
	    LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
	}

}
