package edu.iastate.cs309.r16.diplomacy.users.session;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import edu.iastate.cs309.r16.diplomacy.users.InvalidPasswordException;
import edu.iastate.cs309.r16.diplomacy.users.InvalidUserException;
import edu.iastate.cs309.r16.diplomacy.users.User;
import edu.iastate.cs309.r16.diplomacy.users.UserManager;
import edu.iastate.cs309.r16.diplomacy.users.UserNotFoundException;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;

public class SessionManager
{
	private static String SESSIONS_USERNAME = "username";
	private static String SESSIONS_TABLE = "sessions";
	private static String SESSIONS_SESSION_ID = "sessionId";

	// private static String ALPHABET =
	// "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public static SessionId createSessionId()
	{
		SecureRandom rand = new SecureRandom();
		rand.setSeed(System.currentTimeMillis());
		long longA = Math.abs(rand.nextLong());
		long longB = Math.abs(rand.nextLong());
		long time = System.currentTimeMillis();
		String id = longA + "" + longB + "" + time;
		return new SessionId(id);
	}

	protected static String buildDeleteSessionIdQuery(String username)
	{
		String query = "DELETE FROM " + SESSIONS_TABLE + " WHERE ";
		query += SESSIONS_USERNAME + "='" + username + "'";
		return query;
	}

	protected static String[] buildSetSessionIdQuery(String username, SessionId sid)
	{
		String q[] = new String[4];
		q[0]= "UPDATE " + SESSIONS_TABLE + " SET " + SESSIONS_USERNAME + "=?," + SESSIONS_SESSION_ID + "=? ";
		q[0]+= "WHERE " + SESSIONS_USERNAME + "=?";
		q[1] = username;
		q[2] = sid.getId();
		q[3] = username;
		return q;
	}

	protected static String[] buildInsertSessionIdQuery(String username, SessionId sid)
	{
		String q[] = new String[3];
		q[0] = "INSERT INTO " + SESSIONS_TABLE + " (" + SESSIONS_USERNAME + "," + SESSIONS_SESSION_ID + ") ";
		q[0] += "VALUES( ?,?)";
		q[1] = username;
		q[2] = sid.getId();
		return q;
	}

	public static void dropSessionId(String username)
	{
		DBAccessor.sendQuery(buildDeleteSessionIdQuery(username));
	}

	public static boolean setSessionId(String username, String password, SessionId sid) throws UserNotFoundException, InvalidPasswordException, InvalidUserException
	{
		UserManager.getUser(username, password);// validate password
		Boolean success = false;

		// neither insert nor update will return a resultset
		String[] queryInsert = buildInsertSessionIdQuery(username, sid);
		String[] querySet = buildSetSessionIdQuery(username, sid);

		// We try insert first
		DBAccessor.sendQuery(queryInsert);
		success = sid.equals(getNewSessionId(username, password));

		// This may not work in the case an sid already exists for user
		// so we check it
		if (!success)
		{
			// We then try setting it (i.e. changing it)
			DBAccessor.sendQuery(querySet);
			success = sid.equals(getNewSessionId(username, password));
		}

		return success;
	}

	protected static String[] buildGetSessionIdQuery(String username)
	{
		String q[] = new String[2];
		
		q[0]= "SELECT * FROM " + SESSIONS_TABLE + " WHERE ";
		q[0]+= SESSIONS_USERNAME + "=?";
		q[1] = username;
		return q;
	}

	protected static String[] buildGetSessionIdQuery(SessionId sid)
	{
		String q[] = new String[2];
		q[0]= "SELECT * FROM " + SESSIONS_TABLE + " WHERE ";
		q[0]= SESSIONS_SESSION_ID + "=?";
		q[1] = sid.toString();
		return q;
	}

	public static boolean sessionIdExists(SessionId sid)
	{
		List<Map<String, Object>> results = DBAccessor.sendQuery(buildGetSessionIdQuery(sid));
		return (results != null) && sid.equals(results.get(0).get(SESSIONS_SESSION_ID));
	}

	public static SessionId getNewSessionId(String username, String password) throws InvalidPasswordException, UserNotFoundException, InvalidUserException
	{
		UserManager.getUser(username, password);// validate password
		return getNewSessionId(username);
	}

	protected static SessionId getNewSessionId(String username) throws InvalidPasswordException, UserNotFoundException, InvalidUserException
	{
		String query[] = buildGetSessionIdQuery(username);
		SessionId ret = null;

		String sessionId = null;
		List<Map<String, Object>> results = DBAccessor.sendQuery(query);
		if (results != null && results.size() > 1)
			sessionId = results.get(0).get(SESSIONS_SESSION_ID).toString();

		if (sessionId != null)
			ret = new SessionId(sessionId);

		return ret;
	}
	
	public static SessionId getNewSessionId(User user) throws InvalidPasswordException, UserNotFoundException, InvalidUserException
	{
		return getNewSessionId(user.getUsername(), user.getPassword());
	}
}
