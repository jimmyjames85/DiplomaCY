package edu.iastate.cs309.r16.diplomacy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class DBAccessor
{
	public static final String FS = System.getProperty("file.separator");
	private static final String PROPERTY_FILE_LOC = ".." + FS + "database.properties"; //This should be in the WEB-INF folder
	private static final String PROPERTY_SERVER = "server";
	private static final String PROPERTY_DATABASE = "database";
	private static final String PROPERTY_USERNAME = "username";
	private static final String PROPERTY_PASSWORD = "password";
	private static final String PROPERTY_DRIVER = "driver";

	private static Logger LOG = new Logger(DBAccessor.class);

	private DBAccessor()
	{// hide the default constructor
	}

	//@formatter:off

	/**
	 *
	 * Sends a query to the database and returns the results as a list of
	 * hashmaps. If there is a connection failure than this method returns
	 * null
	 *
	 * THIS METHOD AUTO-COMMITS!!
	 *
	 *
	 *
	 * @param query
	 *            The first argument should be the actual query to be prepared
	 *
	 *            For every n'th '?' in the first argument, this method replaces
	 *            the ? with the n'th argument
	 *
	 *            Example:
	 *
	 *            sendQuery(
	 *            "SELECT * FROM users WHERE username = ? AND password = ?" ,
	 *            "user1", "pass1");
	 *
	 *            would be equivalent to
	 *
	 *            sendQuery(
	 *            "SELECT * FROM users WHERE username = 'user1' AND password = 'pass1'"
	 *            )
	 *
	 *            However, the latter is susceptible to sql injection.
	 *
	 *
	 * @return a List of Map's where for each Map the keys are the
	 *         column names. The very last Map in the List the
	 *         Objects are the DataTypes of the Columns. Because of this the
	 *         size of the ArrayList will always be greater than 1.
	 *
	 *         Returns null if query returns null or an empty ResultSet. Also
	 *         returns null if a communication error occurs.
	 */
	//@formatter:on
	public static List<Map<String, Object>> sendQuery(String... query)
	{

		Properties props = FileHelper.getDatabaseProperties(PROPERTY_FILE_LOC);

		String server = props.getProperty(PROPERTY_SERVER);
		String database = props.getProperty(PROPERTY_DATABASE);
		String username = props.getProperty(PROPERTY_USERNAME);
		String password = props.getProperty(PROPERTY_PASSWORD);
		String driver = props.getProperty(PROPERTY_DRIVER);

		String connectionUrl = "jdbc:mysql://" + server + "/" + database;

		if (query == null || query.length < 1)
			throw new IllegalArgumentException("sendQuery must have at least one argument.");

		List<Map<String, Object>> ret = null;
		try
		{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(connectionUrl, username, password);
			PreparedStatement ps = conn.prepareStatement(query[0]);
			for (int i = 1; i < query.length; i++)
				ps.setString(i, query[i]);

			ps.execute();
			ResultSet resultSet = ps.getResultSet();

			if (resultSet != null)
			{
				ret = cloneResultSet(resultSet);
				if (ret.size() < 2)
					ret = null;
			}

			conn.close();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			String stackTraceStr = Logger.stackTraceToString(e);
			LOG.debug(e.getMessage());
			LOG.debug(stackTraceStr);
		}

		return ret;
	}

	/**
	 * Converts a ResultSet to an ArrayList of HashMaps
	 *
	 * @param rSet
	 *
	 * @return
	 *
	 * @throws SQLException
	 */
	private static List<Map<String, Object>> cloneResultSet(ResultSet rSet) throws SQLException
	{

		ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		ResultSetMetaData meta = rSet.getMetaData();
		int totCol = meta.getColumnCount();

		String[] colName = new String[totCol];
		for (int i = 1; i <= totCol; i++)
			colName[i - 1] = meta.getColumnName(i).toString();

		String[] colType = new String[totCol];
		for (int i = 1; i <= totCol; i++)
			colType[i - 1] = meta.getColumnTypeName(i).toString();

		HashMap<String, Object> colDescription = new HashMap<String, Object>();
		for (int i = 0; i < totCol; i++)
			colDescription.put(colName[i], colType[i]);

		while (rSet.next())
		{
			HashMap<String, Object> row = new HashMap<String, Object>();

			for (int i = 0; i < totCol; i++)
				row.put(colName[i], rSet.getObject(colName[i]));

			rows.add(row);
		}
		rows.add(colDescription);
		return rows;
	}
}
