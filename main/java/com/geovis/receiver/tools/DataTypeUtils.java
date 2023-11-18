package com.geovis.receiver.tools;

import java.io.File;
import java.util.Locale;

public class DataTypeUtils {

	public static String getDataType(String filePath)
	{
		String result = null;
		String prefix = new File(filePath).getName().toLowerCase(Locale.ROOT);
		if(prefix.contains("-grapes-"))
		{
			result = "grapes";
		}
		else if(prefix.startsWith("et"))
		{
			result = "xqzq";
		}
		else if(prefix.contains("_ecmf_"))
		{
			result = "ecmf";
		}
		else if(prefix.contains("kwbc"))
		{
			result = "kwbc";
		}
		else
		{
			result = "default";
		}

		return result;
	}
}
