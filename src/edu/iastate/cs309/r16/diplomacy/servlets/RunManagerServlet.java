package edu.iastate.cs309.r16.diplomacy.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.cs309.r16.diplomacy.games.GameManager;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.moves.Action;
import edu.iastate.cs309.r16.diplomacy.moves.ActionReader;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;

public class RunManagerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		String sgameId = request.getHeader("gameId");
		if (sgameId != null)
		{
			Long gameId = Long.parseLong(sgameId);
			GameManager.triggerProcessTurn(gameId);
			//GameManager.proccessTurn(gameId);
		}

	}

}
