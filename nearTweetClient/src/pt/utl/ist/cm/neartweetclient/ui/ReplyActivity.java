package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.services.ReplyService;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ReplyActivity extends Activity {

	public static final String TWEET_ID_EXTRA = "tweet_id";
	private EditText tweetEditText;
	private String tweetId;
	private CheckBox isBroadcastCheckBox;
	private TweetPDU originalTweetPdu;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_tweet);
        
        Button replyButton = (Button) findViewById(R.id.reply);
        isBroadcastCheckBox = (CheckBox) findViewById(R.id.isBroadcastCheckBox);
        tweetEditText = (EditText) findViewById(R.id.tweetReply);
        
        this.tweetId = getIntent().getExtras().getString(TWEET_ID_EXTRA);
		this.originalTweetPdu = (TweetPDU) MemCacheProvider.getTweet(tweetId);
        
		replyButton.setOnClickListener(submitReplyClickListener);
    }
    
	/*****************************************************************************
	 ****************************** Click Listeners ******************************
	 *****************************************************************************/
    
	private final OnClickListener submitReplyClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (getReplyText().length() > 0) {
				(new SubmitRetweetTask()).execute();
			} else {
				errorMessage();
			}
		}
	};
    
	/*****************************************************************************
	 ******************************** Async Tasks ********************************
	 *****************************************************************************/
	
	private class SubmitRetweetTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return (new ReplyService(originalTweetPdu.getId(), originalTweetPdu.getUserId(), getReplyText(), getIsBroadcast(), getApplicationContext())).execute();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				nextScreen();
			} else {
				errorMessage();
			}
		}
	}
    
    public void nextScreen() {
    	finish();
    }
    
    private boolean getIsBroadcast(){
    	return isBroadcastCheckBox.isChecked();
    }
    
	private String getReplyText() {
		return tweetEditText.getText().toString();
	}
    
    public void errorMessage() {
    	Toast.makeText(this,"You cannot tweet an empty message", Toast.LENGTH_SHORT).show();
    }

}
