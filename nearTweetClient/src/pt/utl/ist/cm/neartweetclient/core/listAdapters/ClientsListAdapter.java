package pt.utl.ist.cm.neartweetclient.core.listAdapters;

import java.util.ArrayList;
import java.util.List;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.SpamManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ClientsListAdapter extends ArrayAdapter<String> {

	private List<String> users;
	private Context context;

	public ClientsListAdapter(Context context, int textViewResourceId, List<String> users) {
		super(context, textViewResourceId, users);
		this.users = new ArrayList<String>(users);
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater viewInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = viewInflater.inflate(R.layout.user_layout, null);
		}
		String userId = users.get(position);
		
		TextView userNameTextView = (TextView) convertView.findViewById(R.id.user_name);
		if(!SpamManager.isBlocked(userId)) {
			userNameTextView.setText(userId);
		} else {
			userNameTextView.setText(userId + " - " + "SPAMMER");
			userNameTextView.setTextColor(Color.RED);
		}
		
		return convertView;
	}
}
