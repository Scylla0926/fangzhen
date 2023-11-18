package com.geovis.receiver.tools;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.geovis.receiver.pojo.model.DecodeConstants;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.*;

public class ReadGribDataUtil {


	public static String lon = "lon";
	public static String lat = "lat";

	/**
	 * 读取格距
	 * @param dataMap
	 * @param lonName
	 * @param latName
	 * @return
	 */
	public static double[] getGridInterval(Map<String, Object> dataMap, String lonName, String latName)
	{
		Array array_lon = getArray(dataMap, lonName);
		Array array_lat = getArray(dataMap, latName);
//		float[] lons = (float[])getArray(dataMap, lonName).copyToNDJavaArray();
		if(array_lon == null)
		{
			lon = "longitude";
			lat = "latitude";
			array_lon = getArray(dataMap, lon);
			array_lat = getArray(dataMap, lat);
		}
		if(array_lon == null)
		{
			lon = "lon1";
			lat = "lat1";
			array_lon = getArray(dataMap, lon);
			array_lat = getArray(dataMap, lat);
		}
		if(array_lon == null)
		{
			lon = "x";
			lat = "y";
			array_lon = getArray(dataMap, lon);
			array_lat = getArray(dataMap, lat);
		}
		if(array_lon == null)
		{
			lon = "xc";
			lat = "yc";
			array_lon = getArray(dataMap, lon);
			array_lat = getArray(dataMap, lat);
		}
		int rank = array_lon.getRank();
		Object obj_lon = null;
		Object obj_lat = null;
		if(rank == 3)
		{
			lon = "xc";
			lat = "yc";
			array_lon = getArray(dataMap, lon);
			array_lat = getArray(dataMap, lat);
			obj_lon = array_lon.copyToNDJavaArray();
			obj_lat = array_lat.copyToNDJavaArray();
		}
		else
		{
			obj_lon = array_lon.copyTo1DJavaArray();
			obj_lat = array_lat.copyTo1DJavaArray();
		}
		float[] lons =toFloats(obj_lon);
		float[] lats =toFloats(obj_lat);

//		return new double[]{Math.abs(lons[0] - lons[1]), Math.abs(lats[0] - lats[1])};


		return new double[]{lons[1] - lons[0], lats[1] - lats[0]};
	}

	public static float[] toFloats(Object obj)
	{
		float[] result =  null;
		if(obj.getClass() == double[].class)
		{
			double[] datas = (double[]) obj;
			result = new float[datas.length];
			for(int i = 0, count = datas.length; i < count; i++)
			{
				result[i] = (float) datas[i];
			}
		}
		else if(obj.getClass() == int[].class)
		{
			int[] datas = (int[]) obj;
			result = new float[datas.length];
			for(int i = 0, count = datas.length; i < count; i++)
			{
				result[i] = datas[i];
			}
		}
		else
		{
			result = (float[])obj;
		}

		return result;
	}

	public static String[] getLonLatName(Map<String, Object> datasMap)
	{
		String[] result = new String[]{"lon_0", "lat_0"};
		String elName = null;
		for(String key : datasMap.keySet())
		{
			if(!(datasMap.get(key) instanceof Variable))
			{
				continue;
			}
			Variable v = (Variable) datasMap.get(key);
			int rank = v.getRank();
			if(rank >= 1)
			{
				elName = key;
				break;
			}
		}
		Variable variable = (Variable) datasMap.get(elName.toLowerCase());
		if(variable != null)
		{
			int count = variable.getDimensions().size();

			String lon = variable.getDimensions().get(count - 1).toString().replace(";", "").replace(" ", "");
			if(lon.contains("UNLIMITED"))
			{
				lon = lon.replace("UNLIMITED//(", "").replace("currently)", "");
			}
			String[] split = lon.split("=");
			if(Integer.parseInt(split[1].trim()) >= 1)
			{
				lon = split[0];
			}
//			String lat = variable.getDimensions().get(count - 2).toString().replace(";", "").replace(" ", "");
//			if(lat.contains("UNLIMITED"))
//			{
//				lat = lat.replace("UNLIMITED//(", "").replace("currently)", "");
//			}
//			split = lat.split("=");
//			if(Integer.parseInt(split[1].trim()) >= 1)
//			{
//				lat = split[0];
//			}

			result[0] = lon;
			result[1] = lat;
		}

		return result;
	}

	public static Map<String, String> getGlobalAttributes(String filePath)
	{
		Map<String, String> result = new ConcurrentHashMap<>();

		try {
			NetcdfFile dataset = NetcdfFile.open(filePath);
			List<Attribute> globalAttributes = dataset.getGlobalAttributes();
			for(Attribute at : globalAttributes)
			{
				if(at.isString())
				{
					result.put(at.getShortName(), at.getStringValue());
				}
				else
				{
					result.put(at.getShortName(), String.valueOf(at.getNumericValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		return result;
	}
	
	private static void getVariables(Group group, Map<String, Object> result)
	{
		List<Group> groups = group.getGroups();
		if(groups.size() > 0)
		{
			for(Group g : groups)
			{
				getVariables(g, result);
			}
		}
		else
		{
			List<Variable> list = group.getVariables();
			for(Variable v : list)
			{
				String name = v.getShortName().toLowerCase();
				result.put(v.getFullName().toLowerCase().replace("/", "_") + "." + name, v);
//				int[] shape = v.getShape();
//				if(shape.length >= 2 && hasElement(name, dataType))
//				{
//					pngCount += getLayerCount(v.getShape());
//				}
			}
		}
	}

	public static Map<String, Object> getStructure(String filePath)
	{
		Map<String, Object> result = new ConcurrentHashMap<String, Object>();
		try {
			NetcdfFile dataset = NetcdfFile.open(filePath);
			Group rootGroup = dataset.getRootGroup();
			List<Group> groups = rootGroup.getGroups();
			int pngCount = 0;
			int[] shape = null;
			if(groups.size() > 0)
			{
				for(int i = 0, count = groups.size(); i < count; i++)
				{
					List<Variable> list = groups.get(i).getVariables();
					for(Variable v : list)
					{
						if(v instanceof Structure)
						{
							List<String> variableNames = ((Structure) v).getVariableNames();
							for(String variableName : variableNames)
							{
								Variable variable = ((Structure) v).findVariable(variableName);
								String name = variable.getShortName();
								DataType dataType = variable.getDataType();
								Array array = variable.read();
								if(dataType == DataType.SHORT)
								{
									result.put(name, array.getShort(0));
								}
								else if(dataType == DataType.INT)
								{
									result.put(name, array.getInt(0));
								}
								else if(dataType == DataType.LONG)
								{
									result.put(name, array.getLong(0));
								}
								else if(dataType == DataType.CHAR)
								{
									result.put(name, array.getChar(0));
								}
								else if(dataType == DataType.FLOAT)
								{
									result.put(name, array.getFloat(0));
								}
								else if(dataType == DataType.DOUBLE)
								{
									result.put(name, array.getDouble(0));
								}
							}
						}
					}
				}
			}
			else
			{
				List<Variable> list = dataset.getVariables();
				for(Variable v : list)
				{
					if(v instanceof Structure)
					{
						List<String> variableNames = ((Structure) v).getVariableNames();
						for(String variableName : variableNames)
						{
							Variable variable = ((Structure) v).findVariable(variableName);
							String name = variable.getShortName();
							DataType dataType = variable.getDataType();
							Array array = variable.read();
							if(dataType == DataType.SHORT)
							{
								result.put(name, array.getShort(0));
							}
							else if(dataType == DataType.INT)
							{
								result.put(name, array.getInt(0));
							}
							else if(dataType == DataType.LONG)
							{
								result.put(name, array.getLong(0));
							}
							else if(dataType == DataType.CHAR)
							{
								result.put(name, array.getChar(0));
							}
							else if(dataType == DataType.FLOAT)
							{
								result.put(name, array.getFloat(0));
							}
							else if(dataType == DataType.DOUBLE)
							{
								result.put(name, array.getDouble(0));
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	public static double getInvalidValue(Map<String, Object> datasMap, String element)
	{
		double result = -999999;
		Variable v = (Variable) datasMap.get(element);
		if(v != null)
		{
			List<Attribute> list = v.getAttributes();
			for(Attribute attr : list)
			{
				if(attr.getFullName().equals("_FillValue"))
				{
					result = getAttributeNumValue(attr);
				}
			}

		}

		return result;
	}
	
	public static double[] getValid_range(Map<String, Object> datasMap, String element)
	{
		double[] result = new double[2];
		Variable v = (Variable) datasMap.get(element);
		double scale_factor = 1;
		double add_offset = 0;
		if(v != null)
		{
			List<Attribute> list = v.getAttributes();
			for(Attribute attr : list)
			{
				String name = attr.getFullName().toLowerCase();
				if(name.equals("valid_range"))
				{
					result[0] = attr.getNumericValue(0).floatValue();
					result[1] = attr.getNumericValue(1).floatValue();
				}
				else if(name.equals("lowervalidrange") || name.equals("valid_min"))
				{
					result[0] = attr.getNumericValue().floatValue();
				}
				else if(name.equals("uppervalidrange") || name.equals("valid_max"))
				{
					result[1] = attr.getNumericValue().floatValue();
				}
				else if(attr.getFullName().equals("scale_factor"))
				{
					scale_factor = getAttributeNumValue(attr);
				}
				else if(attr.getFullName().equals("add_offset"))
				{
					add_offset = getAttributeNumValue(attr);
				}
			}

			result[0] = result[0] * scale_factor + add_offset;
			result[1] = result[1] * scale_factor + add_offset;
		}

		return result;
	}
	
	private static int getLayerCount(int[] shape)
	{
		int result = 0;
		int size = shape.length;
		if(size == 2)
		{
			result = 1;
		}
		else if(size == 3)
		{
			result =  shape[0];
		}
		else if(size == 4)
		{
			result = shape[0] * shape[1];
		}
		
		
		return result;
	}

	private static double getAttributeNumValue(Attribute att)
	{
		double result = 0;
		DataType dataType = att.getDataType();
		if(dataType == DataType.FLOAT)
		{
			result = (float) att.getNumericValue();
		}
		else if(dataType == DataType.DOUBLE)
		{
			result = (double) att.getNumericValue();
		}
		else if(dataType == DataType.INT)
		{
			result = (int) att.getNumericValue();
		}

		return result;
	}

	public synchronized static double[][][] readByElemNameLayerSlice(Map<String, Object> dataMap, String elName, String layer)
	{
		double[][][] result = null;
//		elName = elName.toLowerCase();
		Variable v = (Variable) dataMap.get(elName);
		if(v == null)
		{
			return result;
		}
		List<Attribute> attributes = v.getAttributes();
		double missValue = -999999f;
		double scale_factor = 1;
		double add_offset = 0;
		for(Attribute att : attributes) {
			if (att.getFullName().equals("missing_value"))
			{
				missValue = getAttributeNumValue(att);
			}
			else if(att.getFullName().equals("_FillValue"))
			{
				missValue = getAttributeNumValue(att);
			}
			else if(att.getFullName().equals("scale_factor"))
			{
				scale_factor = getAttributeNumValue(att);
			}
			else if(att.getFullName().equals("add_offset"))
			{
				add_offset = getAttributeNumValue(att);
			}
		}
		int rank = v.getRank();
		int[] sOrigin = new int[rank];
		int[] sShape = new int[rank];
		for(int i = 0; i < rank; i++) {
			sOrigin[i] = 0;
			sShape[i] = 1;
		}

		int[] shape = v.getShape();
		sShape[rank - 2] = shape[rank - 2];
		sShape[rank - 1] = shape[rank - 1];
//		int layerIndex = getLayerIndex(dataMap, elName, layer);
		int layerIndex = Integer.parseInt(layer);
		int temp = rank - 3;
		if(temp < 0)
		{
			temp = 0;
		}
		sOrigin[temp] = layerIndex;
		try {
			Array array = v.read(sOrigin, sShape);

			Object objArray = array.copyToNDJavaArray();

			result = toDoubleArray(objArray, rank, dataMap, elName, layer, missValue, scale_factor, add_offset);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvalidRangeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}


		return result;
	}

	private static double[][][] toDoubleArray(Object objArray, int rank, Map<String, Object> dataMap, String elName, String layer, double missValue, double scale_factor, double add_offset)
	{
		double[][][] result = null;
		Class<? extends Object> arrayClass = objArray.getClass();
		if(arrayClass == byte[][].class || arrayClass == byte[][][].class || arrayClass == byte[][][][].class || arrayClass == byte[][][][][].class)
		{
			byte[][][] data = null;
			if(rank == 2)
			{
				data = new byte[1][][];
				data[0] = ((byte[][]) objArray);
			}
			else if(rank == 3)
			{
				data = ((byte[][][]) objArray);
			}
			else if(rank == 4)
			{
				byte[][][] datas = ((byte[][][][]) objArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
				data = datas;
			}
			result = new double[data.length][data[0].length][data[0][0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					for(int k = 0; k < data[i][j].length; k++)
					{
						if(data[i][j][k] == missValue)
						{
							result[i][j][k] = (byte) DecodeConstants.UNDEF_INT_VALUE;
						}
						else
						{
							result[i][j][k] = data[i][j][k] * scale_factor + add_offset;
						}
					}
				}
			}
		}
		else if(arrayClass == short[][].class || arrayClass == short[][][].class || arrayClass == short[][][][].class || arrayClass == short[][][][][].class)
		{
			short[][][] data = null;
			if(rank == 2)
			{
//				data = ((short[][][]) objArray);
				data = new short[1][][];
				data[0] = ((short[][]) objArray);
			}
			else if(rank == 3)
			{
				data = ((short[][][]) objArray);
			}
			else if(rank == 4)
			{
				short[][][] datas = ((short[][][][]) objArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
				data = datas;
			}
			result = new double[data.length][data[0].length][data[0][0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					for(int k = 0; k < data[i][j].length; k++)
					{
						if(data[i][j][k] == missValue)
						{
							result[i][j][k] = (short) DecodeConstants.UNDEF_INT_VALUE;
						}
						else
						{
							result[i][j][k] = data[i][j][k] * scale_factor + add_offset;
						}
					}
				}
			}
		}
		else if(arrayClass == int[][].class || arrayClass == int[][][].class || arrayClass == int[][][][].class || arrayClass == int[][][][][].class)
		{
			int[][][] data = null;
			if(rank == 2)
			{
				data = new int[1][][];
				data[0] = ((int[][]) objArray);
			}
			else if(rank == 3)
			{
				data = ((int[][][]) objArray);
			}
			else if(rank == 4)
			{
				int[][][] datas = ((int[][][][]) objArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
				data = datas;
			}
			result = new double[data.length][data[0].length][data[0][0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					for(int k = 0; k < data[i][j].length; k++)
					{
						if(data[i][j][k] == missValue)
						{
							result[i][j][k] = (int) DecodeConstants.UNDEF_INT_VALUE;
						}
						else
						{
							result[i][j][k] = data[i][j][k] * scale_factor + add_offset;
						}
					}
				}
			}
		}
		else if(arrayClass == float[][].class || arrayClass == float[][][].class || arrayClass == float[][][][].class || arrayClass == float[][][][][].class)
		{
			float[][][] data = null;
			if(rank == 2)
			{
//				data = ((float[][][]) objArray);
				data = new float[1][][];
				data[0] = ((float[][]) objArray);
			}
			else if(rank == 3)
			{
				data = ((float[][][]) objArray);
			}
			else if(rank == 4)
			{
				float[][][] datas = ((float[][][][]) objArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
				data = datas;
			}
			result = new double[data.length][data[0].length][data[0][0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					for(int k = 0; k < data[i][j].length; k++)
					{
						if(data[i][j][k] == missValue)
						{
							result[i][j][k] = (float) DecodeConstants.UNDEF_INT_VALUE;
						}
						else
						{
							result[i][j][k] = data[i][j][k] * scale_factor + add_offset;
						}
					}
				}
			}
		}
		else if(arrayClass == double[][].class || arrayClass == double[][][].class || arrayClass == double[][][][].class || arrayClass == double[][][][][].class)
		{
			double[][][] data = null;
			if(rank == 2)
			{
				data = new double[1][][];
				data[0] = ((double[][]) objArray);
			}
//			double[][][] data = null;
//			if(rank == 2)
//			{
//				data = ((double[][][]) objArray);
//			}
			else if(rank == 3)
			{
				data = ((double[][][]) objArray);
			}
			else if(rank == 4)
			{
				double[][][] datas = ((double[][][][]) objArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
				data = datas;
			}
			else if(rank == 5)
			{
				double[][][][] datas = ((double[][][][][]) objArray)[0];
				int num = 0;
//				int index = getLayerIndex(dataMap, elName, layer);
				data = datas[num];
			}
			result = new double[data.length][data[0].length][data[0][0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					for(int k = 0; k < data[i][j].length; k++)
					{
						if(data[i][j][k] == missValue)
						{
							result[i][j][k] = (double) DecodeConstants.UNDEF_INT_VALUE;
						}
						else
						{
							result[i][j][k] = data[i][j][k] * scale_factor + add_offset;
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * 读取指定要素和层次的数据
	 * @param dataMap
	 * @param elName
	 * @param layer
	 * @return
	 */
	public static double[][] readByNameLayer(Map<String, Object> dataMap, String elName, String layer)
	{
		double[][] result = null;
		Variable v = (Variable) dataMap.get(elName);

		List<Attribute> attributes = v.getAttributes();
		float missValue = -999999f;
		for(Attribute att : attributes)
		{
			if(att.getFullName().equals("missing_value"))
			{
				missValue = (float) att.getNumericValue();
			}
		}
		int rank = v.getRank();
		long time = System.currentTimeMillis();
//		Object copyToNDJavaArray = getArray(dataMap, elName).copyToNDJavaArray();
		Array array = getArray(dataMap, elName);
//		System.out.println("000000000000000000000000: " + (System.currentTimeMillis() - time));
//		time = System.currentTimeMillis();
		Object dValue = array.copyToNDJavaArray();
		System.out.println("读 " + elName + "_" + layer + " 数据耗时: " + (System.currentTimeMillis() - time));
		if(dValue.getClass() == byte[].class || dValue.getClass() == byte[][].class || dValue.getClass() == byte[][][].class || dValue.getClass() == byte[][][][].class)
		{
			byte[][] data = null;
			if(rank == 1)
			{
				data = new byte[1][];
				data[0] = (byte[]) dValue;
			}
			else if(rank == 2)
			{
				data = ((byte[][]) dValue);
			}
			else if(rank == 3)
			{
				data = ((byte[][][]) dValue)[0];
			}
			else if(rank == 4)
			{
				byte[][][] datas = ((byte[][][][]) dValue)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
			result = new double[data.length][data[0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					if(data[i][j] == missValue)
					{
						result[i][j] = (byte) DecodeConstants.UNDEF_INT_VALUE;
					}
					else
					{
						result[i][j] = data[i][j];
					}
				}
			}
		}
		else if(dValue.getClass() == short[].class || dValue.getClass() == short[][].class || dValue.getClass() == short[][][].class || dValue.getClass() == short[][][][].class)
		{
			short[][] data = null;
			if(rank == 1)
			{
				data = new short[1][];
				data[0] = (short[]) dValue;
			}
			else if(rank == 2)
			{
				data = ((short[][]) dValue);
			}
			else if(rank == 3)
			{
				data = ((short[][][]) dValue)[0];
			}
			else if(rank == 4)
			{
				short[][][] datas = ((short[][][][]) dValue)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
			result = new double[data.length][data[0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					if(data[i][j] == missValue)
					{
						result[i][j] = (short) DecodeConstants.UNDEF_INT_VALUE;
					}
					else
					{
						result[i][j] = data[i][j];
					}
				}
			}
		}
		else if(dValue.getClass() == int[].class || dValue.getClass() == int[][].class || dValue.getClass() == int[][][].class || dValue.getClass() == int[][][][].class)
		{
			int[][] data = null;
			if(rank == 1)
			{
				data = new int[1][];
				data[0] = (int[]) dValue;
			}
			else if(rank == 2)
			{
				data = ((int[][]) dValue);
			}
			else if(rank == 3)
			{
				data = ((int[][][]) dValue)[0];
			}
			else if(rank == 4)
			{
				int[][][] datas = ((int[][][][]) dValue)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
			result = new double[data.length][data[0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					if(data[i][j] == missValue)
					{
						result[i][j] = DecodeConstants.UNDEF_INT_VALUE;
					}
					else
					{
						result[i][j] = data[i][j];
					}
				}
			}
		}else if(dValue.getClass() == long[].class || dValue.getClass() == long[][].class || dValue.getClass() == long[][][].class || dValue.getClass() == long[][][][].class)
		{
			long[][] data = null;
			if(rank == 1)
			{
				data = new long[1][];
				data[0] = (long[]) dValue;
			}
			else if(rank == 2)
			{
				data = ((long[][]) dValue);
			}
			else if(rank == 3)
			{
				data = ((long[][][]) dValue)[0];
			}
			else if(rank == 4)
			{
				long[][][] datas = ((long[][][][]) dValue)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
			result = new double[data.length][data[0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					if(data[i][j] == missValue)
					{
						result[i][j] = DecodeConstants.UNDEF_INT_VALUE;
					}
					else
					{
						result[i][j] = data[i][j];
					}
				}
			}
		}
		else if(dValue.getClass() == float[].class || dValue.getClass() == float[][].class || dValue.getClass() == float[][][].class || dValue.getClass() == float[][][][].class)
		{
			float[][] data = null;
			if(rank == 1)
			{
				data = new float[1][];
				data[0] = (float[]) dValue;
			}
			else if(rank == 2)
			{
				data = ((float[][]) dValue);
			}
			else if(rank == 3)
			{
				data = ((float[][][]) dValue)[0];
			}
			else if(rank == 4)
			{
				float[][][] datas = ((float[][][][]) dValue)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
			result = new double[data.length][data[0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					if(data[i][j] == missValue)
					{
						result[i][j] = DecodeConstants.UNDEF_INT_VALUE;
					}
					else
					{
						result[i][j] = data[i][j];
					}
				}
			}
		}
		else if(dValue.getClass() == double[].class || dValue.getClass() == double[][].class || dValue.getClass() == double[][][].class || dValue.getClass() == double[][][][].class)
		{
			double[][] data = null;
			if(rank == 1)
			{
				data = new double[1][];
				data[0] = (double[]) dValue;
			}
			else if(rank == 2)
			{
				data = ((double[][]) dValue);
			}
			else if(rank == 3)
			{
				data = ((double[][][]) dValue)[0];
			}
			else if(rank == 4)
			{
				double[][][] datas = ((double[][][][]) dValue)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
			result = new double[data.length][data[0].length];
			for(int i = 0; i < data.length; i++)
			{
				for(int j = 0; j < data[0].length; j++)
				{
					if(data[i][j] == missValue)
					{
						result[i][j] = DecodeConstants.UNDEF_INT_VALUE;
					}
					else
					{
						result[i][j] = data[i][j];
					}
				}
			}
		}

		return result;
	}

	/**
	 * 读取指定要素和层次的数据
	 * @param dataMap
	 * @param elName
	 * @param layer
	 * @return
	 */
	public static float[][] readByNameLayerFloat(Map<String, Object> dataMap, String elName, String layer)
	{
		float[][] result = null;
		Variable v = (Variable) dataMap.get(elName);
//		if(elName.equals("DQF"))
//		{
//			System.out.println();
//		}
		List<Attribute> attributes = v.getAttributes();
		float missValue = -999999f;
		for(Attribute att : attributes)
		{
			if(att.getFullName().equals("missing_value"))
			{
				missValue = (float) att.getNumericValue();
			}
		}
		int rank = v.getRank();
		long time = System.currentTimeMillis();
//		Object copyToNDJavaArray = getArray(dataMap, elName).copyToNDJavaArray();
		Array array = getArray(dataMap, elName);
//		System.out.println("000000000000000000000000: " + (System.currentTimeMillis() - time));
//		time = System.currentTimeMillis();
		Object copyToNDJavaArray = array.copyToNDJavaArray();
		System.out.println("读 " + elName + "_" + layer + " 数据耗时: " + (System.currentTimeMillis() - time));
		if(copyToNDJavaArray.getClass() == byte[][].class || copyToNDJavaArray.getClass() == byte[][][].class || copyToNDJavaArray.getClass() == byte[][][][].class)
		{
			byte[][] data = null;
			if(rank == 2)
			{
				data = ((byte[][]) copyToNDJavaArray);
			}
			else if(rank == 3)
			{
				data = ((byte[][][]) copyToNDJavaArray)[0];
			}
			else if(rank == 4)
			{
				byte[][][] datas = ((byte[][][][]) copyToNDJavaArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
		}
		else if(copyToNDJavaArray.getClass() == short[][].class || copyToNDJavaArray.getClass() == short[][][].class || copyToNDJavaArray.getClass() == short[][][][].class)
		{
			short[][] data = null;
			if(rank == 2)
			{
				data = ((short[][]) copyToNDJavaArray);
			}
			else if(rank == 3)
			{
				data = ((short[][][]) copyToNDJavaArray)[0];
			}
			else if(rank == 4)
			{
				short[][][] datas = ((short[][][][]) copyToNDJavaArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}
		}
		else if(copyToNDJavaArray.getClass() == float[][].class || copyToNDJavaArray.getClass() == float[][][].class || copyToNDJavaArray.getClass() == float[][][][].class)
		{
			float[][] data = null;
			if(rank == 2)
			{
				data = ((float[][]) copyToNDJavaArray);
			}
			else if(rank == 3)
			{
				data = ((float[][][]) copyToNDJavaArray)[0];
			}
			else if(rank == 4)
			{
				float[][][] datas = ((float[][][][]) copyToNDJavaArray)[0];
//				int index = getLayerIndex(dataMap, elName, layer);
//				data = datas[index];
			}

		}

		return result;
	}

	public static Array getArray(Map<String, Object> dataMap, String elName)
	{
		Array result = null;
		Variable v = (Variable) dataMap.get(elName);
		if(v == null)
		{
			return result;
		}
		try {
			result = v.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Array getArray(Variable v)
	{
		Array result = null;
		if(v == null)
		{
			return result;
		}
		try {
			result = v.read();
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	public static double[] readByName(Map<String, Object> dataMap, String elName)
	{
		double[] result = null;
		elName = elName.toLowerCase();
		Variable v = (Variable) dataMap.get(elName);
		if(v == null)
		{
			result = new double[]{-1};
			return result;
		}
		int rank = v.getRank();
		Array array = getArray(v);

		if(array == null)
		{
			return result;
		}
		Object copyToNDArray = array.copyToNDJavaArray();
		if(copyToNDArray.getClass() == byte[].class || copyToNDArray.getClass() == byte[][].class || copyToNDArray.getClass() == byte[][][].class || copyToNDArray.getClass() == byte[][][][].class)
		{
			byte[][] data = null;
			if(rank == 2)
			{
				data = ((byte[][]) copyToNDArray);
			}
			else if(rank == 3)
			{
				data = ((byte[][][]) copyToNDArray)[0];
			}
			else if(rank == 4)
			{
				byte[][][] datas = ((byte[][][][]) copyToNDArray)[0];
				int x = datas.length;
				int y = datas[0].length;
				int z = datas[0][0].length;
				result = new double[x * y * z];
				for(int i = 0; i < x; i++)
				{
					for(int j = 0; j < y; j++)
					{
						for(int k = 0; k < z; k++)
						{
							result[i * y * z + (j * z + k)] = datas[i][j][k];
						}
					}
				}
			}
		}
		else if(copyToNDArray.getClass() == short[][].class || copyToNDArray.getClass() == short[][][].class || copyToNDArray.getClass() == short[][][][].class)
		{
			short[][] data = null;
			if(rank == 2)
			{
				data = ((short[][]) copyToNDArray);

			}
			else if(rank == 3)
			{
				data = ((short[][][]) copyToNDArray)[0];
			}
			else if(rank == 4)
			{
				short[][][] datas = ((short[][][][]) copyToNDArray)[0];
				int x = datas.length;
				int y = datas[0].length;
				int z = datas[0][0].length;
				result = new double[x * y * z];
				for(int i = 0; i < x; i++)
				{
					for(int j = 0; j < y; j++)
					{
						for(int k = 0; k < z; k++)
						{
							result[i * y * z + (j * z + k)] = datas[i][j][k];
						}
					}
				}
			}
		}
		else if(copyToNDArray.getClass() == int[].class || copyToNDArray.getClass() == int[][].class || copyToNDArray.getClass() == int[][][].class || copyToNDArray.getClass() == int[][][][].class)
		{
			int[][] data = null;
			if(rank == 1)
			{
				int[] temp = (int[])copyToNDArray;
				int count = temp.length;
				result = new double[count];
				for(int i = 0; i < count; i++)
				{
					result[i] = temp[i];
				}
			}
			else if(rank == 2)
			{
				data = ((int[][]) copyToNDArray);

			}
			else if(rank == 3)
			{
				data = ((int[][][]) copyToNDArray)[0];
			}
			else if(rank == 4)
			{
				int[][][] datas = ((int[][][][]) copyToNDArray)[0];
				int x = datas.length;
				int y = datas[0].length;
				int z = datas[0][0].length;
				result = new double[x * y * z];
				for(int i = 0; i < x; i++)
				{
					for(int j = 0; j < y; j++)
					{
						for(int k = 0; k < z; k++)
						{
							result[i * y * z + (j * z + k)] = datas[i][j][k];
						}
					}
				}
			}
		}else if(copyToNDArray.getClass() == long[][].class || copyToNDArray.getClass() == long[][][].class || copyToNDArray.getClass() == long[][][][].class)
		{
			long[][] data = null;
			if(rank == 2)
			{
				data = ((long[][]) copyToNDArray);

			}
			else if(rank == 3)
			{
				data = ((long[][][]) copyToNDArray)[0];
			}
			else if(rank == 4)
			{
				long[][][] datas = ((long[][][][]) copyToNDArray)[0];
				int x = datas.length;
				int y = datas[0].length;
				int z = datas[0][0].length;
				result = new double[x * y * z];
				for(int i = 0; i < x; i++)
				{
					for(int j = 0; j < y; j++)
					{
						for(int k = 0; k < z; k++)
						{
							result[i * y * z + (j * z + k)] = datas[i][j][k];
						}
					}
				}
			}
		}

		else if(copyToNDArray.getClass() == float[].class || copyToNDArray.getClass() == float[][].class || copyToNDArray.getClass() == float[][][].class || copyToNDArray.getClass() == float[][][][].class)
		{
			float[][] data = null;
			if(rank == 1)
			{
				float[] temp = (float[])copyToNDArray;
				int count = temp.length;
				result = new double[count];
				for(int i = 0; i < count; i++)
				{
					result[i] = temp[i];
				}
			}
			else if(rank == 2)
			{
				data = ((float[][]) copyToNDArray);
				result = new double[data.length * data[0].length];
				for(int i = 0, count = data.length; i < count; i++)
				{
					for(int j = 0, num = data[i].length; j < num; j++)
					{
						result[i * num + j] = data[i][j];
					}
				}
			}
			else if(rank == 3)
			{
				data = ((float[][][]) copyToNDArray)[0];
			}
			else if(rank == 4)
			{
				float[][][] datas = ((float[][][][]) copyToNDArray)[0];
				int x = datas.length;
				int y = datas[0].length;
				int z = datas[0][0].length;
				result = new double[x * y * z];
				for(int i = 0; i < x; i++)
				{
					for(int j = 0; j < y; j++)
					{
						for(int k = 0; k < z; k++)
						{
							result[i * y * z + (j * z + k)] = datas[i][j][k];
						}
					}
				}
			}
		}else if(copyToNDArray.getClass() == double[].class || copyToNDArray.getClass() == double[][].class || copyToNDArray.getClass() == double[][][].class || copyToNDArray.getClass() == double[][][][].class)
		{
			double[][] data = null;
			if(rank == 1)
			{
				double[] temp = (double[])copyToNDArray;
				int count = temp.length;
				result = new double[count];
				for(int i = 0; i < count; i++)
				{
					result[i] = temp[i];
				}
			}
			else if(rank == 2)
			{
				data = ((double[][]) copyToNDArray);
			}
			else if(rank == 3)
			{
				data = ((double[][][]) copyToNDArray)[0];
			}
			else if(rank == 4)
			{
				double[][][] datas = ((double[][][][]) copyToNDArray)[0];
				int x = datas.length;
				int y = datas[0].length;
				int z = datas[0][0].length;
				result = new double[x * y * z];
				for(int i = 0; i < x; i++)
				{
					for(int j = 0; j < y; j++)
					{
						for(int k = 0; k < z; k++)
						{
							result[i * y * z + (j * z + k)] = datas[i][j][k];
						}
					}
				}
			}
		}


		return result;
	}
}
