package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetclient.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TweetAdapter extends BaseAdapter {
	private static ArrayList<String> tweetsList;

	private LayoutInflater mInflater;

	public TweetAdapter(Context context, ArrayList<String> tweets) {
	    this.tweetsList = tweets;
	    this.mInflater = LayoutInflater.from(context);
	}

	public void updateResults(ArrayList<String> results) {
	    this.tweetsList = results;
	    //Triggers the list update
	    notifyDataSetChanged();
	}
	
	public void addTweet(String result) {
		this.tweetsList.add(result);
	}

	public int getCount() {
	    return this.tweetsList.size();
	}

	public Object getItem(int position) {
	    return this.tweetsList.get(position);
	}

	public long getItemId(int position) {
	    return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mInflater.inflate(R.layout.activity_create_poll, null);
	}

}