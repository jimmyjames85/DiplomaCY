package edu.iastate.cs309.r16.diplomacy.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

public class Logger
{
	@SuppressWarnings("rawtypes")
	private Class loggingClass;
	private static boolean debug = true;
	private static boolean warn = true;
	public static final String FS = System.getProperty("file.separator");
	private static final String PROPERTY_FILE_LOC = ".." + FS + "context.properties"; //This should be in the WEB-INF folder
	private static final String PROPERTY_LOG_LOCATION = "log.file.location";
	private File logFile;

	@SuppressWarnings("rawtypes")
	public Logger(Class clazz)
	{
		this.loggingClass = clazz;
		Properties props = FileHelper.getDatabaseProperties(PROPERTY_FILE_LOC);
		logFile = new File(props.getProperty(PROPERTY_LOG_LOCATION));

	}

	private void log(Object x, String method)
	{
		log("[" + loggingClass.getSimpleName() + ":" + method + "]" + x.toString());
	}

	private void log(String msg)
	{

		FileWriter fw = null;
		try
		{
			fw = new FileWriter(logFile, true);
			byte bytes[] = msg.getBytes();
			for (byte b : bytes)
				fw.append((char) b);
			fw.append('\n');
			fw.append('\n');

		}
		catch (Exception e)
		{
		}
		finally
		{
			IOUtils.closeQuietly(fw);
		}
	}

	public void debug(Exception e)
	{
		log(stackTraceToString(e), "DEBUG");		
	}
	public static String stackTraceToString(Exception e)
	{
		String stackTraceStr = "";
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++)
		{
			StackTraceElement cur = stackTrace[i];
			stackTraceStr += cur.toString() + "\n";

		}
		return stackTraceStr;
	}
	
	public void debug(Object x)
	{
		if (debug)
			log(x, "DEBUG");
	};

	public void debug()
	{
		if (debug)
			log("", "DEBUG");
	};

	public void warn(Object x)
	{
		if (debug || warn)
			log(x, "WARN");
	};

	public void warn()
	{
		if (debug || warn)
			log("", "WARN");
	};

}
