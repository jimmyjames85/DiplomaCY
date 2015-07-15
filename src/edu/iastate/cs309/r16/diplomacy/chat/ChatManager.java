package edu.iastate.cs309.r16.diplomacy.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import edu.iastate.cs309.r16.diplomacy.users.InvalidUserException;
import edu.iastate.cs309.r16.diplomacy.users.User;
import edu.iastate.cs309.r16.diplomacy.users.UserManager;
import edu.iastate.cs309.r16.diplomacy.users.UserNotFoundException;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;
import edu.iastate.cs309.r16.diplomacy.util.Logger;

public class ChatManager
{

	private static final String CM_TABLE = "chat_messages";
	private static final String CM_USERNAME = "username";
	private static final String CM_CHATROOM = "chatRoom";
	private static final String CM_TIME = "time";
	private static final String CM_MESSAGE = "message";
	private static final String CM_SEPARATOR = ";";
	@SuppressWarnings("unused")
	private static final Logger LOG = new Logger(ChatManager.class);

	/**
	 * 
	 * @param users
	 *            list of users that defines the chatroom
	 * 
	 * @return the chat room name for the room with the list of users given
	 * @throws InvalidChatRoomException
	 */
	public static String createChatRoomName(List<String> users) throws InvalidChatRoomException
	{
		// verify users
		if (users == null || users.size() < 1)
			throw new InvalidChatRoomException("Users cannot be null or empty");

		return createChatRoomName(users.get(0), users);
	}

	/**
	 * 
	 * @param username
	 *            the username gernerating the request
	 * @param users
	 *            list of users that defines that chat room along with username
	 * @return the chat room name for the room with the list of users given
	 *         along with username
	 * @throws InvalidChatRoomException
	 */
	public static String createChatRoomName(String username, List<String> users) throws InvalidChatRoomException
	{
		// verify users
		if (users == null || username == null || username.equals(""))
			throw new InvalidChatRoomException("Users cannot be null");

		TreeSet<User> userList = new TreeSet<User>();
		for (String u : users)
			userList.add(new User(u));

		userList.add(new User(username));

		// verify we have at least 2 users
		if (userList.size() < 2)
			throw new InvalidChatRoomException("users list must have at least two unique users");

		String ret = "";

		TreeSet<String> allUsernames = new TreeSet<String>();
		for (User u : userList)
			allUsernames.add(u.getUsername().toUpperCase());
		// toUpper because mySql is not case-sensitive under comparisons
		// ('UsEr1' is the same as 'user1')

		Iterator<String> itr = allUsernames.iterator();
		while (itr.hasNext())
		{
			ret += itr.next();
			if (itr.hasNext())
				ret += CM_SEPARATOR;
		}
		return ret;
	}

	/**
	 * Sends a message from one user to a list of users. This will create a
	 * chatroom if one doens't exist
	 * 
	 * @param message
	 *            - content of the message
	 * @param sender
	 *            -who is sending the message
	 * @param users
	 *            -to whom the message is being sent to
	 * @throws UserNotFoundException
	 * @throws InvalidUserException
	 * @throws InvalidChatRoomException
	 */
	public static void sendChatMessage(String message, User sender, User... users) throws UserNotFoundException, InvalidUserException, InvalidChatRoomException
	{
		sendChatMessage(new ChatMessage(sender, message), users);
	}

	/**
	 * Sends a message from one user to a list of users. This will create a
	 * chatroom if one doens't exist
	 * 
	 * @param message
	 *            - content of the message
	 * @param sender
	 *            -who is sending the message
	 * @param users
	 *            -to whom the message is being sent to
	 * @throws UserNotFoundException
	 * @throws InvalidUserException
	 * @throws InvalidChatRoomException
	 */
	public static void sendChatMessage(String message, String sender, List<String> users) throws UserNotFoundException, InvalidUserException, InvalidChatRoomException
	{
		User[] allUsers = new User[users.size()];
		for (int i = 0; i < users.size(); i++)
			allUsers[i] = new User(users.get(i));

		sendChatMessage(new ChatMessage(new User(sender), message), allUsers);
	}

	/**
	 * 
	 * @param msg
	 *            -ChatMessage contains the sender and the content of the
	 *            message
	 * @param users
	 *            -list of users that defines a chat room
	 * @throws UserNotFoundException
	 * @throws InvalidUserException
	 * @throws InvalidChatRoomException
	 */
	public static void sendChatMessage(ChatMessage msg, User... users) throws UserNotFoundException, InvalidUserException, InvalidChatRoomException
	{
		// add sender to the list
		ArrayList<String> allUsers = new ArrayList<String>();
		boolean gmAction = false;
		for (int i = 0; i < users.length; i++)
		{
			if (!UserManager.doesUserExist(users[i].getUsername()))
				throw new UserNotFoundException("The user '" + users[i] + "' does not exist.");

			allUsers.add(users[i].getUsername());

		}
		allUsers.add(msg.sender.getUsername());

		if (!gmAction)
			DBAccessor.sendQuery(buildAddMessageQuery(msg, createChatRoomName(allUsers)));
	}

	private static String[] buildAddMessageQuery(ChatMessage msg, String chatRoomName)
	{
		String q[] = new String[5];

		q[0] = "INSERT INTO " + CM_TABLE + " ( ";
		q[0] += CM_USERNAME + " , ";
		q[0] += CM_CHATROOM + " , ";
		q[0] += CM_TIME + " , ";
		q[0] += CM_MESSAGE + " ) ";

		q[0] += "VALUES ( ?,?,?,?)";
		q[1] = msg.getSender();
		q[2] = chatRoomName;
		q[3] = "" + msg.getTimestamp();
		q[4] = msg.getMessage();

		return q;
	}

	private static String[] buildGetMessageLogQuery(String chatRoomName)
	{
		String q[] = new String[2];
		q[0] = "SELECT * FROM " + CM_TABLE + " WHERE " + CM_CHATROOM + "=?";
		q[1] = chatRoomName;
		return q;
	}

	/**
	 * Retrieves a list of all messages in the chat room defined by the list of
	 * users and username
	 * 
	 * @param username
	 *            - username of requesting user
	 * @param users
	 *            -list of users (along with the username) that defines the chat
	 *            room
	 * @return a list of messages associated with the chatroom
	 * @throws InvalidChatRoomException
	 */
	public static List<ChatMessage> getMessageLog(String username, List<String> users) throws InvalidChatRoomException
	{
		return getMessageLog(createChatRoomName(username, users));
	}

	/**
	 * Returns all messages in the chat room after a certain date
	 * 
	 * @param username
	 *            username of requesting user
	 * @param users
	 *            list of Users (along with the username) that defines that chat
	 *            room
	 * @param afterTime
	 *            long time in milliseconds since January 1, 1970
	 * @return a list of chat messages after a specified time
	 * @throws InvalidChatRoomException
	 */
	public static List<ChatMessage> getMessageLog(String username, List<String> users, long afterTime) throws InvalidChatRoomException
	{
		return getMessageLog(createChatRoomName(username, users), afterTime);
	}

	/**
	 * Returns all message in the chat room with chat room id chatRoomName
	 * 
	 * @param chatRoomName
	 *            the name of the chat room
	 * @return all message in the chat room with chat room id chatRoomName
	 */
	public static List<ChatMessage> getMessageLog(String chatRoomName)
	{
		return getMessageLog(chatRoomName, 0);
	}

	/**
	 * Returns all message in the chat room with chat room id chatRoomName after
	 * the specified time
	 * 
	 * @param chatRoomName
	 *            the name of the chat room
	 * @param afterTime
	 *            long time in milliseconds since January 1, 1970
	 * @return all messages in the chat room with chat room id chatRoomName
	 *         after the specified time
	 */
	public static List<ChatMessage> getMessageLog(String chatRoomName, long afterTime)
	{

		ArrayList<ChatMessage> ret = new ArrayList<ChatMessage>();

		List<Map<String, Object>> res = DBAccessor.sendQuery(buildGetMessageLogQuery(chatRoomName));
		if (res != null)
		{
			for (int i = 0; i < res.size() - 1; i++)
			{
				long time = (Long) res.get(i).get(CM_TIME);
				String sender = res.get(i).get(CM_USERNAME).toString();
				String msgText = res.get(i).get(CM_MESSAGE).toString();

				ChatMessage msg = new ChatMessage(new User(sender), msgText);
				msg.timestamp = time;
				if (time >= afterTime)
					ret.add(msg);

			}
		}
		return ret;
	}

	private static String[] buildGetAssociatedChatRoomsList(String username)
	{
		String q[] = new String[4];
		q[0] = "SELECT DISTINCT " + CM_CHATROOM + " FROM " + CM_TABLE + " WHERE ";
		q[0] += CM_CHATROOM + " LIKE ? OR " + CM_CHATROOM + " LIKE ? OR " + CM_CHATROOM + " LIKE ? ";
		q[1] = username + ";%";
		q[2] = "%;" + username + ";%";
		q[3] = "%;" + username;
		return q;
	}

	/**
	 * returns all chat room names the user with username belongs too
	 * 
	 * @param username
	 *            the requesting user
	 * @return all chat room names the user with username belongs too
	 */
	public static List<String> getAssociatedChatRooms(String username)
	{
		ArrayList<String> ret = new ArrayList<String>();
		List<Map<String, Object>> results = DBAccessor.sendQuery(buildGetAssociatedChatRoomsList(username));

		if (results != null)
			for (int i = 0; i < results.size() - 1; i++)
				ret.add(results.get(i).get(CM_CHATROOM).toString());

		return ret;
	}
}
