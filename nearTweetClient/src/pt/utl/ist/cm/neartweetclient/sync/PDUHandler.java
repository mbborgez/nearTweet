package pt.utl.ist.cm.neartweetclient.sync;

import pt.utl.ist.cm.neartweetEntities.pdu.GenericMessagePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PDUHandler extends PDUVisitor {
	
	private Context context;
	public PDUHandler(Context c) {
		this.context = c;
	}
	
	public void processGenericMessagePDU(GenericMessagePDU pdu) {
		Intent intent = new Intent();
		intent.setAction(Actions.REGISTER_CONFIRMATION);
		intent.putExtra(Actions.SUCCESS_LOGIN, pdu.GetResponse());
		if (this.context != null) {
			Log.i("DEBUG", "BroadCasting Message");
			context.sendBroadcast(intent);
		} else {
			Log.i("DEGUB", "CONTEXT WAS EMPTY ON GENERIC MESSAGE PDU");
		}
	}
	
	@Override
	public void processPollVotePDU(PollVotePDU pdu) {
		Log.i("DEBUG", "received a pollVote [ userId: " + pdu.GetUserId() + ", option: " + pdu.GetOptionPosition() + "]" );
		
		MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);
			
		Intent intent = new Intent();
		intent.setAction(Actions.POLL_VOTE);
		intent.putExtra(Actions.POLL_VOTE_DATA, pdu.GetTweetId());
		if (this.context != null) {
			Log.i("DEBUG", "BroadCasting Message");
			context.sendBroadcast(intent);
		}
	
	}

	@Override
	public void processPublishPollPDU(PublishPollPDU pdu) {
		Intent intent = new Intent();
		intent.setAction(Actions.BROADCAST_TWEET);
		intent.putExtra(Actions.TWEET_DATA, pdu.GetTweetId());
		if (this.context != null) {
			MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);
			this.context.sendBroadcast(intent);
		}
	}

	@Override
	public void processRegisterPDU(RegisterPDU pdu) {
		//do nothing
	}

	@Override
	public void processReplyPDU(ReplyPDU pdu) {
		Intent intent = new Intent();
		intent.setAction(Actions.BROADCAST_TWEET);
		intent.putExtra(Actions.TWEET_DATA, pdu.GetTweetId());
		if (this.context != null) {
			MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);
			this.context.sendBroadcast(intent);
		}
		
	}

	@Override
	public void processSpamVotePDU(SpamVotePDU pdu) {
		Log.i("DEBUG", "New SPAM");
		Toast.makeText(this.context, pdu.GetTargetMessageId(), Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void processTweetPDU(TweetPDU pdu) {
		Intent intent = new Intent();
		intent.setAction(Actions.BROADCAST_TWEET);
		intent.putExtra(Actions.TWEET_DATA, pdu.GetTweetId());
		if (this.context != null) {
			MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);
			this.context.sendBroadcast(intent);
		}
	}

}
