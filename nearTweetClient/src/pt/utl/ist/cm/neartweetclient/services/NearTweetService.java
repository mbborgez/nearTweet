package pt.utl.ist.cm.neartweetclient.services;

import pt.utl.ist.cm.neartweetclient.exceptions.NearTweetException;

public abstract class NearTweetService {
	
	public abstract void execute() throws NearTweetException;
}
