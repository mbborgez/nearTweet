package pt.utl.ist.cm.neartweetclient.core.listAdapters;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import pt.utl.ist.cm.neartweetEntities.pdu.ReplyPDU;
import pt.utl.ist.cm.neartweetEntities.pdu.TweetPDU;
import pt.utl.ist.cm.neartweetclient.R;
import pt.utl.ist.cm.neartweetclient.core.TweetConversation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetConversationAdapter extends ArrayAdapter<ReplyPDU> {

	private List<PDU> pdus;
	private Context context;

	public TweetConversationAdapter(Context context, int textViewResourceId, TweetConversation tweetConversation) {
		super(context, textViewResourceId, tweetConversation.getConversation());
		this.pdus = new ArrayList<PDU>(tweetConversation.getConversation());
		this.pdus.add(tweetConversation.getMainMessage());
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater viewInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = viewInflater.inflate(R.layout.tweet_layout, null);
		}

		PDU pdu = pdus.get(position);

		if (pdu != null) {
			// duck typing
			TextView name = (TextView) view.findViewById(R.id.userName);
			TextView message = (TextView) view.findViewById(R.id.tweetMessage);
			ImageView image = (ImageView) view.findViewById(R.id.tweetImage);

			if (pdu instanceof TweetPDU) {
				TweetPDU currentPDU = (TweetPDU) pdu;

				if (name != null) {
					name.setText(currentPDU.getId());
				}
				if (message != null) {
					message.setText(currentPDU.GetText());
				}
				if(currentPDU.GetMediaObject()!=null && currentPDU.GetMediaObject().length>0){
					Bitmap bitmap = BitmapFactory.decodeByteArray(currentPDU.GetMediaObject(), 0, currentPDU.GetMediaObject().length);
					image.setImageBitmap(bitmap);
					image.setVisibility(View.VISIBLE);
				} else {
					image.setVisibility(View.GONE);
				}
			} else if(pdu instanceof ReplyPDU) {
				ReplyPDU currentPDU = (ReplyPDU) pdu;

				if (name != null) {
					name.setText(currentPDU.getId());
				}
				if (message != null) {
					message.setText(currentPDU.getText());
				}
				image.setVisibility(View.GONE);
			}
		}
		return view;
	}

}