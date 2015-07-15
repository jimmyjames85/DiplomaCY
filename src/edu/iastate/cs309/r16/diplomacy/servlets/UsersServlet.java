package edu.iastate.cs309.r16.diplomacy.servlets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import edu.iastate.cs309.r16.diplomacy.users.session.SessionId;
import edu.iastate.cs309.r16.diplomacy.users.session.SessionManager;
import edu.iastate.cs309.r16.diplomacy.users.InvalidPasswordException;
import edu.iastate.cs309.r16.diplomacy.users.InvalidUserException;
import edu.iastate.cs309.r16.diplomacy.users.User;
import edu.iastate.cs309.r16.diplomacy.users.UserManager;
import edu.iastate.cs309.r16.diplomacy.users.UserNotFoundException;

public class UsersServlet extends HttpServlet
{
	public static final String ACTION_ADD = "ADD";
	public static final String ACTION_GET = "GET";
	public static final String ACTION_UPDATE = "UPDATE";
	public static final String ACTION_CHANGE_PASSWORD = "PASSWORD";
	public static final String ACTION_LOGIN = "LOGIN";
	public static final String ACTION_LOGOUT = "LOGOUT";
	public static final String ACTION_GET_VIA_SESSIONID = "GETS";
	public static final String ACTION_USER_EXIST = "PINGUSER";
	public static final String ACTION_GET_USER_GAMES = "GETGAMES";
	public static final String ACTION_ADD_FRIEND = "ADDFRIEND";
	public static final String ACTION_DROP_FRIEND = "DROPFRIEND";
	
	public static final String ACTION = "action";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email";
	public static final String NEW_PASSWORD = "newPassword";
	public static final String SESSIONID = "sid";

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getOutputStream().println("Unsupported request method");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User incomingUser = new User();
		ServletOutputStream os = response.getOutputStream();
		Map<String, String[]> parmMap = request.getParameterMap();

		Set<String> parms = parmMap.keySet();
		String action = "";

		Iterator<String> itr = parms.iterator();
		while (itr.hasNext())
		{
			String curParm = itr.next();
			String curVal = parmMap.get(curParm)[0];

			if (curParm.equals(USERNAME))
				incomingUser.setUsername(curVal);
			else if (curParm.equals(PASSWORD))
				incomingUser.setPassword(curVal);
			else if (curParm.equals(EMAIL))
				incomingUser.setEmail(curVal);
			else if (curParm.equals(FIRST_NAME))
				incomingUser.setFirstName(curVal);
			else if (curParm.equals(LAST_NAME))
				incomingUser.setLastName(curVal);
			else if (curParm.equals(ACTION))
				action = curVal;
			else if (curParm.equals(SESSIONID))
				incomingUser.setSessionId(new SessionId(curVal));
		}

		String jOut = null;

		try
		{
			String username = incomingUser.getUsername();
			String password = incomingUser.getPassword();

			if (ACTION_ADD.equals(action))
			{
				UserManager.addUser(incomingUser);
				// if no exception return new user
				jOut = UserManager.getUser(username, password).toJSONString();
			}
			else if (ACTION_GET.equals(action))
			{
				jOut = UserManager.getUser(username, password).toJSONString();
			}
			else if (ACTION_UPDATE.equals(action))
			{
				UserManager.updateUser(incomingUser);
				jOut = UserManager.getUser(username, password).toJSONString();
			}
			else if (ACTION_CHANGE_PASSWORD.equals(action))
			{
				String newPassword = request.getParameter(NEW_PASSWORD);
				UserManager.changeUserPassword(incomingUser.getUsername(), incomingUser.getPassword(), newPassword);
				jOut = UserManager.getUser(incomingUser.getUsername(), newPassword).toJSONString();
			}
			else if (ACTION_LOGIN.equals(action))
			{
				logout(username);
				jOut = login(username, password).toJSONString();
			}
			else if (ACTION_LOGOUT.equals(action))
			{
				incomingUser = UserManager.getUser(incomingUser.getSessionId());
				logout(incomingUser.getUsername());
				jOut = "";
			}
			else if (ACTION_GET_VIA_SESSIONID.equals(action))
			{
				String sid = request.getParameter(SESSIONID);
				jOut = UserManager.getUser(new SessionId(sid)).toJSONString();
			}
			else if (ACTION_USER_EXIST.equals(action))
			{
				boolean userExists = UserManager.doesUserExist(username);
				StringWriter out = new StringWriter();
				JsonWriter json = new JsonWriter(out);
				json.beginObject();
				json.name("jsontype").value("userExists");
				json.name("exists").value(userExists);
				json.close();
				jOut = out.toString();
			}
			else if (ACTION_GET_USER_GAMES.equals(action))
			{
				String sid = request.getParameter(SESSIONID);
				incomingUser = UserManager.getUser(new SessionId(sid));
				jOut = new Gson().toJson(UserManager.getUserGames(incomingUser));

			}
			else if (request.getHeader("action").equals(ACTION_ADD_FRIEND))
			{
				String sid = request.getHeader("sessionId");
				String newFriend = request.getHeader("newFriend");
				
				if (sid != null)
					incomingUser = UserManager.getUser(new SessionId(sid));

				boolean success = false;
				if (newFriend != null && incomingUser != null)
					success = UserManager.addFriend(incomingUser.getUsername(), newFriend);

				jOut = "{\"success\":" + success + "}";

			}
			else if (request.getHeader("action").equals(ACTION_DROP_FRIEND))
			{
				String sid = request.getHeader("sessionId");
				String exFriend = request.getHeader("exFriend");
				
				if (sid != null)
					incomingUser = UserManager.getUser(new SessionId(sid));

				boolean success = false;
				if (exFriend != null && incomingUser != null)
					success = UserManager.removeFriend(incomingUser.getUsername(), exFriend);

				jOut = "{\"success\":" + success + "}";

			}			
			else
			{
				String err = "Unsupported action query : " + action;
				throw new UnsupportedOperationException(err);
			}

		}
		catch (Exception e)
		{
			StringWriter out = new StringWriter();
			JsonWriter json = new JsonWriter(out);
			json.beginObject();
			json.name("jsontype").value("error");
			json.name("message").value(e.getMessage());
			String stackTraceStr = "";
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (int i = 0; i < stackTrace.length; i++)
			{
				StackTraceElement cur = stackTrace[i];
				stackTraceStr += cur.toString() + "\n";

			}
			json.name("stacktrace").value(stackTraceStr);
			json.endObject();
			json.close();

			jOut = out.toString();
		}
		os.print(jOut);
	}

	// get the user and set/reset the sessionId
	private User login(String username, String password) throws UserNotFoundException, InvalidPasswordException, InvalidUserException
	{
		User user = UserManager.getUser(username, password);
		SessionId sid = SessionManager.createSessionId();

		if (SessionManager.setSessionId(username, password, sid))
			user.setSessionId(sid);

		return user;
	}

	private void logout(String username) throws UserNotFoundException, InvalidPasswordException, InvalidUserException
	{
		SessionManager.dropSessionId(username);
		//User user = UserManager.getUser(username);
		//return user;
	}

}
