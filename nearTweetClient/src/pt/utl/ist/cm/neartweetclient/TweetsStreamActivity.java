package pt.utl.ist.cm.neartweetclient;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TweetsStreamActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// storing string resources into Array
		String[] adobe_products = new String[]{"t1", "t2", "t3"};
		
		// Binding resources Array to ListAdapter
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_tweets_stream, R.id.tweets_stream_text, adobe_products));

		// Adding a click listener to each tweet item
		ListView tweetsListView = getListView();
		tweetsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showTweetDetails("t1");
			}
			
		});
	}
	
	protected void showTweetDetails(String tweetId) {
		Intent tweetDetailsIntent = new Intent(this, TweetDetailsAcitivity.class);
		tweetDetailsIntent.putExtra(TweetDetailsAcitivity.TWEET_ID, tweetId);
		startActivity(tweetDetailsIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweets_stream, menu);
		return true;
	}

}
