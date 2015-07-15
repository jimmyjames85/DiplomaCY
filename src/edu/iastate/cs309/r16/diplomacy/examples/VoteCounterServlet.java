package edu.iastate.cs309.r16.diplomacy.examples;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VoteCounterServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	

	private static long lastTime = 0;
	private static int upVote = 0;
	private static int downVote = 0;
	private static Random rand;

	public VoteCounterServlet()
	{
		lastTime = System.currentTimeMillis();
		rand = new Random(lastTime);
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		//create a random vote 
		boolean voteUp = rand.nextBoolean();

		
		//NOTICE!! that this is text/event-stream instead of text/html
		//This is required for server sent events

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		ServletOutputStream os = response.getOutputStream();

		//we either sent an up_event with a total or a down_event with a total		
		if (voteUp)
		{
			upVote++;
			os.print("event: up_event\n");
			os.print("data: " + upVote + "\n\n");
		}
		else
		{
			downVote++;
			os.print("event: down_event\n");
			os.print("data: " + downVote + "\n\n");
		}

		
		//we send a separate event, total_votes with a total
		os.print("event: total_votes\n");
		os.print("data: " + (upVote + downVote) + "\n\n");
		
		os.close();

	}
}
