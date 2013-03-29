package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
import java.net.UnknownHostException;
import pt.utl.ist.cm.neartweetEntities.PDU.RegisterPDU;
import android.os.AsyncTask;

public class SendTask extends AsyncTask<String, Void, Integer> {
    protected Integer doInBackground(String...strings) {
            // connect to the server and send the message
            try {
	            RegisterPDU registerPdu = new RegisterPDU("borgez");
	            
	            ConnectionStatus.getInstance().getOutputStream().writeObject(registerPdu);
	            
	            ConnectionStatus.getInstance().getOutputStream().flush();
            } catch (UnknownHostException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            
            return 0;
    }
    
    protected void onPostExecute(Long result) {
    	//TODO
            return;
    }
}