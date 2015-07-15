package edu.iastate.cs309.r16.diplomacy.users.session;

public class SessionId
{
	private final String id;

	public SessionId(String id)
	{
		if (id == null)
			throw new IllegalArgumentException("id cannot be null!");
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return id;
	}

	@Override
	public boolean equals(Object other)
	{
		return other!=null && id.equals(other.toString());
	}
}
