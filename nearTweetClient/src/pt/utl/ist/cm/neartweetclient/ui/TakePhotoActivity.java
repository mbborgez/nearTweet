package pt.utl.ist.cm.neartweetclient.ui;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import pt.utl.ist.cm.neartweetclient.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TakePhotoActivity extends Activity {

	private static final String IMAGE_TMP = "image.tmp";
	// keep track of camera capture intent
	private static final int CAMERA_PIC_REQUEST = 1337;
	// keep track of cropping intent
	final int PIC_CROP = 2;
	// image preview
	ImageView imageView;
	// take photo button
	Button takePhotoButton;
	
	File tempFile;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);

		tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_TMP);

		imageView = (ImageView) findViewById(R.id.picture);

		takePhotoButton = (Button) findViewById(R.id.TakePhotoButton);
		takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePhoto();
			}
		});
	}

	private void takePhoto() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			startActivityForResult(intent, CAMERA_PIC_REQUEST);
		} catch (ActivityNotFoundException e) {
			// display an error message
			e.printStackTrace();
			String errorMessage = "Whoops - your device doesn't support capturing images!";
			Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Handle user returning from both capturing and cropping the image
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
//			InputStream inputStream=null;
//			try {
//				inputStream=new FileInputStream(tempFile);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//			//fix for some of the android camera's implementations
//			if(inputStream==null){
//				try {
//					Uri imageURI = data.getData();
//					inputStream=getContentResolver().openInputStream(imageURI);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//
//			previewPhoto(inputStream);
			new PhotoDownloaderTask().execute(data.getData().getPath());
//		}
	}
	
	private class PhotoDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            return loadImage(urls[0]);
        }
        protected void onPostExecute(Bitmap result) {
        	imageView.setImageBitmap(result);
             setContentView(imageView);
        }
    }
	
    private Bitmap loadImage(String url){
         try {
        	 return BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
         } catch (Exception e) {
             e.printStackTrace(); 
             return null;
       }
    }
	
	
	private void previewPhoto(InputStream is) {
		Bitmap bitmapPreview = BitmapFactory.decodeStream(is);
		imageView.setImageBitmap(bitmapPreview);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		tempFile.delete();
	}
}