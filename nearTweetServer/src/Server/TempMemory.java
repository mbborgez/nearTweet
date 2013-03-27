package Server;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import PDU.PollVotePDU;
import PDU.PublishPollPDU;
import PDU.TweetPDU;

public class TempMemory 
{
	static int TWEETSPAMLIMIT = 3;
	static int USERSPAMLIMIT = 5;
	
	public HashMap<String, ObjectOutputStream> listUsers;
	public ArrayList<TweetPDU> listTweets;
	public ArrayList<PublishPollPDU> listPolls;
	public HashMap<String, Integer> tweetSpamVotes;
	public HashMap<String, Integer> userSpamVotes;
	
	public TempMemory()
	{
		this.listUsers = new HashMap<String, ObjectOutputStream>();
		this.listTweets = new ArrayList<TweetPDU>();
		this.listPolls = new ArrayList<PublishPollPDU>();
		this.tweetSpamVotes = new HashMap<String, Integer>();
		this.userSpamVotes = new HashMap<String, Integer>();
	}
	
	public void InsertUser(String userId, ObjectOutputStream obj)
	{
		listUsers.put(userId, obj);
	}
	
	public void RemoveUser(ObjectOutputStream obj)
	{
		for(String user :  this.listUsers.keySet())
		{
			if(this.listUsers.get(user) == obj)
				this.listUsers.remove(user);
		}
	}
	
	public int TweetSpamVote(String tweetId)
	{
		if(tweetSpamVotes.containsKey(tweetId))
		{
			tweetSpamVotes.put(tweetId, tweetSpamVotes.get(tweetId) + 1);
			if(tweetSpamVotes.get(tweetId) >= TWEETSPAMLIMIT)
			{
				this.tweetSpamVotes.remove(tweetId);
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			tweetSpamVotes.put(tweetId, 1);
			return 0;
		}
	}
	
	public int UserSpamVote(String userId)
	{
		if(userSpamVotes.containsKey(userId))
		{
			userSpamVotes.put(userId, userSpamVotes.get(userId) + 1);
			if(userSpamVotes.get(userId) >= USERSPAMLIMIT)
			{
				this.userSpamVotes.remove(userId);
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			userSpamVotes.put(userId, 1);
			return 0;
		}
	}
	public String GetUserID(ObjectOutputStream obj)
	{
		for(String user :  this.listUsers.keySet())
		{
			if(this.listUsers.get(user) == obj)
				return user;
		}
		return "ERRO INTERNO";
	}
	
	public ObjectOutputStream GetUserStream(String id)
	{
		if(this.VerifyIfUserExists(id))
			return this.listUsers.get(id);
		
		return null;
	}
	
	public String GetUserFromPollID(String id)
	{
		for(PublishPollPDU pdu : listPolls)
		{
			if(pdu.GetTweetId().equals(id))
				return pdu.GetUserId();
		}
		
		return "ERRO INTERNO";
	}
	
	public String GetUserFromTweetID(String id)
	{
		for(TweetPDU pdu : listTweets)
		{
			if(pdu.GetTweetId().equals(id))
				return pdu.GetUserId();
		}
		
		return "ERRO INTERNO";
	}
	
	public boolean VerifyIfUserExists(String userId)
	{
		return listUsers.containsKey(userId);
	}
	
	public void InsertTweet(String userId, String tweetId, String text, byte[] mediaObject)
	{
		listTweets.add(new TweetPDU(userId, tweetId, text, mediaObject));
	}
	
	public void RemoveTweet(String tweetId)
	{
		for(TweetPDU tweet : this.listTweets)
		{
			if(tweet.GetTweetId().equals(tweetId))
			{
				listTweets.remove(tweet);
				return;
			}
		}
	}
	
	public boolean VerifyIfTweetExists(String tweetId)
	{
		for(TweetPDU pdu : listTweets)
		{
			if(pdu.GetTweetId().equals(tweetId))
				return true;
		}
		return false;
	}
	
	public void InsertPoll(String userId, String tweetId, String text, ArrayList<String> options)
	{
		listPolls.add(new PublishPollPDU(userId, tweetId, text, options));
	}
	
	public boolean VerifyIfPollExists(String tweetId)
	{
		for(PublishPollPDU pdu : listPolls)
		{
			if(pdu.GetTweetId().equals(tweetId))
				return true;
		}
		return false;
	}
}