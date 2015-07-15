package edu.iastate.cs309.r16.diplomacy.users;

public class InvalidPasswordException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public InvalidPasswordException()
	{
	}

	public InvalidPasswordException(String message)
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
