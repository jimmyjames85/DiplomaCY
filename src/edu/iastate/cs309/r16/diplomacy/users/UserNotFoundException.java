package edu.iastate.cs309.r16.diplomacy.users;

public class UserNotFoundException extends Exception
{
	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public UserNotFoundException()
	{
	}

	public UserNotFoundException(String message)
	{
		this.errorMessage = message;
	}

	@Override
	public String getMessage()
	{
		return errorMessage;
	}

	@Override
	public String getLocalizedMessage()
	{
		return errorMessage;
	}

}
