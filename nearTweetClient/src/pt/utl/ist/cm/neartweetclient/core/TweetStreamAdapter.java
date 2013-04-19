package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;
import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetStreamAdapter extends ArrayAdapter<PDU> {

	private ArrayList<PDU> pdus;
	private Context context;

	public TweetStreamAdapter(Context context, int textViewResourceId, ArrayList<PDU> objects) {
		super(context, textViewResourceId, objects);
		this.pdus = objects;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater viewInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = viewInflater.inflate(R.layout.tweet_layout, null);
		}
		
		TextView name = (TextView) convertView.findViewById(R.id.userName);
		TextView message = (TextView) convertView.findViewById(R.id.tweetMessage);
		ImageView image = (ImageView) convertView.findViewById(R.id.tweetImage);

		PDU pdu = pdus.get(position);

		if (pdu != null) {
			if (pdu instanceof TweetPDU) {
				populateTweetItem(name, message, image, (TweetPDU) pdu);
			} else if(pdu instanceof PublishPollPDU) {
				populatePollItem(name, message, (PublishPollPDU) pdu);
			} else {
				return null;
			}
		}
		
		return convertView;
	}

	private void populatePollItem(TextView name, TextView message, PublishPollPDU publishPDU) {
		Log.i("DEBUG", "populatePollItem" + publishPDU.GetTweetId());
		if (name != null) {
			name.setText(publishPDU.GetTweetId());
		}
		if (message != null) {
			message.setText("POLL MESSAGE: " + publishPDU.GetText());
		}
	}

	private void populateTweetItem(TextView name, TextView message, ImageView image, TweetPDU tweetPDU) {
		Log.i("DEBUG", "populateTweetItem" + tweetPDU.GetTweetId());
		if (name != null) {
			name.setText(tweetPDU.GetTweetId());
		}
		
		if (message != null) {
			message.setText(tweetPDU.GetText());
		}
		
		if(tweetPDU.GetMediaObject()!=null && tweetPDU.GetMediaObject().length>0){
			Bitmap bitmap = BitmapFactory.decodeByteArray(tweetPDU.GetMediaObject(), 0, tweetPDU.GetMediaObject().length);
			image.setImageBitmap(bitmap);
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.GONE);
		}
	}

//	public void addTweet(PDU pdu) {
//		this.pdus.add(pdu);
//		notifyDataSetChanged();
//	}
}