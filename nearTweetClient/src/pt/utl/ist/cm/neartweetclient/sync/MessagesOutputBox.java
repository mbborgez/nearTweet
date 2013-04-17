package pt.utl.ist.cm.neartweetclient.sync;

import java.util.concurrent.LinkedBlockingQueue;

import pt.utl.ist.cm.neartweetEntities.pdu.PDU;

public class MessagesOutputBox {
	
	private static MessagesOutputBox instance;
	
	private LinkedBlockingQueue<PDU> outputBoxQueue = new LinkedBlockingQueue<PDU>();
	
	public void registerOutputMessage(PDU pdu){
		outputBoxQueue.add(pdu);
	}
	
	public PDU getNextOutputMessage(){
		return outputBoxQueue.peek();
	}
	
	public static MessagesOutputBox getInstance(){
		if(instance==null){
			instance = new MessagesOutputBox();
		}
		return instance;	}
}
