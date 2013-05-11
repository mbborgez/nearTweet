package pt.utl.ist.cm.neartweetclient.ui;

import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.PeersManager;
import pt.utl.ist.cm.neartweetclient.core.listAdapters.ClientsListAdapter;
import pt.utl.ist.cm.neartweetclient.utils.Actions;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;

public class UsersActivity extends ListActivity {

	private ClientsListAdapter clientsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		
		clientsListAdapter = new ClientsListAdapter(getApplicationContext(), R.layout.user_layout, PeersManager.getUsers());
        setListAdapter(clientsListAdapter);
	    
        // Put whatever message you want to receive as the action
		registerAsyncUpdates();
	}

	@Override
    public void onResume() {
        super.onResume();

        // Put whatever message you want to receive as the action
        registerAsyncUpdates();
        
        //Update all the PDUS on the Adapter
        clientsListAdapter.notifyDataSetChanged();
        
    }
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.broadcastReceiver);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.users, menu);
		return true;
	}
	
	private void registerAsyncUpdates() {
		IntentFilter iff = new IntentFilter();
        iff.addAction(Actions.BROADCAST_NEW_USER);
        iff.addAction(Actions.BROADCAST_SPAMMER_BLOCKED);
        this.registerReceiver(broadcastReceiver, iff);
	}
	
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Actions.BROADCAST_NEW_USER)) {
				clientsListAdapter.notifyDataSetChanged();	
			}
			else if (intent.getAction().equals(Actions.BROADCAST_SPAMMER_BLOCKED)) {
				clientsListAdapter.notifyDataSetChanged();	
			}
		}
    };

}
