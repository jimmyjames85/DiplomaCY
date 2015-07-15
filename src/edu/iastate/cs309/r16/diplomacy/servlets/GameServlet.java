package edu.iastate.cs309.r16.diplomacy.servlets;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import edu.iastate.cs309.r16.diplomacy.chat.ChatManager;
import edu.iastate.cs309.r16.diplomacy.games.GameManager;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.GameMapSerializer;
import edu.iastate.cs309.r16.diplomacy.map.Territory;
import edu.iastate.cs309.r16.diplomacy.map.TerritorySerializer;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;

public class GameServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String sessionId = request.getHeader("sessionId");
		String sGameId = request.getHeader("gameId");
		String username = GameManager.getUserName(sessionId);
		Integer gameId = null;
		try
		{
			gameId = Integer.parseInt(sGameId);
		}
		catch (Exception e)
		{
		}

		if (Boolean.parseBoolean(request.getHeader("getBoard")) && sessionId != null && gameId != null)
		{

			int currentStateNumber = GameManager.getCurrentStateNumber((gameId));
			String map = GameManager.getJsonGameMapForStateId((gameId), currentStateNumber);
			response.getOutputStream().write(map.getBytes());
		}
		else if (Boolean.parseBoolean(request.getHeader("getCountry")) && sessionId != null && gameId != null)
		{
			Writer responseWriter = response.getWriter();
			responseWriter.write(GameManager.getCountry(username, (gameId)));
		}
		else if (Boolean.parseBoolean(request.getHeader("getState")) && request.getHeader("stateNumber") != null && sessionId != null && gameId != null)
		{
			int sNum = Integer.parseInt(request.getHeader("stateNumber"));
			GameManager.getGameMapForStateId(gameId, sNum);
		}
		else if (Boolean.parseBoolean(request.getHeader("getCurrentState")) && gameId != null && sessionId != null)
		{
			Date nextTriggerDate = GameManager.getNextTrigger(gameId);
			int stateNumber = GameManager.getCurrentStateNumber((gameId));

			StringWriter out = new StringWriter();
			JsonWriter json = new JsonWriter(out);
			
			/*			
			String jsonList = GameManager.getUserList(gameId).replaceAll("\\{","");//
			jsonList = jsonList.replaceAll("\\}", "");
			String userList[] = jsonList.split(",");
			
			ArrayList<String> uList = new ArrayList<String>();
			for(int i=0;i<userList.length;i++)
				uList.add(userList[i].split(":")[1].replaceAll("\"",""));
			*/

			String boardType = GameManager.getBoardType(gameId);

			try
			{
				json.beginObject();
				json.name("stateNumber").value(stateNumber);
				json.name("boardType").value(boardType);
				if(nextTriggerDate!=null)
				json.name("nextTrigger").value(nextTriggerDate.getTime());
				json.endObject();
				json.close();
			}
			catch (IOException e)
			{
			}
			response.getWriter().write(out.toString());
		}
		else if (Boolean.parseBoolean(request.getHeader("getUserList")) && gameId != null)
		{
			response.getWriter().write(GameManager.getUserList(gameId));
		}
		else if (Boolean.parseBoolean(request.getHeader("getNextTrigger")) && gameId != null)
		{
			response.getWriter().write("{\"nextTrigger\":" + GameManager.getNextTrigger(gameId).getTime() + "}");
		}

	}
}