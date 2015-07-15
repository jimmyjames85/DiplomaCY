package edu.iastate.cs309.r16.diplomacy.users;

public class InvalidUserException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public InvalidUserException()
	{
	}

	public InvalidUserException(String message)
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
