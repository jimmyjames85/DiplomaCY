package edu.iastate.cs309.r16.diplomacy.users;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.TreeSet;

import com.google.gson.stream.JsonWriter;

import edu.iastate.cs309.r16.diplomacy.games.GameManager;
import edu.iastate.cs309.r16.diplomacy.users.session.SessionId;

public class User implements Comparable<User>
{
	private static final int MIN_PASSWORD_LENGTH = 3;
	private static final int MIN_USERNAME_LENGTH = 3;

	/**
	 * temporary unique backdoor username GAMEMANAGER allows the user to submit
	 * game modes via the chat service
	 */
	public static final String GAMEMANAGER = "GM";

	/**
	 * Base Line valid characters
	 */
	private static final String VALID_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static final String VALID_CHARS_USERNAME = VALID_CHARS;
	private static final String VALID_CHARS_PASSWORD = VALID_CHARS;

	/**
	 * Emails can have .@_-
	 */
	private static final String VALID_CHARS_EMAIL = VALID_CHARS + ".@_-";

	/**
	 * First names can have .- and a space
	 */
	private static final String VALID_CHARS_FIRSTNAME = VALID_CHARS + " .-";

	/**
	 * Last names can have .- and a space
	 */
	private static final String VALID_CHARS_LASTNAME = VALID_CHARS_FIRSTNAME + " .-";

	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private SessionId sid;

	public User()
	{
	}

	public User(String username)
	{
		setUsername(username);
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public SessionId getSessionId()
	{
		return sid;
	}

	public void setSessionId(SessionId sid)
	{
		this.sid = sid;
	}

	@Override
	public String toString()
	{
		return username;
	}

	/*
	 * public String toHtmlTableStringWithPassword() { String ret =
	 * "<table border=1>"; ret +=
	 * "<tr><td>username</td><td>password</td><td>firstName</td><td>lastName</td><td>email</td></tr>"
	 * ; ret += "<tr><td>" + username + "</td><td>" + password + "</td><td>" +
	 * firstName + "</td><td>" + lastName + "</td><td>" + email + "</td></tr>";
	 * ret += "</table>"; return ret; }
	 */

	/**
	 * This method is userful for sending the user information to the web client
	 * 
	 * @return a json string of the user
	 */
	public String toJSONString()
	{

		StringWriter out = new StringWriter();
		JsonWriter json = new JsonWriter(out);

		try
		{
			json.beginObject();
			json.name("username").value(username);
			json.name("password").value(password);
			json.name("firstName").value(firstName);
			json.name("lastName").value(lastName);
			json.name("email").value(email);
			if (sid != null)
			{
				json.name("sessionId").value(sid.toString());
			}

			List<Integer> gameIds = UserManager.getUserGames(this);

			json.name("gameIds");
			json.beginArray();
			for (int gameId : gameIds)
				json.value(gameId);
			json.endArray();

			List<Integer> ownedGames = UserManager.getUserOwnedGames(this);

			json.name("ownedGameIds");
			json.beginArray();
			for (int gameId : ownedGames)
				json.value(gameId);
			json.endArray();

			List<String> friends = UserManager.getFriendList(username);
			if (friends.size() > 0)
			{
				json.name("friends");
				json.beginArray();
				for (String friend : friends)
					json.value(friend);
				json.endArray();
			}

			for (int gameId : gameIds)
			{
				String country = GameManager.getCountry(username, gameId);
				if (country != null && country.length() > 0)
					json.name("country" + gameId).value(country);
			}

			json.name("jsontype").value("user");

			json.endObject();
			json.close();
		}
		catch (IOException e)
		{
			// TODO  log
			e.printStackTrace();
		}

		return out.toString();

	}

	/**
	 * This method returns if the username of this user is valid. I.e. the
	 * username length is sufficient and there are no invalid characters.
	 * Otherwise an InvalidUserException is thrown.
	 * 
	 * @throws InvalidUserException
	 */
	public void validateUsername() throws InvalidUserException
	{
		if (username.length() < MIN_USERNAME_LENGTH)
			throw new InvalidUserException("Username must be " + MIN_USERNAME_LENGTH + " characters long");
		if (!validateCharacters(username, VALID_CHARS_USERNAME))
			throw new InvalidUserException("Invalid characters in username.");
		if (username.equalsIgnoreCase(GAMEMANAGER))
			throw new InvalidUserException(GAMEMANAGER + " cannot be a player.");

	}

	/**
	 * This method returns if the password of this user is valid. I.e. the
	 * password length is sufficient and there are no invalid characters.
	 * Otherwise an InvalidUserException is thrown
	 * 
	 * @throws InvalidUserException
	 */
	public void validatePassword() throws InvalidUserException
	{
		if (password.length() < MIN_PASSWORD_LENGTH)
			throw new InvalidUserException("Password must be " + MIN_PASSWORD_LENGTH + " characters long");
		if (!validateCharacters(password, VALID_CHARS_PASSWORD))
			throw new InvalidUserException("Invalid characters in password.");
	}

	/**
	 * This method returns if validatePassword() and validateUsername() returns
	 * and if this user's first name, last name and email don't have invalid
	 * characters. Otherwise an InvalidUserException is thrown.
	 * 
	 * @throws InvalidUserException
	 */
	public void validateUser() throws InvalidUserException
	{
		validateUsername();
		validatePassword();

		if (!validateCharacters(firstName, VALID_CHARS_FIRSTNAME))
			throw new InvalidUserException("Invalid characters in first name.");
		if (!validateCharacters(lastName, VALID_CHARS_LASTNAME))
			throw new InvalidUserException("Invalid characters in last name.");
		if (!validateCharacters(email, VALID_CHARS_EMAIL))
			throw new InvalidUserException("Invalid characters in email.");

	}

	private boolean validateCharacters(String check, String valid)
	{
		TreeSet<Character> setCheck = new TreeSet<Character>();
		TreeSet<Character> setValid = new TreeSet<Character>();

		char[] listA = check.toCharArray();
		for (int i = 0; i < listA.length; i++)
			setCheck.add(listA[i]);

		char[] listB = valid.toCharArray();
		for (int i = 0; i < listB.length; i++)
			setValid.add(listB[i]);
		
		return setValid.containsAll(setCheck);
	}

	@Override
	public int compareTo(User o)
	{
		return username.compareTo(o.username);
	}

	@Override
	public boolean equals(Object other)
	{
		boolean ret = false;
		if (other.getClass().equals(User.class))
			ret = this.compareTo((User) other) == 0;

		return ret;
	}

}
