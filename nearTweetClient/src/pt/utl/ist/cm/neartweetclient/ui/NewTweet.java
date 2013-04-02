package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.services.CreateTweetService;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewTweet extends Activity {

	EditText tweet;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tweet);
        
        Button cancelButton = (Button) findViewById(R.id.cancelTweet);
        Button submitButton = (Button) findViewById(R.id.submitTweet);
        tweet = (EditText) findViewById(R.id.tweetText);
        cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        submitButton.setOnClickListener(new OnClickListener() {
			
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
    		CreateTweetService service = new CreateTweetService(username, text, this);
    		try {
    			service.execute("");
    		} catch(Exception e) {
    			errorMessage();
    			finish();
    		}
    	}
    }
    
    public void nextScreen() {
    	finish();
    }
    
    
    public void errorMessage() {
    	Toast.makeText(this,"You cannot tweet an empty message", Toast.LENGTH_SHORT).show();
    }

}
