package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RetweetActivity extends ListActivity {
	
	public static final String TWEET_ID_EXTRA = "tweet_item";
	private TextView tweetDetailsTextView;
	private ImageView tweetImage;

	private Button retweetButton;

	private String tweetId;
	private TweetPDU tweetPdu;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retweet);
		
		tweetDetailsTextView = (TextView) findViewById(R.id.retweet_details_text);
		tweetImage = (ImageView) findViewById(R.id.retweet_details_image);
		retweetButton = (Button) findViewById(R.id.submitRetweetButton);

		retweetButton.setOnClickListener(retweetClickListener);

		tweetId = getIntent().getStringExtra(TWEET_ID_EXTRA);
		tweetPdu = (TweetPDU) MemCacheProvider.getTweet(tweetId);

		populateTweetDetails();
	}
	
	private void populateTweetDetails() {
		tweetDetailsTextView.setText(tweetPdu.GetText());
		if(tweetPdu.GetMediaObject()!=null && tweetPdu.GetMediaObject().length>0){
			Bitmap bitmap = BitmapFactory.decodeByteArray(tweetPdu.GetMediaObject(), 0, tweetPdu.GetMediaObject().length);
			tweetImage.setImageBitmap(bitmap);
			tweetImage.setVisibility(View.VISIBLE);
		} else {
			tweetImage.setVisibility(View.GONE);
		}
	}
	
	private final OnClickListener retweetClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			System.out.println("I Will implement Tweet feature tomorrow :)!"); 
		}
	};
}
