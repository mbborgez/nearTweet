package pt.utl.ist.cm.neartweetclient.ui;

import java.io.ByteArrayOutputStream;
import java.util.List;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.services.CreateTweetService;
import pt.utl.ist.cm.neartweetclient.services.NetworkDownloadService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NewTweetActivity extends Activity {

	private static final String DATA_EXTRA = "data";
	private static final String DEFAULT_URL = "http://upload.wikimedia.org/wikipedia/pt/e/ed/IST_Logo.png";
	protected static final int TAKE_PHOTO_ACTION = 1337;
	private byte[] tweetImageBytes;
	
	private EditText tweet;
	private Button cancelButton;
	private Button submitButton;
	private Button takePhotoButton;
	private Button addMultimediaButton;
	private EditText multimediaLinkEditText;
	private ImageView tweetImagePreview;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_tweet);

		cancelButton = (Button) findViewById(R.id.cancelTweet);
		submitButton = (Button) findViewById(R.id.submitTweet);
		addMultimediaButton = (Button) findViewById(R.id.addMultimediaTweet);
		takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
		tweetImagePreview = (ImageView) findViewById(R.id.tweetImagePreview);
		multimediaLinkEditText = (EditText) findViewById(R.id.multimediaLink);
		tweet = (EditText) findViewById(R.id.tweetText);

		cancelButton.setOnClickListener(cancelClickListener);
		submitButton.setOnClickListener(submitTweetClickListener);
		multimediaLinkEditText.setText(DEFAULT_URL);
		Log.i("DEBUG", "onCreate");
		takePhotoButton.setOnClickListener(takePhotoClickListener);
		addMultimediaButton.setOnClickListener(downloadImageClickListener);

	}

	/************************************************************************
	 ************************* Click Listeners ******************************
	 ************************************************************************/

	private final OnClickListener cancelClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private final OnClickListener submitTweetClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (getTweetText().length() > 0) {
				(new SubmitTweetTask(getTweetText(), tweetImageBytes)).execute();
			} else {
				errorMessage();
			}
		}
	};

	private final OnClickListener downloadImageClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new PhotoDownloaderTask().execute(getImageLink());
		}
	};

	private final OnClickListener takePhotoClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(TAKE_PHOTO_ACTION);
		}
	};

	/************************************************************************
	 ************************* Camera Activities ****************************
	 ************************************************************************/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == TAKE_PHOTO_ACTION && resultCode == RESULT_OK){
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

	private void handleSmallCameraPhoto(final Intent intent) {
		
		new AsyncTask<Void, Void, Boolean>(){
			Bitmap photoBitmap;
			byte[] photoBytes;
			
			@Override
			protected Boolean doInBackground(Void... params) {
				Bundle extras = intent.getExtras();
				Log.i("DEBUG", "handleSmallCameraPhoto - extras: " + extras);
				if(extras!=null && extras.containsKey(DATA_EXTRA) && extras.get(DATA_EXTRA)!=null){
					try{
						photoBitmap = (Bitmap) extras.get(DATA_EXTRA);
						photoBytes = compressBitmapImage(photoBitmap);
						return true;
					} catch(Exception e){
						e.printStackTrace();
						return false;
					}
				}
				return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result){
				if(result){
					tweetImagePreview.setImageBitmap(photoBitmap);
					tweetImageBytes = photoBytes;
				} else {
					errorTakingPhoto();
				}
			}
		}.execute();

	}

	private byte[] compressBitmapImage(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	/************************************************************************
	 **************************** Async Tasks *******************************
	 ************************************************************************/

	private class SubmitTweetTask extends AsyncTask<String, Void, Boolean> {

		private byte[] tweetImageBytes;
		private String tweetText;

		public SubmitTweetTask(String tweetText, byte[] tweetImageBytes){
			this.tweetText = tweetText;
			this.tweetImageBytes = tweetImageBytes;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return (new CreateTweetService(tweetText, tweetImageBytes, getApplicationContext())).execute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				nextScreen(); 
			} else {
				errorMessage();
			}
		}
	}

	private class PhotoDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private NetworkDownloadService service;

		@Override
		protected Bitmap doInBackground(String... urls) {
			if(urls.length>0 && urls[0]!=null){
				service = new NetworkDownloadService(urls[0]);
				service.execute();
				return service.getBitmap();
			} else {
				return null;
			}
		}

		protected void onPostExecute(Bitmap bitmap) {
			if(bitmap!=null && service.getBitmap()!=null && service.getBitmapBytes()!=null){
				tweetImagePreview.setImageBitmap(service.getBitmap());
				tweetImageBytes = service.getBitmapBytes();
			} else {
				errorLoadingImageFromNetwork();
			}
		}
	}

	private String getImageLink() {
		return multimediaLinkEditText.getText().toString();
	}

	private String getTweetText() {
		return tweet.getText().toString();
	}

	public void errorLoadingImageFromNetwork() {
		Toast.makeText(getApplicationContext(), "Error loading image from the network", Toast.LENGTH_SHORT).show();
	}

	private void errorTakingPhoto() {
		Toast.makeText(getApplicationContext(), "Error loading image from the camera", Toast.LENGTH_SHORT).show();
	}

	public void nextScreen() {
		finish();
	}

	public void errorMessage() {
		Toast.makeText(this,"You cannot tweet an empty message", Toast.LENGTH_SHORT).show();
	}
}
