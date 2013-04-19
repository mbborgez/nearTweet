package pt.utl.ist.cm.neartweetclient.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class NetworkDownloadService implements INearTweetService {

	private String url;
	private Bitmap bitmap;
	private byte[] bitmapBytes;
	
	public NetworkDownloadService(String url) {
		this.url = url;
	}

	@Override
	public boolean execute() {
		try {
			bitmap = loadImageFromNetwork(url);
			bitmapBytes = compressImageBitmap(bitmap);
			return bitmap!=null;
		} catch (Exception e) {
			Log.i("DEGUB", e.getMessage());
			bitmap = null;
			bitmapBytes = null;
			return false;
		}
	}
	
	private byte[] compressImageBitmap(Bitmap bitmapArg){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmapArg.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}
	
	private Bitmap loadImageFromNetwork(String url) throws MalformedURLException, IOException {
		return BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public byte[] getBitmapBytes() {
		return bitmapBytes;
	}

}
