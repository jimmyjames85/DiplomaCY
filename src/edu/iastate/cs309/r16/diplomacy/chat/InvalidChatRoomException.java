package edu.iastate.cs309.r16.diplomacy.chat;

public class InvalidChatRoomException extends Exception
{
	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public InvalidChatRoomException()
	{
	}

	public InvalidChatRoomException(String message)
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
