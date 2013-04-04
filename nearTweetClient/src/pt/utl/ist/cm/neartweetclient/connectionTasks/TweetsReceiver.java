package pt.utl.ist.cm.neartweetclient.connectionTasks;

import pt.utl.ist.cm.neartweetclient.ui.TweetsStreamActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TweetsReceiver extends BroadcastReceiver {
	
	private TweetsStreamActivity activity;
	
	public TweetsReceiver(TweetsStreamActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(this.activity.getApplicationContext(), "AAAA",Toast.LENGTH_SHORT);
		this.activity.updateList(intent.getStringExtra("tweet"));
	}

}
