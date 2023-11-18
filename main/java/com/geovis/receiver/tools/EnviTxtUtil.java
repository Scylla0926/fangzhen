package com.geovis.receiver.tools;

/**
 * 读取hdr 文件
 */
public class EnviTxtUtil {

	public static String getEnviTxt(int samples, int lines, int dataType, double startLon, double startLat, double lonDis, double latDis, String unit)
	{
		StringBuilder result = new StringBuilder("ENVI\r\n" +
												 "description = {\r\n" +
												 "File Imported into ENVI.}\r\n");
		result.append("samples = ");
		result.append(samples);
		result.append("\r\n");
		result.append("lines   = ");
		result.append(lines);
		result.append("\r\n");
		result.append("bands   = 1\r\n" + 
					  "header offset = 0\r\n" +
					  "file type = ENVI Standard\r\n");
		result.append("data type = ");
		result.append(dataType);
		result.append("\r\n");
		result.append("interleave = bsq\r\n" +
					  "sensor type = Unknown\r\n" +
					  "byte order = 0\r\n" +
					  "wavelength units = Unknown\r\n");
		result.append("map info = {Geographic Lon/Lat,1,1,");
		result.append(startLon);
		result.append(",");
		result.append(startLat);
		result.append(",");
		result.append(lonDis);
		result.append(",");
		result.append(latDis);
		result.append(",WGS-84,units = Degrees}");
		result.append("\r\n");
		result.append("unit = ");
		result.append(unit);

		return result.toString();
	}
}
