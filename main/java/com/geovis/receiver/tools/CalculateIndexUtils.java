package com.geovis.receiver.tools;

/***
 * ���� ƫ����
 */
public class CalculateIndexUtils {

    public static int getIndex(double start, double step) {
        int result = 0;
        double dis = 180 - start;
        if(start < -180)
        {
            dis = Math.abs(180 + start);
        }
        result = (int) (dis / step);

        return result;
    }

    /**
     * 获取偏移大小
     *
     * @param start
     * @param step
     * @param
     * @return
     */
    public static int getInverseIndex(double start, double step) {
        int result = 0;
        double dis = 180 - start;
        result = (int) (dis / step);
        return Math.abs(result);
    }


    /**
     * 开森
     *

     * @return
     */
    public static int getIndexCur() {

        return 1000;
    }
}
