package pt.utl.ist.cm.neartweetclient.sync;

import pt.utl.ist.cm.neartweetEntities.pdu.GenericMessagePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PDUVisitor;
import pt.utl.ist.cm.neartweetEntities.pdu.PollVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.RegisterPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.SpamVotePDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.core.ClientsManager;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.core.SpamManager;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

		if(!SpamManager.isBlocked(pdu.GetUserId())){
			MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);

			Intent intent = new Intent();
			intent.setAction(Actions.POLL_VOTE);
			intent.putExtra(Actions.POLL_VOTE_DATA, pdu.GetTweetId());
			if (this.context != null) {
				Log.i("DEBUG", "BroadCasting Message");
				context.sendBroadcast(intent);
			}
		}

	}

	@Override
	public void processPublishPollPDU(PublishPollPDU pdu) {
		if(!SpamManager.isBlocked(pdu.GetUserId())){
			Intent intent = new Intent();
			intent.setAction(Actions.BROADCAST_TWEET);
			intent.putExtra(Actions.TWEET_DATA, pdu.GetTweetId());
			if (this.context != null) {
				MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);
				this.context.sendBroadcast(intent);
			}
		}
	}

	@Override
	public void processRegisterPDU(RegisterPDU pdu) {
		ClientsManager.registerUser(pdu.GetUserId());
		Intent intent = new Intent();
		intent.setAction(Actions.BROADCAST_NEW_USER);
		intent.putExtra(Actions.NEW_USER_DATA, pdu.GetUserId());
		this.context.sendBroadcast(intent);
	}

	@Override
	public void processReplyPDU(ReplyPDU pdu) {
		if(!SpamManager.isBlocked(pdu.GetUserId())){
			Intent intent = new Intent();
			intent.setAction(Actions.BROADCAST_TWEET);
			intent.putExtra(Actions.TWEET_DATA, pdu.GetTweetId());
			if (this.context != null) {
				MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);
				this.context.sendBroadcast(intent);
			}
		}

	}

	@Override
	public void processSpamVotePDU(SpamVotePDU pdu) {
		if(!SpamManager.isBlocked(pdu.GetUserId())){
			Log.i("DEBUG", "New SPAM VOTE FOR USER " + pdu.GetUserId());
			SpamManager.registerSpamVote(pdu);
			if(SpamManager.isBlocked(pdu.getTargetUserId())) {
				Intent intent = new Intent();
				intent.setAction(Actions.BROADCAST_SPAMMER_BLOCKED);
				intent.putExtra(Actions.SPAMMER_ID_DATA, pdu.getTargetUserId());
				this.context.sendBroadcast(intent);
			}
		}
	}

	@Override
	public void processTweetPDU(TweetPDU pdu) {
		if(!SpamManager.isBlocked(pdu.GetUserId())){
			Intent intent = new Intent();
			intent.setAction(Actions.BROADCAST_TWEET);
			intent.putExtra(Actions.TWEET_DATA, pdu.GetTweetId());
			if (this.context != null) {
				MemCacheProvider.addTweet(pdu.GetTweetId(), pdu);
				this.context.sendBroadcast(intent);
			}
		}
	}

}
