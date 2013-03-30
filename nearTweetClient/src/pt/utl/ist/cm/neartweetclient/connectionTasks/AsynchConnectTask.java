package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import android.util.Log;

public class AsynchConnectTask implements Runnable {
	private static final String SERVER_ADDRESS = "10.0.2.2";
	private static final int SERVER_PORT = 8000;

	public void run() {
		try {
			Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);

			ConnectionStatus.getInstance().setSocket(clientSocket);

			ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			
			ConnectionStatus.getInstance().setInputStream(inputStream);
			ConnectionStatus.getInstance().setOutputStream(outputStream);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.e(this.getClass().toString(), "Error connecting");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(this.getClass().toString(), "Error connecting");
		}

		Log.i(this.getClass().toString(), "Connected");
	}

}