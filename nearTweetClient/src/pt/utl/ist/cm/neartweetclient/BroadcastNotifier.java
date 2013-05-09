package pt.utl.ist.cm.neartweetclient;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastNotifier {
	
	private LocalBroadcastManager mBroadcaster;

    /**
     * Creates a BroadcastNotifier containing an instance of LocalBroadcastManager.
     * LocalBroadcastManager is more efficient than BroadcastManager; because it only
     * broadcasts to components within the app, it doesn't have to do parceling and so forth.
     *
     * @param context a Context from which to get the LocalBroadcastManager
     */
    public BroadcastNotifier(Context context) {

        // Gets an instance of the support library local broadcastmanager
        mBroadcaster = LocalBroadcastManager.getInstance(context);

    }
    
    /**
    *
    * Uses LocalBroadcastManager to send an {@TweetPDU} containing {@string Message}.
    *
    * @param status {@pdu TweetPDU} denoting a pdu received
    */
    public void broadcastTweetMessage(TweetPDU pdu) {
    	String tweetTemplate = "%s said: %s!";
    	
    	Intent localIntent = new Intent();
    	
    	// The Intent contains the custom broadcast action for this app
        localIntent.setAction(Actions.BROADCAST_TWEET);
		localIntent.putExtra(Actions.TWEET_DATA, String.format(tweetTemplate, pdu.getUserId(), pdu.GetText()));
		
        // Broadcasts the Intent
        mBroadcaster.sendBroadcast(localIntent);
    }

}
