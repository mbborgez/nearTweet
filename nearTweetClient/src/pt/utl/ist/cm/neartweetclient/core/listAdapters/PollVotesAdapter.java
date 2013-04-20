package pt.utl.ist.cm.neartweetclient.core.listAdapters;

import java.util.ArrayList;
import java.util.List;
import pt.utl.ist.cm.neartweetclient.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PollVotesAdapter extends ArrayAdapter<String> {

	private List<String> pollVotesText;
	private Context context;

	public PollVotesAdapter(Context context, int textViewResourceId, List<String> pollVotesText) {
		super(context, textViewResourceId, pollVotesText);
		this.pollVotesText = new ArrayList<String>(pollVotesText);
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater viewInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = viewInflater.inflate(R.layout.poll_option_layout, null);
		}
		
		TextView pollOptionDescription = (TextView) convertView.findViewById(R.id.pollOption_description);
		pollOptionDescription.setText(pollVotesText.get(position));
		
		return convertView;
	}
}
