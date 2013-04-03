package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
import java.net.UnknownHostException;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;
import android.content.Context;

public class StreamingHandler implements Runnable {

	private Context context;

	public StreamingHandler(Context context){
		this.context = context;
	}

	@Override
	public void run() {
		try {
			while(Connection.getInstance().isAlive()){
				PDU pdu = Connection.getInstance().receiveData();
				ClientPDUVisitor pduVisitor = new ClientPDUVisitor(this.context);
				pdu.accept(pduVisitor);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
