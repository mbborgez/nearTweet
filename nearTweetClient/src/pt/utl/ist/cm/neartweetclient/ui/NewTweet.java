package pt.utl.ist.cm.neartweetclient.ui;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.services.CreateTweetService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NewTweet extends Activity {

	protected static final int TAKE_PHOTO_ACTION = 1234;
	EditText tweet;
	Button cancelButton;
	Button submitButton;
	
	Button takePhotoButton;
	Button addMultimediaButton;
	EditText multimediaLinkEditText;
	ImageView tweetImagePreview;
	byte[] tweetImageBytes;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_tweet);

		cancelButton = (Button) findViewById(R.id.cancelTweet);
		submitButton = (Button) findViewById(R.id.submitTweet);
		
		addMultimediaButton = (Button) findViewById(R.id.addMultimediaTweet);
		takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
		
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
		
		//MULTIMEDIA STUFF - start
		tweetImagePreview = (ImageView) findViewById(R.id.tweetImagePreview);
		String url = "http://upload.wikimedia.org/wikipedia/pt/e/ed/IST_Logo.png";
		multimediaLinkEditText = (EditText) findViewById(R.id.multimediaLink);
		multimediaLinkEditText.setText(url);
		addMultimediaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new PhotoDownloaderTask(tweetImagePreview).execute(multimediaLinkEditText.getText().toString());
			}
		});
		takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent(TAKE_PHOTO_ACTION);
			}
		});
		//MULTIMEDIA STUFF - end

	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(requestCode==TAKE_PHOTO_ACTION){
			handleSmallCameraPhoto(data);
		}
	}
	
	private void dispatchTakePictureIntent(int actionCode) {
		if(isIntentAvailable(getApplicationContext(), MediaStore.ACTION_IMAGE_CAPTURE)){
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, actionCode);
		} else {
			Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT).show();
		}
	}
	
	public static boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	private void handleSmallCameraPhoto(Intent intent) {
	    Bundle extras = intent.getExtras();
	    Bitmap mImageBitmap = (Bitmap) extras.get("data");
	    tweetImagePreview.setImageBitmap(mImageBitmap);
	    
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		tweetImageBytes = stream.toByteArray();
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
			Log.i("DEBUG", "before service");
			CreateTweetService service = new CreateTweetService(username, text, tweetImageBytes, this);

			try {			
				 service.execute("");
			} catch(Exception e) {
				errorMessage();
				finish();
			}
		}
	}
	
	private class PhotoDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		
		private ImageView imageView;

		public PhotoDownloaderTask(ImageView imageView){
			this.imageView = imageView;
		}
		
		@Override
		protected Bitmap doInBackground(String... urls) {
			return loadImageFromNetwork(urls[0]);
		}

		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			result.compress(Bitmap.CompressFormat.PNG, 100, stream);
			tweetImageBytes = stream.toByteArray();
		}
	}

	private Bitmap loadImageFromNetwork(String url){
		try {
			return BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}
	
	
	//	private void submitTweet(final String text) {
	//		AsyncTask<String, Integer, Boolean> my_task = new AsyncTask<String, Integer, Boolean>() {
	//			final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	//			final String userName = settings.getString("username", null);
	//			
	//			@Override
	//			protected Boolean doInBackground(String... params) {
	//				try {
	//					Log.i("DEBUG", "doInBackground");
	//					String lastTweetID = userName + Actions.getLastTweet(getApplicationContext());
	//					sendTweet(lastTweetID);
	//				} catch(NearTweetException e) {
	//					e.printStackTrace();
	//					Log.i("NEART WEET EXCEPTION", e.getMessage());
	//					return false;
	//				}
	//				return true;
	//			}
	//			
	//			@Override
	//			 protected void onPostExecute(Boolean result) {
	//		        if (result) {
	//		       	 nextScreen(); 
	//		        } else {
	//		       	 errorMessage();
	//		        }
	//		    }
	//			
	//			private void sendTweet(String tweetID) {
	//				try {
	//					 Log.i("DEBUG", "sending tweet" + tweetID);
	//					TweetPDU pdu = new TweetPDU(userName, tweetID, text, null);
	//					Connection.getInstance().sendPDU(pdu);
	//					Actions.incrementTweetID(getApplicationContext());
	//				} catch (UnknownHostException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				} catch (IOException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//			}
	//		};
	//		
	//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	//			my_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
	//		else
	//		    my_task.execute();
	//	}

	public void nextScreen() {
		finish();
	}


	public void errorMessage() {
		Toast.makeText(this,"You cannot tweet an empty message", Toast.LENGTH_SHORT).show();
	}

}
