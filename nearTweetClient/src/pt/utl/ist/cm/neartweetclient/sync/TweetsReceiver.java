package pt.utl.ist.cm.neartweetclient.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TweetsReceiver extends BroadcastReceiver {
	
	public TweetsReceiver() { /* should be empty */}

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "AAAA",Toast.LENGTH_SHORT).show();
		Log.i("DEBUG", "RECEIVED");
	}

}
