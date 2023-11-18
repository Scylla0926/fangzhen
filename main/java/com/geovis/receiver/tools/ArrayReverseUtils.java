package com.geovis.receiver.tools;

import cn.hutool.core.util.ArrayUtil;
import com.geovis.receiver.pojo.model.NcBeanPg;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

/***
 * 处理数组 工具类
 */
public class ArrayReverseUtils {


    /**
     * 倒叙 将二维数组 转为  一位数组
     *
     * @param datas
     * @return
     */
    public static double[] trans(double[][] datas) {
        int count = datas.length;
        int ii = datas[0].length;
        double[] result = new double[count * ii];
        for (int i = count - 1; i >= 0; i--) {
            for (int j = 0, num = datas[i].length; j < num; j++) {
                result[(count - i - 1) * num + j] = datas[i][j];
            }
        }
        return result;
    }

    /**
     * 倒叙 将二维数组
     *
     * @param datas
     * @return
     */
    public static double[][] transToArray2(double[][] datas) {
        int count = datas.length;
        int index = 0;
        double[][] result = new double[datas.length][datas[0].length];
        for (int i = count; i > 0; i--) {
            result[index] = datas[i];
            index++;
        }
        return result;
    }


    /**
     * 正序 将二维数组 转为 一维数组
     *
     * @param datas
     * @return
     */
    public static double[] transToOneArray(double[][] datas) {
        int count = datas.length;
        int ii = datas[0].length;
        double[] result = new double[count * ii];
        for (int i = 0; i < count; i++) {
            for (int j = 0, num = datas[i].length; j < num; j++) {
                result[i * num + j] = datas[i][j];
            }

        }
        return result;
    }

    /**
     * @param datas 数组
     * @param index 对调索引位置
     * @return
     * @category 将数组数据在索引位置处按列对调数据并且上下对调
     */
    public static double[] trans_reverse(double[][] datas, int index) {
        int count = datas.length;
        int ii = datas[0].length;
        double[] result = new double[count * ii];
        double temp;
        for (int i = count - 1; i >= 0; i--) {
            for (int j = 0, num = datas[i].length; j < num; j++) {
                result[(count - i - 1) * num + j] = datas[i][j];
            }

            for (int j = 0, num = datas[i].length; j < index; j++) {
                temp = result[(count - i - 1) * num + j];
                result[(count - i - 1) * num + j] = result[(count - i - 1) * num + (num - index) + j];
                result[(count - i - 1) * num + (num - index) + j] = temp;
                result[(count - i - 1) * num + index] = result[(count - i - 1) * num + (num - index)];
            }
        }


        return result;
    }

    /**
     * @param datas 数组
     * @param index 对调索引位置
     * @return
     * @category 将数组数据在索引位置处按列对调数据
     */
    public static double[] transReverse(double[][] datas, int index) {
        int count = datas.length;
        int ii = datas[0].length;
        double[] result = new double[count * ii];
        double temp;
        for (int i = 0; i < count; i++) {
            for (int j = 0, num = datas[i].length; j < num; j++) {
                result[i * num + j] = datas[i][j];
            }

            for (int j = 0, num = datas[i].length; j < index; j++) {
                temp = result[i * num + j];
                result[i * num + j] = result[i * num + (num - index) + j];
                result[i * num + (num - index) + j] = temp;
                result[i * num + index] = result[i * num + (num - index)];
            }
        }


        return result;
    }


    public static double[] transReverseCopy(double[][] datas, int index) {
        int count = datas.length;
        int ii = datas[0].length;
        double[] result = new double[count * ii];
        for (int i = 0; i < count; i++) {

            double[] tts1 = Arrays.copyOfRange(datas[i], 0, index);
            double[] tts2 = Arrays.copyOfRange(datas[i], index, datas[i].length);

            datas[i] = ArrayUtil.addAll(tts2, tts1);

            for (int j = 0, num = datas[i].length; j < num; j++) {
                result[i * num + j] = datas[i][j];
            }

        }


        return result;
    }

    /**
     * @param datas 数组
     * @param index 对调索引位置
     * @return double[][]
     * @category 将数组数据在索引位置处按列对调数据
     */
    public static double[][] reverseArray(double[][] datas, int index) {
        int count = datas.length;
        int ii = datas[0].length;
        double[][] result = new double[count][ii];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < index; j++) {
                double[] ds1 = Arrays.copyOfRange(datas[i], 0, index);
                double[] ds2 = Arrays.copyOfRange(datas[i], index, ii);
                result[i] = ArrayUtils.addAll(ds2, ds1);
//				result[i] = ds2;
            }
        }

        return result;
    }

    /**
     * 将二维数组中 的 行和列互换
     *
     * @param datas
     * @param
     * @return
     */
    public static double[][] toArrayReversal(double[][] datas) {
        double[][] doubles = new double[datas[0].length][datas.length];
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                doubles[j][i] = datas[i][j];
            }
        }

        return doubles;
    }

    public static double[][] res(double[][] values) {
        double[][] result = new double[values.length][];
        for (int i = 0, count = values.length; i < count; i++) {
            for (int j = 0, num = values[i].length; j < num; j++) {
                result[i][j] = values[count - 1 - i][num - 1 - j];
            }
        }

        return result;
    }

    /**
     * 将二维数组的上下左右 分别反转
     *
     * @param values
     * @return
     */
    public static double[][] FootballClub(double[][] values) {
        double[][] cloned = values.clone();
        for (int i = 0; i < values.length / 2; i++) {
            double[] temp = values[i];
            values[i] = values[values.length - i - 1];
            values[values.length - i - 1] = temp;
        }

        for (int i = 0; i < cloned.length; i++) {
            for (int j = 0; j < cloned[i].length / 2; j++) {
                double temp = cloned[i][j];
                cloned[i][j] = cloned[i][cloned[i].length - j - 1];
                cloned[i][cloned[i].length - j - 1] = temp;
            }
        }
        return cloned;
    }

    public static double[] tt(double[][] values, NcBeanPg pg, int index, int width, int height) {

        double[] result = new double[width * height];
        for (int i = 0; i < pg.latnum; i++) {
            for (int j = index; j < pg.lonnum; j++) {
                result[i * width + j - index] = values[i][j];
            }
        }
        int index2 = (int) (Math.abs(pg.lone - pg.lons) / pg.lonstep);
        for (int i = 0; i < pg.latnum; i++) {
            for (int j = 0; j < index; j++) {
                result[i * width + j + index2] = values[i][j];
            }
        }
        for (int i = 0; i < pg.latnum; i++) {
            for (int j = pg.lonnum - index; j < index2; j++) {
                result[i * width + j] = 999999;
            }
        }
        return result;
    }

}
