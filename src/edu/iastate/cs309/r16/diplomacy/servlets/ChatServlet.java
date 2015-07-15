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

import com.google.gson.stream.JsonWriter;

import edu.iastate.cs309.r16.diplomacy.chat.ChatManager;
import edu.iastate.cs309.r16.diplomacy.chat.ChatMessage;
import edu.iastate.cs309.r16.diplomacy.chat.InvalidChatRoomException;
import edu.iastate.cs309.r16.diplomacy.users.InvalidUserException;
import edu.iastate.cs309.r16.diplomacy.users.UserNotFoundException;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;
import edu.iastate.cs309.r16.diplomacy.util.Logger;

public class ChatServlet extends HttpServlet
{
	public static final String ACTION_SEND_MESSAGE = "SENDM";
	public static final String ACTION_GET_CHAT_LOG = "GETL";
	public static final String ACTION_GET_CHAT_LOG_BYTIME = "GETLT";
	public static final String ACTION_GET_CHAT_ROOMS = "GETRM";
	public static final String ACTION_GETMAP = "GETMAP";

	//parameters
	public static final String ACTION = "action";
	public static final String USERNAME = "username";
	public static final String USER = "user";
	public static final String MESSAGE = "message";
	public static final String TIMESTAMP = "timestamp";

	@SuppressWarnings("unused")
	private static Logger log = new Logger(ChatServlet.class);
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getOutputStream().println("Unsupported request method");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ServletOutputStream os = response.getOutputStream();
		Map<String, String[]> parmMap = request.getParameterMap();

		Set<String> parms = parmMap.keySet();
		String action = "";
		ArrayList<String> userList = new ArrayList<String>();
		String username = "";
		String msg = "";
		String timestamp = "";

		Iterator<String> itr = parms.iterator();
		while (itr.hasNext())
		{
			String curParm = itr.next();
			String[] curVal = parmMap.get(curParm);

			if (curParm.equalsIgnoreCase(ACTION))
				action = curVal[0];
			else if (curParm.equalsIgnoreCase(USER))
			{
				for (int i = 0; i < curVal.length; i++)
					userList.add(curVal[i]);
			}
			else if (curParm.equalsIgnoreCase(USERNAME))
				username = curVal[0];
			else if (curParm.equalsIgnoreCase(MESSAGE))
				msg = curVal[0];
			else if (curParm.equalsIgnoreCase(TIMESTAMP))
				timestamp = curVal[0];

		}

		os.println(handleRequest(action, username, userList, msg, timestamp));
	}

	private String handleRequest(String action, String username, ArrayList<String> userList, String msg, String timestamp) throws IOException
	{
		String ret = "";

		if (ACTION_GET_CHAT_LOG.equalsIgnoreCase(action))
		{
			try
			{
				ret = chatLogToJson(ChatManager.getMessageLog(username, userList));

			}
			catch (InvalidChatRoomException e)
			{
				ret = errorToJson(e);
			}
		}
		else if (ACTION_GETMAP.equalsIgnoreCase(action))
		{
			try
			{
				int mapId = Integer.parseInt(msg);
				String q[] = new String[2];
				q[0] = "SELECT * FROM gameMaps WHERE id=?";
				q[1] = "" + mapId;
				List<Map<String, Object>> result = DBAccessor.sendQuery(q);
				
				if(result!=null)
					ret=result.get(0).get("map").toString();
			}
			catch (Exception e)
			{
				ret = errorToJson(e);
			}
		}
		else if (ACTION_GET_CHAT_LOG_BYTIME.equalsIgnoreCase(action) && !timestamp.equals(""))
		{
			Long time = null;
			try
			{
				if (timestamp != null)
					time = Long.parseLong(timestamp);
			}
			catch (NumberFormatException e)
			{
			}
			if (time != null)
			{
				try
				{
					ret = chatLogToJson(ChatManager.getMessageLog(username, userList, time));
				}
				catch (InvalidChatRoomException e)
				{
					ret = errorToJson(e);
				}
			}

		}
		else if (ACTION_SEND_MESSAGE.equalsIgnoreCase(action))
		{
			try
			{
				if (msg != null && msg.length() > 0)
					ChatManager.sendChatMessage(msg, username, userList);
			}
			catch (UserNotFoundException | InvalidUserException | InvalidChatRoomException e)
			{
				ret = errorToJson(e);
			}
		}
		else if (ACTION_GET_CHAT_ROOMS.equalsIgnoreCase(action))
			ret = chatRoomListToJson(ChatManager.getAssociatedChatRooms(username));

		return ret;
	}

	private static String errorToJson(Exception e) throws IOException
	{
		StringWriter out = new StringWriter();
		JsonWriter json = new JsonWriter(out);
		json.beginObject();
		json.name("jsontype").value("error");
		json.name("message").value(e.getMessage());
		String stackTraceStr = Logger.stackTraceToString(e);
		json.name("stacktrace").value(stackTraceStr);
		json.endObject();
		json.close();

		return out.toString();
	}

	//select * from chat_messages where chatRoom like '%.jim' or chatRoom like '%.jim.%' or chatRoom like 'jim.%';
	private static String chatRoomListToJson(List<String> roomList) throws IOException
	{
		StringWriter out = new StringWriter();

		JsonWriter json = new JsonWriter(out);
		json.beginObject();
		json.name("chatRooms");
		json.beginArray();
		for (String room : roomList)
		{
			json.value(room);
		}
		json.endArray();
		json.name("jsontype").value("chatRoomList");
		json.endObject();
		json.close();
		return out.toString();
	}

	private static String chatLogToJson(List<ChatMessage> chatLog) throws IOException
	{
		StringWriter out = new StringWriter();

		JsonWriter json = new JsonWriter(out);
		json.beginObject();
		json.name("log");
		json.beginArray();
		for (ChatMessage msg : chatLog)
		{
			json.beginObject();
			json.name("sender").value(msg.getSender());
			json.name("timestamp").value(msg.getTimestamp());
			json.name("message").value(msg.getMessage());
			json.endObject();
		}
		json.endArray();
		json.name("jsontype").value("chatlog");
		json.endObject();
		json.close();

		return out.toString();
	}
}
