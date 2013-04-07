package pt.utl.ist.cm.neartweetclient.core;

import java.util.ArrayList;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.PublishPollPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<PDU> {

	    private ArrayList<PDU> pdus;
	    private Context context;

	    public TweetAdapter(Context context, int textViewResourceId, ArrayList<PDU> objects) {
	        super(context, textViewResourceId, objects);
	        this.pdus = objects;
	        this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater viewInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = viewInflater.inflate(R.layout.tweet, null);
	        }

	        PDU pdu = pdus.get(position);
 
	        if (pdu != null) {
	        	// duck typing
	        	if (pdu instanceof TweetPDU) {
	        		TweetPDU currentPDU = (TweetPDU) pdu;
	        		TextView name = (TextView) view.findViewById(R.id.userName);
	        		TextView message = (TextView) view.findViewById(R.id.tweetMessage);
	        		if (name != null) {
	        			name.setText(currentPDU.GetTweetId());
	        		}
	        		if (message != null) {
	        			message.setText(currentPDU.GetText());
	        		}
	        		
	        	} else if(pdu instanceof PublishPollPDU) {
	        		PublishPollPDU currentPDU = (PublishPollPDU) pdu;
	        		TextView name = (TextView) view.findViewById(R.id.userName);
	        		TextView message = (TextView) view.findViewById(R.id.tweetMessage);
	        		if (name != null) {
	        			name.setText(currentPDU.GetTweetId());
	        		}
	        		if (message != null) {
	        			message.setText("POLL MESSAGE: " + currentPDU.GetText());
	        		}
	        	} else if(pdu instanceof ReplyPDU) {
	        		ReplyPDU currentPDU = (ReplyPDU) pdu;
	        		TextView name = (TextView) view.findViewById(R.id.userName);
	        		TextView message = (TextView) view.findViewById(R.id.tweetMessage);
	        		if (name != null) {
	        			name.setText(currentPDU.GetTweetId());
	        		}
	        		if (message != null) {
	        			message.setText("RESPONSE: " + currentPDU.GetText());
	        		}
	        	}
	            
	        }
	        return view;
	   }
	    
	    public void addTweet(PDU pdu) {
	    	this.pdus.add(pdu);
	    	notifyDataSetChanged();
	    }
}