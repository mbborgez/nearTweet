package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectTask extends AsyncTask<String, Void, Integer> {
	private static final String SERVER_ADDRESS = "10.0.2.2";
	private static final int SERVER_PORT = 8000;

	protected Integer doInBackground(String... strings) {
		// validate input parameters
		try {
			
			Log.i("ConnectTask", "Trying to connect to " + SERVER_ADDRESS + ":" + SERVER_PORT);
			Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			ConnectionStatus.getInstance().setSocket(clientSocket);
			ConnectionStatus.getInstance().setInputStream(new ObjectInputStream(clientSocket.getInputStream()));
			ConnectionStatus.getInstance().setOutputStream(new ObjectOutputStream(clientSocket.getOutputStream()));
			Log.i("ConnectTask", "Connected =)");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.e("ConnectTask", "Error connecting");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("ConnectTask", "Error connecting");
		}

		return 0;
	}

	protected void onPostExecute(Long result) {
		// TODO
		return;
	}
}