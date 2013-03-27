package pt.utl.ist.cm.neartweetclient.tasks;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import pt.utl.ist.cm.neartweetEntities.PDU.RegisterPDU;

import android.os.AsyncTask;
import android.util.Log;

public class ClientServerConnectorTask extends AsyncTask<String, Void, Integer> {
    private static final String SERVER_ADDRESS = "10.0.2.2";
	private static final int SERVER_PORT = 8000;
	private Socket client;
    
    protected Integer doInBackground(String...strings) {
            // validate input parameters
            if (strings.length <= 0) {
                    return 0;
            }
            // connect to the server and send the message
            try {
            	Log.i("Task", "before creating socket");
                    client = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                    Log.i("Task", "after creating socket");

                    //TODO: should get the clientID from the strings given as argument.
                    Log.i("Task", "before creating writing object");

                    outputStream.writeObject(new RegisterPDU("1"));
                    outputStream.flush();
                    Log.i("Task", "after writing object");

//                    printwriter = new PrintWriter(client.getOutputStream(),true);
//                    printwriter.write(strings[0]);
//                    printwriter.flush();
//                    printwriter.close();
                    Log.i("Task", "before closing socket");
                    client.close();
                    Log.i("Task", "after closing socket");

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