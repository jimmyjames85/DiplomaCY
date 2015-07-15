package edu.iastate.cs309.r16.diplomacy.servlets;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.stream.JsonWriter;

import edu.iastate.cs309.r16.diplomacy.games.GameManager;
import edu.iastate.cs309.r16.diplomacy.users.UserManager;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;

public class MovesServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String sessionId = request.getHeader("sessionId");
		String gameId = request.getHeader("gameId");
		if (Boolean.parseBoolean(request.getHeader("postMoves")) && sessionId != null && gameId != null)
		{
			String username = GameManager.getUserName(sessionId);
			String country = GameManager.getCountry(username, Long.parseLong(gameId));
			if (country != null)
			{
				ServletInputStream is = request.getInputStream();

				ObjectMapper mapper = new ObjectMapper();
				List<String> moves = mapper.readValue(is, List.class);

				int stateNumber = GameManager.getCurrentStateNumber(Long.parseLong(gameId));

				for (String move : moves)
				{
					GameManager.submitMoveOrder(country, move, Integer.parseInt(gameId), stateNumber);
					//make query work to post the move - I think it works now!!
				}
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String sessionId = request.getHeader("sessionId");
		String sgameId = request.getHeader("gameId");
		String sstateNumber = request.getHeader("stateNumber");

		if (sgameId == null || sessionId == null)
			return;

		int gameId = Integer.parseInt(sgameId);
		String username = GameManager.getUserName(sessionId);
		String userCountry = GameManager.getCountry(username, gameId);

		Integer stateNumber = null;
		try
		{
			stateNumber = Integer.parseInt(sstateNumber);
		}
		catch (NumberFormatException e)
		{
		}

		Writer out = response.getWriter();

		if (Boolean.parseBoolean(request.getHeader("getPendingMoves")))
			out.write(movesToJson(GameManager.getPendingMoves(gameId), userCountry));
		if (Boolean.parseBoolean(request.getHeader("getSubmitStatus")))
			out.write(submitStatusToJson(GameManager.getSubmitMoveStatus(gameId)));
		if (Boolean.parseBoolean(request.getHeader("getLastMoves")) && stateNumber != null)
			out.write(getLastMovesToJson(GameManager.getSubmittedMovesForStatenumber(gameId, stateNumber)));
	}

	protected String getLastMovesToJson(List<Map<String, Object>> lastMoves)
	{
		ArrayList<String[]> moves = new ArrayList<String[]>();
		if (lastMoves != null)
			for (Map<String, Object> row : lastMoves)
			{
				String status = row.get("evaluation").toString();
				String move = row.get("moveText").toString();
				if (!move.equals("VARCHAR"))
				{
					String arr[] = { move, status };
					moves.add(arr);
				}
			}

		StringWriter out = new StringWriter();
		JsonWriter json = new JsonWriter(out);

		try
		{
			json.beginObject();
			json.name("lastMoves");
			json.beginArray();
			for (String[] move : moves)
			{
				json.beginArray();
				json.value(move[0]);
				json.value(move[1]);
				json.endArray();
			}
			json.endArray();
			json.endObject();
			json.close();
		}
		catch (IOException e)
		{
		}
		return out.toString();
	}

	protected String submitStatusToJson(List<Map<String, Object>> submitStatus)
	{

		ArrayList<String> countries = new ArrayList<String>();
		if (submitStatus != null)
			for (Map<String, Object> row : submitStatus)
			{
				String country = row.get("country").toString();
				if (!country.equals("VARCHAR"))
					countries.add(country);
			}

		StringWriter out = new StringWriter();
		JsonWriter json = new JsonWriter(out);

		try
		{
			json.beginObject();
			json.name("countries");
			json.beginArray();
			for (String country : countries)
				json.value(country);
			json.endArray();
			json.endObject();
			json.close();
		}
		catch (IOException e)
		{
		}
		return out.toString();
	}

	protected String movesToJson(List<Map<String, Object>> moves, String userCountry)
	{
		StringWriter out = new StringWriter();
		JsonWriter json = new JsonWriter(out);

		ArrayList<String> orders = new ArrayList<String>();
		if (moves != null)
			for (Map<String, Object> row : moves)
			{
				if (row.get("country").equals(userCountry))
					orders.add((row.get("moveText").toString()));
			}

		try
		{
			json.beginObject();
			json.name("pendingMoves");
			json.beginArray();
			for (String order : orders)
				json.value(order);
			json.endArray();
			json.endObject();
			json.close();
		}
		catch (IOException e)
		{
		}
		return out.toString();
	}

}
