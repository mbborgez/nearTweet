package pt.utl.ist.cm.neartweetclient.sync;

import android.content.Context;
import android.content.Intent;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.utils.Actions;

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
		String tweetTemplate = "%s said: %s!";
		intent.setAction(Actions.NEW_TWEET);
		intent.putExtra("tweet", String.format(tweetTemplate, pdu.GetUserId(), pdu.GetText()));
		if (this.context != null) {
			this.context.sendBroadcast(intent);
		}
	}

}
