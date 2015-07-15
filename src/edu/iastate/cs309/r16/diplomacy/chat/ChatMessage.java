package edu.iastate.cs309.r16.diplomacy.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.iastate.cs309.r16.diplomacy.users.User;

public class ChatMessage implements Comparable<ChatMessage>
{

	/**
	 * It is important that timestamp is unique so users know who sent what
	 * first. This also is used in the compareTo method
	 */
	protected long timestamp;
	protected User sender;
	protected String message;

	/**
	 * at re-initialization this may not be the last timestampe on the database
	 * but it ensures the next timestamp will be unique
	 */
	protected static long lastTimeStamp = System.currentTimeMillis();

	/**
	 * Creates a Chatmessage with the sender and the message where the curren
	 * time in millisecond is set as the the timestamp
	 * 
	 * @param sender
	 *            - the sending user
	 * @param message
	 *            - the contents of the message
	 * 
	 */
	public ChatMessage(User sender, String message)
	{
		// create timestamp
		// this insures there are not duplicates
		long curTime = System.currentTimeMillis();
		while (curTime == lastTimeStamp)
			curTime = System.currentTimeMillis();

		this.timestamp = curTime;
		lastTimeStamp = curTime;

		// verify sender
		if (sender == null)
			throw new IllegalArgumentException("Sender cannot be null");
		this.sender = sender;

		// verify message
		if (message == null || message.equals(""))
			throw new IllegalArgumentException("Message cannot be empty");
		this.message = message;
	}

	public String getSender()
	{
		return sender.getUsername();
	}

	public String getMessage()
	{
		return message;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	@Override
	public boolean equals(Object o)
	{
		boolean ret = false;
		if (o instanceof ChatMessage)
			ret = ((ChatMessage) o).compareTo(this) == 0;

		return ret;
	}

	@Override
	public String toString()
	{

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String formattedDate = formatter.format(new Date(timestamp));
		return "(" + formattedDate + ")" + sender + ":" + message;
	}

	@Override
	public int compareTo(ChatMessage o)
	{
		// first and foremost is time
		int cmp = ((Long) timestamp).compareTo(o.timestamp);

		// next message content
		if (cmp == 0)
			cmp = message.compareTo(o.message);

		if (cmp == 0) // next by user
			cmp = sender.compareTo(o.sender);

		return cmp;
	}
}
