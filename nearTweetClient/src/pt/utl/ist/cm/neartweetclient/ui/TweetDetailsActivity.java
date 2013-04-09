package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.services.CreateSpamService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetDetailsActivity extends Activity {
	
	private TextView tweetDetailsTextView;
	private String tweetId;
	private ImageView tweetImage;
	private PDU pdu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_details_acitivity);
		
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
}
