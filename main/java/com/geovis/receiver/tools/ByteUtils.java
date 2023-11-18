package com.geovis.receiver.tools;

import java.nio.charset.Charset;

/**
 * @program: Generator
 * @描述 二进制转换工具
 * @创建人 zzj
 * @创建时间 2019/11/7 15:28
 */
public class ByteUtils {

    public static byte[] getBytes(short data)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(char data)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytes(int data)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(long data)
    {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(float data)
    {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }
    
    public static byte[] getBytes(float[] data)
    {
    	byte[] result = new byte[data.length * 4];
    	for(int i = 0, count = data.length; i < count; i++)
    	{
    		byte[] bytes = getBytes(data[i]);
    		result[i * 4] = bytes[0];
    		result[i * 4 + 1] = bytes[1];
    		result[i * 4 + 2] = bytes[2];
    		result[i * 4 + 3] = bytes[3];
    	}
    	
    	return result;
    }
    
    public static byte[] getBytes(float[][] datas)
    {
    	byte[] result = new byte[datas.length * datas[0].length * 4];
    	for(int i = 0, count = datas.length; i < count; i++)
    	{
    		for(int j = 0, num = datas[i].length; j < num; j++)
    		{
    			byte[] bytes = getBytes(datas[i][j]);
        		result[(i * count + j) * 4] = bytes[0];
        		result[(i * count + j) * 4 + 1] = bytes[1];
        		result[(i * count + j) * 4 + 2] = bytes[2];
        		result[(i * count + j) * 4 + 3] = bytes[3];
    		}
    	}
    	
    	return result;
    }

    public static byte[] getBytes(double data)
    {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(String data, String charsetName)
    {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data)
    {
        return getBytes(data, "GBK");
    }


    public static short getShort(byte[] bytes)
    {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }
    
    public static short getUnsignedShort(byte[] bytes)
    {
        return (short) ((0xff & getUnsignedByte(bytes[0])) | (0xff00 & (getUnsignedByte(bytes[1]) << 8)));
    }
    

    public static char getChar(byte[] bytes)
    {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static int getInt(byte[] bytes)
    {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static long getLong(byte[] bytes)
    {
        return(0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24))
                | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L & ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] << 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));
    }

    public static float getFloat(byte[] bytes)
    {
        return Float.intBitsToFloat(getInt(bytes));
    }
    
//    public static float[] getFloats(byte[] bytes)
//    {
//    	ByteBuffer wrap = ByteBuffer.wrap(bytes);
//    	FloatBuffer floatBuffer = wrap.asFloatBuffer();
//        return floatBuffer.array();
//    }
    public static float getFloat(byte b)
	{
    	return b;
	}

    public static double getDouble(byte[] bytes)
    {
        long l = getLong(bytes);
        
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes, String charsetName)
    {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes)
    {
        return getString(bytes, "GBK");
    }
    
    public static int getUnsignedByte (byte data){
        return data&0x0FF;
    }
    
    public static int getUnsignedShort (short data){
        return data&0x0FFFF;
    }
    
    public static Object arrayCopy(byte[] data,int start,int length,Class<?> t){
        byte[] temp=new byte[length];
        System.arraycopy(data,start,temp,0,length);
        if(t==Long.class){
            return ByteUtils.getLong(temp);
        }
        if(t==Float.class){
            return ByteUtils.getFloat(temp);
        }
        if(t==String.class){
            return ByteUtils.getString(temp,"utf-8");
        }
        if(t==Integer.class){
            return ByteUtils.getInt(temp);
        }
        if(t==Short.class){
            return ByteUtils.getShort(temp);
        }
        return temp;

    }

}
