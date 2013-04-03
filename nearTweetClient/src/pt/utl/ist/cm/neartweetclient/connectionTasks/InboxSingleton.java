package pt.utl.ist.cm.neartweetclient.connectionTasks;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class InboxSingleton {
	private static InboxSingleton instance;
	private List<PDU> receivedPduList = new ArrayList<PDU>();
	
	private InboxSingleton(){ /* Avoid instanciation */ }
	
	public static InboxSingleton getInstance(){
		if(instance==null){
			InboxSingleton.instance = new InboxSingleton();
		}
		return InboxSingleton.instance;
	}
	
	public void addReceivedMessage(PDU pdu){
		receivedPduList.add(pdu);
	}
	
	public List<PDU> getAllReceivedMessges(){
		return receivedPduList;
	}
}
