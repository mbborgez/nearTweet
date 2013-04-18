package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.services.ReplyService;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReplyActivity extends Activity {

	private EditText tweet;
	private String tweetId;
	private CheckBox isBroadcastCheckBox;
	private TweetPDU pdu;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_tweet);
        
        Button replyButton = (Button) findViewById(R.id.reply);
        tweet = (EditText) findViewById(R.id.tweetReply);
        
        Bundle extras = getIntent().getExtras();
        this.tweetId = extras.getString("tweet_id");
		this.pdu = (TweetPDU) MemCacheProvider.getTweet(tweetId);
        
		isBroadcastCheckBox = (CheckBox) findViewById(R.id.isBroadcastCheckBox);
		
        replyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (tweet.getText().toString().length() > 0) {
					submitTweet(tweet.getText().toString());
				} else {
					errorMessage();
				}
				
			}
		});
    }
    
    private void submitTweet(String text) {
    	/**
    	 * it should call the service layer
    	 */
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String username = settings.getString("username", null);
    	if (username == null) {
    		errorMessage();
    	} else {
    		ReplyService service = new ReplyService(username, this.pdu.GetTweetId(), text, this.pdu.GetUserId(), isBroadcast(), this.getApplicationContext());
    		try {
    			service.execute();
    			nextScreen();
    		} catch(Exception e) {
    			errorMessage();
    			finish();
    		}
    	}
    }
    
    public void nextScreen() {
    	finish();
    }
    
    private boolean isBroadcast(){
    	return isBroadcastCheckBox.isChecked();
    }
    
    public void errorMessage() {
    	Toast.makeText(this,"You cannot tweet an empty message", Toast.LENGTH_SHORT).show();
    }

}
