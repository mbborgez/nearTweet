package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import pt.utl.ist.cm.neartweetclient.services.RetweetService;
import pt.utl.ist.cm.neartweetclient.sync.HTTPConnection;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RetweetActivity extends ListActivity {
	
	public static final String TWEET_ID_EXTRA = "tweet_item";
	private TextView tweetDetailsTextView;
	private ImageView tweetImage;

	private Button retweetButton;

	private String tweetId;
	private TweetPDU tweetPdu;
	private RetweetService service;
	ProgressDialog progressDialog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retweet);
		
		String userId = Actions.getUserId(getApplicationContext());
		service = new RetweetService(userId,getApplicationContext());
		
		HTTPConnection connection = new HTTPConnection(getApplicationContext());
		if (!connection.isConnectingToInternet()) {
			Toast.makeText(getApplicationContext(), 
					"There are no internet connection. We cannot Proceed!",
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		// first check if the user have already token to log on
		
		if(service.userAlreadyLoggedIn()) {
			System.out.println("User logged in");
			tweetId = getIntent().getStringExtra(TWEET_ID_EXTRA);
			populateView();
		} else if (service.intentFromOAuthCallback(getIntent())) {
			System.out.println("Handle Callback");
			service.handleOAuthCallback(getIntent().getData());
			populateView();
		} else {
			System.out.println("Start Request OAuth");
			try {
				service.setRequestToken();
				service.cacheTweetId(getIntent().getStringExtra(TWEET_ID_EXTRA));
				startActivity(new Intent(Intent.ACTION_VIEW, 
						Uri.parse(service.getAuthenticationUrl())));
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), 
						"Something went wrond during the connection", 
						Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}
	
	private void populateView() {
		tweetDetailsTextView = (TextView) findViewById(R.id.retweet_details_text);
		tweetImage = (ImageView) findViewById(R.id.retweet_details_image);
		retweetButton = (Button) findViewById(R.id.submitRetweetButton);
		retweetButton.setOnClickListener(retweetClickListener);
		if (tweetId == null) {
			tweetId = service.takeFromCacheStoredTweet();
		}
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
			String message = tweetPdu.GetText();
			new SendMessageToTwitter().execute(message);
		}
	};
	
	class SendMessageToTwitter extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(RetweetActivity.this);
			progressDialog.setMessage("Sending Message...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			String message = args[0];
			service.setMessage(message);
			return service.sendTweet(message);
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(final Boolean requestWellHandled) {
			// dismiss the dialog after getting all products
			progressDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (requestWellHandled) {
						Toast.makeText(getApplicationContext(),
								"Status tweeted successfully", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Tweet cannot be sent", Toast.LENGTH_SHORT)
								.show();
					}
					finish();

				}
			});
		}
	}
}
