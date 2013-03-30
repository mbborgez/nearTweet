package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionStatus {
	public static final ConnectionStatus instance = new ConnectionStatus();

	public static final String MESSAGE_RECEIVED_DATA = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.receivedData";

	public static final String POLL_VOTE_PDU_RECEIVED = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.PollVotePDU";
	public static final String PUBLISH_POLL_PDU_RECEIVED = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.PublishPollPDU";
	public static final String REGISTER_PDU_RECEIVED = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.RegisterPDU";
	public static final String REPLY_PDU_RECEIVED = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.ReplyPDU";
	public static final String SPAM_VOTE_PDU_RECEIVED = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.SpamVotePDU";
	public static final String TWEET_PDU_RECEIVED = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.TweetPDU";
	public static final String GENERIC_MESSAGE_PDU_RECEIVED = "pt.utl.ist.cm.neartweetclient.connectionTasks.ConnectionStatus.GenericMessagePDU";
	
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;
	
	public static ConnectionStatus getInstance() {
		return instance;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setSocket(Socket clientSocket) {
		this.socket = clientSocket;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public boolean isConnected() {
		return socket!=null && socket.isConnected();
	}
	
}
