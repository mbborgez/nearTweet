package pt.utl.ist.cm.neartweetclient.core.listAdapters;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.MemCacheProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
				TweetPDU tweetPdu = (TweetPDU) pdu;
				populateTweetItem(name, message, image, tweetPdu);
			} else if(pdu instanceof PublishPollPDU) {
				PublishPollPDU pollPdu = (PublishPollPDU) pdu;
				populatePollItem(name, message, pollPdu);
			}
		}
		
		return convertView;
	}

	private void populatePollItem(TextView name, TextView message, PublishPollPDU publishPDU) {
		boolean hasunreadMessages = MemCacheProvider.getPollConversation(publishPDU.getId()).isHasUnreadMessages();
		Log.i("DEBUG", "populatePollItem" + publishPDU.getId());
		if (name != null) {
			name.setText(publishPDU.getId());
			name.setTextColor(hasunreadMessages ? Color.RED : Color.BLACK);
		}
		if (message != null) {
			message.setText("POLL MESSAGE: " + publishPDU.getText());
			message.setTextColor(hasunreadMessages ? Color.RED : Color.BLACK);
		}
	}

	private void populateTweetItem(TextView name, TextView message, ImageView image, TweetPDU tweetPDU) {
		Log.i("DEBUG", "populateTweetItem" + tweetPDU.getId());
		
		boolean hasunreadMessages = MemCacheProvider.getTweetConversation(tweetPDU.getId()).isHasUnreadMessages();
		
		if (name != null) {
			name.setText(tweetPDU.getId());
			name.setTextColor(hasunreadMessages ? Color.RED : Color.BLACK);
		}
		
		if (message != null) {
			message.setText(tweetPDU.GetText());
			message.setTextColor(hasunreadMessages ? Color.RED : Color.BLACK);
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