package com.geovis.receiver.tools;


import org.apache.commons.lang3.ArrayUtils;
import ucar.ma2.Array;
import ucar.nc2.Variable;

import java.text.NumberFormat;
import java.util.Arrays;

/**
 * @Author: gengfangdong
 * @Description
 * @FileName: NumberUtils
 * @Date: 2022/10/31 14:29
 * @Version: 1.0
 */
public class NumberUtils {
    /**
     * 查找指定值在数组中的第一个位置
     *
     * @param data  数组
     * @param value 指定的值
     * @return 第一个位置 如果不存在 返回 -1
     */
    public static int findValueFirstIndexOfData(double[] data, double value) {
        if (data == null) {
            return -1;
        }
        int index = -1;
        double step = 0.0009;
        if (data.length >= 2) {
            step = Math.abs(data[0] - data[1]);
        }
        System.out.println("步长为:" + step);
        for (int i = 0; i < data.length; i++) {
            if (Double.valueOf(value) == data[i]) {
                index = i;
                break;
            }
            if (Math.abs(Double.valueOf(value) - data[i]) < step) {
                index = i;
            }
        }
        return index;
    }

    /**
     * 计算二维数组中的最大值和最小值
     *
     * @param doubleArray
     * @return {maxValue,minValue}
     */
    public static double[] calcMaxAndMinInTwoArray(double[][] doubleArray) {
        int yLength = doubleArray.length;
        if (yLength <= 0) {
            return null;
        }
        int xLength = doubleArray[0].length;
        if (xLength <= 0) {
            return null;
        }

        double maxData = 0.0, minData = 0.0;
        for (int i = 0; i < yLength; i++) {
            Arrays.sort(doubleArray[i]);
            double maxCandidate = doubleArray[i][xLength - 1];
            double minCandidate = doubleArray[i][0];
            if (maxData < maxCandidate || maxData == 0.0) {
                maxData = maxCandidate;
            }
            if (minData > minCandidate || minData == 0.0) {
                minData = minCandidate;
            }
        }

        return new double[]{maxData, minData};
    }

    /**
     * 合并两个数组中的最大值变成一个
     *
     * @param data1 数组1
     * @param data2 数组2
     * @return
     */
    public static float[] floatMergeMax(float[] data1, float[] data2) {
        if (data1 == null) {
            return data2;
        }
        if (data2 == null) {
            return data1;
        }
        if (data1.length != data2.length) {
            throw new RuntimeException("数据维度不一致!");
        }
        int length = data1.length;
        float[] result = new float[length];
        for (int i = 0; i < length; i++) {
            result[i] = Math.max(data1[i], data2[i]);
        }
        return result;
    }

    /**
     * 合并两个数组中的最大值变成一个
     *
     * @param data1 数组1
     * @param data2 数组2
     * @return
     */
    public static double[] doubleMergeMax(double[] data1, double[] data2) {
        if (data1 == null) {
            return data2;
        }
        if (data2 == null) {
            return data1;
        }
        if (data1.length != data2.length) {
            throw new RuntimeException("数据维度不一致!");
        }
        int length = data1.length;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = Math.max(data1[i], data2[i]);
        }
        return result;
    }

    /**
     * 合并两个数组中的最大值变成一个
     *
     * @param data1 数组1
     * @param data2 数组2
     * @return
     */
    public static double[] doubleMergeMin(double[] data1, double[] data2) {
        if (data1 == null) {
            return data2;
        }
        if (data2 == null) {
            return data1;
        }
        if (data1.length != data2.length) {
            throw new RuntimeException("数据维度不一致!");
        }
        int length = data1.length;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            result[i] = Math.min(data1[i], data2[i]);
        }
        return result;
    }

    /**
     * object 转 double
     *
     * @param data
     * @return
     */
    public static double[] transformToDouble(Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof double[]) {
            return (double[]) data;
        } else if (data instanceof float[]) {
            return NumberUtils.transformFloatToDouble((float[]) data);
        } else if (data instanceof char[]) {
            return NumberUtils.transformCharToDouble((char[]) data);
        } else if (data instanceof short[]) {
            return NumberUtils.transformShortToDouble((short[]) data);
        } else if (data instanceof int[]) {
            return NumberUtils.transformIntToDouble((int[]) data);
        } else if (data instanceof long[]) {
            return NumberUtils.transformLongToDouble((long[]) data);
        }else if (data instanceof byte[]) {
            return NumberUtils.transformByteToDouble((byte[]) data);
        }
        return null;
    }

    /**
     * 从要素中获取二维数据
     *
     * @param objectArray 数据
     * @return
     */
    public static double[][] transform2DToDouble(Object objectArray)  {
        try {
            if (objectArray != null) {
                if (objectArray instanceof double[][]) {
                    return (double[][]) objectArray;
                } else if (objectArray instanceof float[][]) {
                    return transformFloatToDouble((float[][]) objectArray);
                } else if (objectArray instanceof char[][]) {
                    return transformCharToDouble((char[][]) objectArray);
                } else if (objectArray instanceof short[][]) {
                    return transformShortToDouble((short[][]) objectArray);
                } else if (objectArray instanceof int[][]) {
                    return transformIntToDouble((int[][]) objectArray);
                } else if (objectArray instanceof long[][]) {
                    return transformLongToDouble((long[][]) objectArray);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[0][0];
    }

    /**
     * object 转 double
     *
     * @param arrayObj 数组对象 二维
     * @param xIndex   x位置
     * @param yIndex   y位置
     * @return
     */
    public static Double getPointFromData(Object arrayObj, Integer xIndex, Integer yIndex) {
        if (arrayObj == null) {
            return null;
        }
        if (arrayObj instanceof double[][]) {
            double[][] data = (double[][]) arrayObj;
            return data[xIndex][yIndex];
        }
        if (arrayObj instanceof short[][]) {
            double[][] data = transformShortToDouble((short[][]) arrayObj);
            return data[xIndex][yIndex];
        }
        if (arrayObj instanceof float[][]) {
            double[][] data = transformFloatToDouble((float[][]) arrayObj);
            return data[xIndex][yIndex];
        }
        return null;
    }

    /**
     * object 转 double
     *
     * @param arrayObj 数组对象 二维
     * @param xIndex   x位置
     * @param yIndex   y位置
     * @return
     */
    public static Double getPointFromDataAndReversal(Object arrayObj, Integer xIndex, Integer yIndex, Boolean reversal) {
        if (arrayObj == null) {
            return null;
        }
        if (arrayObj instanceof double[][]) {
            double[][] data = (double[][]) arrayObj;
            if (reversal) {
                return data[yIndex][xIndex];
            }
            return data[xIndex][yIndex];
        }
        if (arrayObj instanceof short[][]) {
            double[][] data = transformShortToDouble((short[][]) arrayObj);
            if (reversal) {
                return data[yIndex][xIndex];
            }
            return data[xIndex][yIndex];
        }
        if (arrayObj instanceof float[][]) {
            double[][] data = transformFloatToDouble((float[][]) arrayObj);
            if (reversal) {
                return data[yIndex][xIndex];
            }
            return data[xIndex][yIndex];
        }
        return null;
    }

    /**
     * float 转 double
     *
     * @param data
     * @return
     */
    public static double[] transformFloatToDouble(float[] data) {
        if (data == null) {
            return null;
        }
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    /**
     * float 转 double
     *
     * @param data
     * @return
     */
    public static double[] transformCharToDouble(char[] data) {
        if (data == null) {
            return null;
        }
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }


    /**
     * int 转 double
     *
     * @param data
     * @return
     */
    public static double[] transformIntToDouble(int[] data) {
        if (data == null) {
            return null;
        }
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    /**
     * long 转 double
     *
     * @param data
     * @return
     */
    public static double[] transformLongToDouble(long[] data) {
        if (data == null) {
            return null;
        }
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    /**
     * byte 转 double
     *
     * @param data
     * @return
     */
    public static double[] transformByteToDouble(byte[] data) {
        if (data == null) {
            return null;
        }
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    /**
     * short 转 double
     *
     * @param data
     * @return
     */
    public static double[][] transformShortToDouble(short[][] data) {
        if (data == null) {
            return null;
        }
        double[][] result = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j];
            }
        }
        return result;
    }

    /**
     * char 转 double
     *
     * @param data
     * @return
     */
    public static double[][] transformCharToDouble(char[][] data) {
        if (data == null) {
            return null;
        }
        double[][] result = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j];
            }
        }
        return result;
    }

    /**
     * byte 转 double
     *
     * @param data
     * @return
     */
    public static double[][] transformByteToDouble(byte[][] data) {
        if (data == null) {
            return null;
        }
        double[][] result = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j];
            }
        }
        return result;
    }

    /**
     * int 转 double
     *
     * @param data
     * @return
     */
    public static double[][] transformIntToDouble(int[][] data) {
        if (data == null) {
            return null;
        }
        double[][] result = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j];
            }
        }
        return result;
    }

    /**
     * long 转 double
     *
     * @param data
     * @return
     */
    public static double[][] transformLongToDouble(long[][] data) {
        if (data == null) {
            return null;
        }
        double[][] result = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j];
            }
        }
        return result;
    }

    /**
     * float 转 double
     *
     * @param data
     * @return
     */
    public static double[][] transformFloatToDouble(float[][] data) {
        if (data == null) {
            return null;
        }
        double[][] result = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j];
            }
        }
        return result;
    }

    /**
     * float 转 double
     *
     * @param data
     * @return
     */
    public static double[][][] transformFloatToDouble(float[][][] data) {
        if (data == null) {
            return null;
        }
        double[][][] result = new double[data.length][data[0].length][data[0][0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                for (int p = 0; p < data[0][0].length; p++) {
                    result[i][j][p] = data[i][j][p];
                }
            }
        }
        return result;
    }

    /**
     * float 转 double
     *
     * @param data
     * @return
     */
    public static double[] transformShortToDouble(short[] data) {
        if (data == null) {
            return null;
        }
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    /**
     * 获取数组中的最小值
     *
     * @param data
     * @return
     */
    public static double minDoubleArray(double[] data) {
        double min = 999999;
        for (int index = 0; index < data.length; index++) {
            if ((data[index] == -999 || data[index] == -9999 || data[index] > 999999 || data[index] == 9999 || data[index] < -99999 || Double.isNaN(data[index]))) {
                continue;
            }
            // 判断数组元素的最小值
            if (data[index] < min) {
                // 把最小值存储Min变量
                min = data[index];
            }
        }
        return min;
    }

    /**
     * 获取数组中的最小值
     *
     * @param data
     * @return
     */
    public static short minShortArray(short[] data) {
        short min = Short.MAX_VALUE;
        for (int index = 0; index < data.length; index++) {
            if (data[index] == Short.MIN_VALUE || data[index] == Short.MAX_VALUE || data[index] == Short.MIN_VALUE + 1 || data[index] == Short.MAX_VALUE - 2) {
                continue;
            }
            // 判断数组元素的最小值
            if (data[index] < min) {
                // 把最小值存储Min变量
                min = data[index];
            }
        }
        return min;
    }

    /**
     * 获取数组中的最小值
     *
     * @param data
     * @return
     */
    public static double maxDoubleArray(double[] data) {
        double max = -999999;
        for (int index = 0; index < data.length; index++) {
            if ((data[index] == -999.0 || data[index] == -9999.0 || data[index] > 999999.0 || data[index] == 9999.0 || data[index] < -99999.0 || Double.isNaN(data[index]))) {
                continue;
            }
            // 判断数组元素的最小值
            if (data[index] > max) {
                // 把最大值存储Min变量
                max = data[index];
            }
        }
        return max;
    }

    /**
     * 获取数组中的最小值
     *
     * @param data
     * @return
     */
    public static short maxShortArray(short[] data) {
        short max = Short.MIN_VALUE;
        for (int index = 0; index < data.length; index++) {
            if (data[index] == Short.MIN_VALUE || data[index] == Short.MAX_VALUE || data[index] == Short.MIN_VALUE + 1 || data[index] == Short.MAX_VALUE - 2) {
                continue;
            }
            // 判断数组元素的最小值
            if (data[index] > max) {
                // 把最大值存储Min变量
                max = data[index];
            }
        }
        return max;
    }

    /**
     * @param num
     * @return
     */
    public static String scienceD(double num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        //设置保留多少为小数
        nf.setMaximumFractionDigits(5);
        //取消科学计数法
        nf.setGroupingUsed(false);


        return nf.format(num);
    }

    /**
     * Double 数组转 double 数组
     *
     * @param arr
     * @return
     */
    public static double[] convert2doubleArray(Double[] arr) {
        if (ArrayUtils.isNotEmpty(arr)) {
            return Arrays.stream(arr)
                    .filter(val -> val != null)
                    .mapToDouble(Double::doubleValue)
                    .toArray();
        }
        return null;
    }

    public static Boolean numberMaxEqualsCompare(Object a, String b) {
        if (a instanceof Double) {
            Double aDouble = (Double) a;
            Double bDouble = Double.valueOf(b);
            return aDouble >= bDouble;
        } else if (a instanceof Integer) {
            Integer aInteger = (Integer) a;
            Integer bInteger = Integer.valueOf(b);
            return aInteger >= bInteger;
        } else if (a instanceof Float) {
            Float aFloat = (Float) a;
            Float bFloat = Float.valueOf(b);
            return aFloat >= bFloat;
        } else if (a instanceof Short) {
            Short aShort = (Short) a;
            Short bShort = Short.valueOf(b);
            return aShort >= bShort;
        }
        return false;
    }

    public static Boolean numberMaxCompare(Object a, String b) {
        if (a instanceof Double) {
            Double aDouble = (Double) a;
            Double bDouble = Double.valueOf(b);
            return aDouble > bDouble;
        } else if (a instanceof Integer) {
            Integer aInteger = (Integer) a;
            Integer bInteger = Integer.valueOf(b);
            return aInteger > bInteger;
        } else if (a instanceof Float) {
            Float aFloat = (Float) a;
            Float bFloat = Float.valueOf(b);
            return aFloat > bFloat;
        } else if (a instanceof Short) {
            Short aShort = (Short) a;
            Short bShort = Short.valueOf(b);
            return aShort > bShort;
        }
        return false;
    }

    public static Boolean numberMinEqualsCompare(Object a, String b) {
        if (a instanceof Double) {
            Double aDouble = (Double) a;
            Double bDouble = Double.valueOf(b);
            return aDouble <= bDouble;
        } else if (a instanceof Integer) {
            Integer aInteger = (Integer) a;
            Integer bInteger = Integer.valueOf(b);
            return aInteger <= bInteger;
        } else if (a instanceof Float) {
            Float aFloat = (Float) a;
            Float bFloat = Float.valueOf(b);
            return aFloat <= bFloat;
        } else if (a instanceof Short) {
            Short aShort = (Short) a;
            Short bShort = Short.valueOf(b);
            return aShort <= bShort;
        }
        return false;
    }

    public static Boolean numberMinCompare(Object a, String b) {
        if (a instanceof Double) {
            Double aDouble = (Double) a;
            Double bDouble = Double.valueOf(b);
            return aDouble < bDouble;
        } else if (a instanceof Integer) {
            Integer aInteger = (Integer) a;
            Integer bInteger = Integer.valueOf(b);
            return aInteger < bInteger;
        } else if (a instanceof Float) {
            Float aFloat = (Float) a;
            Float bFloat = Float.valueOf(b);
            return aFloat < bFloat;
        } else if (a instanceof Short) {
            Short aShort = (Short) a;
            Short bShort = Short.valueOf(b);
            return aShort < bShort;
        }
        return false;
    }

    /**
     * 数组翻转
     *
     * @param ncDoubleTrue
     * @return
     */
    public static double[] toArrayReversal(double[] ncDoubleTrue, Integer oldXSize, Integer oldYSize) {
        double[] reversal = new double[ncDoubleTrue.length];
        for (int i = 0; i < oldXSize; i++) {
            for (int j = 0; j < oldYSize; j++) {
                reversal[j * oldXSize + i] = ncDoubleTrue[i * oldYSize + j];
            }
        }
        return reversal;
    }
}
