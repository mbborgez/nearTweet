package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.TweetConversation;
import pt.utl.ist.cm.neartweetclient.core.TweetConversationAdapter;
import pt.utl.ist.cm.neartweetclient.services.CreateSpamService;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetDetailsActivity extends ListActivity {
	
	private TextView tweetDetailsTextView;
	private String tweetId;
	private ImageView tweetImage;
	
	private Button repplyButton;
	private Button spamButton;
	
	private PDU pdu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_details);
		tweetId = getIntent().getStringExtra("tweet_item");
		pdu = MemCacheProvider.getTweet(tweetId);
		if(tweetId.length()>0){
			tweetDetailsTextView = (TextView) findViewById(R.id.tweet_details_text);
			tweetImage = (ImageView) findViewById(R.id.tweet_details_image);
			
			if (pdu instanceof TweetPDU) {
				TweetPDU currentPDU = (TweetPDU) pdu;
				tweetDetailsTextView.setText(currentPDU.GetText());
				if(currentPDU.GetMediaObject()!=null && currentPDU.GetMediaObject().length>0){
					Bitmap bitmap = BitmapFactory.decodeByteArray(currentPDU.GetMediaObject(), 0, currentPDU.GetMediaObject().length);
					tweetImage.setImageBitmap(bitmap);
					tweetImage.setVisibility(View.VISIBLE);
				} else {
					tweetImage.setVisibility(View.GONE);
				}
				
			} else if(pdu instanceof PublishPollPDU) {
				tweetDetailsTextView.setText(((PublishPollPDU) pdu).GetText());
			} 
		}
		
		repplyButton = (Button) findViewById(R.id.submitReplyTweetButton);
		spamButton = (Button) findViewById(R.id.markSpamTweetButton);

		repplyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				replyTweetScreen();
			}
		});
		
		spamButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				markTweetAsSpam();
			}
		});
		
		updateTweetConversation();
	    
		// Put whatever message you want to receive as the action
		IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.BROADCAST_TWEET);
        this.registerReceiver(this.repliesReceiver,iff);
	    
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.reply_tweet_button:
	            replyTweetScreen();
	            return true;
	        case R.id.spam_button:
	        	markTweetAsSpam();
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_details_acitivity, menu);
		return true;
	}
	
	private void replyTweetScreen() {
		Intent intent = new Intent(this, ReplyActivity.class);
		intent.putExtra("tweet_id", this.tweetId);
		startActivity(intent);
	}
	
	private void markTweetAsSpam() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		String user = settings.getString("username", null);
		new CreateSpamService(user,this.tweetId).execute();
		finish();
	}
	
	private BroadcastReceiver repliesReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("DEGUB", "RECEIVED SOMETHING");
			if (intent.getAction().equals(Actions.BROADCAST_TWEET)) {
					updateTweetConversation();
			}
		}
    };
    
	private void updateTweetConversation() {
		TweetConversation tweetConversation = MemCacheProvider.getTweetConversation(tweetId);
		TweetConversationAdapter tweetConversationAdapter = 
				new TweetConversationAdapter(this.getApplicationContext(), R.layout.tweet_layout, tweetConversation);
	    setListAdapter(tweetConversationAdapter);
	}

}
