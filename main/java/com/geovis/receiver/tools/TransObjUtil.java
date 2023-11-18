package com.geovis.receiver.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  ��ȿ��� ����
 */
public class TransObjUtil {
	public static void transObj(Object from, Object to)
	{
		List<Field> fields = getAllFields(from.getClass());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for(Field field : fields)
			{
				field.setAccessible(true);
				map.put(field.getName().toUpperCase(), field.get(from));
			}
		} catch (  IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		
		List<Field> fields2 = getAllFields(to.getClass());
		try {
			for(Field field : fields2)
			{
				field.setAccessible(true);
				if(Modifier.toString(field.getModifiers()).contains("final"))
				{
					continue;
				}
				if(field.getName().equalsIgnoreCase("v01300"))
				{
					field.set(to, dealStationNum(from));
				}
				else if(field.getName().equalsIgnoreCase("precipitationEveryMinutes"))
				{
					continue;
				}
				else if("rain".equals(field.getName()))
				{
					String obj = map.get("c39".toUpperCase()).toString();
					if("999999".equals(obj))
					{
						continue;
					}
					field.set(to, (Float.parseFloat(obj.substring(1, 4)) / 10) + "");
				}
				else if("dp24".equals(field.getName()))
				{
					String obj = map.get("c310".toUpperCase()).toString();
					if("999999".equals(obj))
					{
						field.set(to, "999999");
						continue;
					}
					field.set(to, obj.substring(1, 3));
				}
				else if("dt24".equals(field.getName()))
				{
					String obj = map.get("c310".toUpperCase()).toString();
					if("999999".equals(obj))
					{
						field.set(to, "999999");
						continue;
					}
					field.set(to, obj.substring(3, 5));
				}
				else if("rain24".equals(field.getName()))
				{
					String obj = map.get("c311".toUpperCase()).toString().substring(1);
					if("99999".equals(obj))
					{
						field.set(to, "999999");
						continue;
					}
					if("9999".equals(obj))
					{
						field.set(to, "-1");
					}
					else
					{
						field.set(to, (Float.parseFloat(obj) / 10) + "");
					}
				}
				else if("max_at24".equals(field.getName()))
				{
					String obj = map.get("c312".toUpperCase()).toString().substring(1);
					if("99999".equals(obj))
					{
						field.set(to, "999999");
						continue;
					}
					String sub = obj.substring(0, 1);
					String data = obj.substring(1);
					if("0".equals(sub))
					{
						field.set(to, (Float.parseFloat(data) / 10) + "");
					}
					else
					{
						field.set(to, (-Float.parseFloat(obj) / 10) + "");
					}
				}
				else if("min_at24".equals(field.getName()))
				{
					String obj = map.get("c313".toUpperCase()).toString().substring(1);
					if("99999".equals(obj))
					{
						field.set(to, "999999");
						continue;
					}
					String sub = obj.substring(0, 1);
					String data = obj.substring(1);
					if("0".equals(sub))
					{
						field.set(to, (Float.parseFloat(data) / 10) + "");
					}
					else
					{
						field.set(to, (-Float.parseFloat(obj) / 10) + "");
					}
				}
				else if("c314".equals(field.getName()))
				{
					String obj = map.get("c314".toUpperCase()).toString().substring(1);
					if("99999".equals(obj))
					{
						field.set(to, "999999");
						continue;
					}
					String sub = obj.substring(0, 1);
					String data = obj.substring(1);
					if("0".equals(sub))
					{
						field.set(to, Double.parseDouble(data) / 10);
					}
					else
					{
						field.set(to, -Double.parseDouble(obj) / 10);
					}
				}
				else
				{
					Object obj = map.get(field.getName().toUpperCase());
					if(obj == null)
					{
						continue;
					}
					field.set(to, obj);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	private static int dealStationNum(Object obj)
	{
		int result = 0;
		String value = getFieldValue(obj, "stationNumberChina").toString();
//		if(value == null)
//		{
//			value = getFieldValue(obj, "v").toString();
//		}
		char[] charArray = value.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i = 0, count = charArray.length; i < count; i++)
		{
			int n = String.valueOf(charArray[i]).toUpperCase().hashCode();
			 if (n >= 48 & n <= 57) {
				 sb.append(charArray[i]);
			 }
			 else
			 {
				 sb.append(n);
			 }
		}
		result = Integer.parseInt(sb.toString());
		return result;
	}
	
	public static Object getFieldValue(Object obj, String fieldName)
	{
		Object result = null;
		Field field;
		try {
			field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			Object object = field.get(obj);
			result = object;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static List<Field> getAllFields(Class<? extends Object> clazz)
	{
//		List<String> listStr = new ArrayList<String>();
//		List<String> listName = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		Field[] fields2 = clazz.getSuperclass().getDeclaredFields();
		List<Field> list = new ArrayList<Field>(Arrays.asList(fields));
		list.addAll(Arrays.asList(fields2));
		
		return list;
	}
}
