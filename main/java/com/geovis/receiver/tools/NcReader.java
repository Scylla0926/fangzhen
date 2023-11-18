package com.geovis.receiver.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Gengfangdong
 * @Description:
 * @FileName: NcReader
 * @Date: 2022/12/2 22:49
 * @Version: 1.0
 */
@Slf4j
public class NcReader {

    /**
     * 从指定nc文件中读取指定要素的一维数据
     *
     * @param variableName 要素名
     * @param filePath     文件路径
     * @return
     * @throws Exception
     */
    public static double[] readNcDouble(String variableName, String filePath) throws Exception {
        if (!new File(filePath).exists()) {
            throw new FileNotFoundException("文件 " + filePath + "不存在!");
        }
        try (NetcdfFile dataSet = NetcdfFile.open(filePath)) {
            List<Variable> variables = dataSet.getVariables();
            Variable variable = null;
            for (Variable item : variables) {
                if (StringUtils.equals(item.getShortName(), variableName)) {
                    variable = item;
                    break;
                }
            }
            if (variable != null) {
                // 读取数据
                Array array = variable.read();
                Object objectArray = array.copyTo1DJavaArray();
                if (objectArray != null) {
                    if (objectArray instanceof double[]) {
                        return (double[]) objectArray;
                    } else if (objectArray instanceof float[]) {
                        return NumberUtils.transformFloatToDouble((float[]) objectArray);
                    } else if (objectArray instanceof char[]) {
                        return NumberUtils.transformCharToDouble((char[]) objectArray);
                    } else if (objectArray instanceof short[]) {
                        return NumberUtils.transformShortToDouble((short[]) objectArray);
                    } else if (objectArray instanceof int[]) {
                        return NumberUtils.transformIntToDouble((int[]) objectArray);
                    } else if (objectArray instanceof long[]) {
                        return NumberUtils.transformLongToDouble((long[]) objectArray);
                    }
                }
            }
            return null;
        }
    }

    /**
     * 要素列表转map key为shortname value为要素
     *
     * @param variableList 要素列表
     * @return
     */
    public static Map<String, Variable> variablesTranToShortNameVariableMap(List<Variable> variableList) {
        return variableList.stream().collect(Collectors.toMap(item -> item.getShortName(), item -> item));
    }

    /**
     * 读取指定范围的nc数据 并且输出为double 一维数组
     *
     * @param variableName 要素名
     * @param variable     要素
     * @param org          起始
     * @param sha          终止
     * @return
     */
    public static double[] readNcDouble(String variableName, Variable variable, int[] org, int[] sha) {
        try {
            if (variable != null) {
                // 读取数据
                Array array = variable.read(org, sha);
                Object objectArray = array.copyTo1DJavaArray();
                if (objectArray != null) {
                    if (objectArray instanceof double[]) {
                        return (double[]) objectArray;
                    } else if (objectArray instanceof float[]) {
                        return NumberUtils.transformFloatToDouble((float[]) objectArray);
                    } else if (objectArray instanceof char[]) {
                        return NumberUtils.transformCharToDouble((char[]) objectArray);
                    } else if (objectArray instanceof short[]) {
                        return NumberUtils.transformShortToDouble((short[]) objectArray);
                    } else if (objectArray instanceof int[]) {
                        return NumberUtils.transformIntToDouble((int[]) objectArray);
                    } else if (objectArray instanceof long[]) {
                        return NumberUtils.transformLongToDouble((long[]) objectArray);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从要素中获取一维数据
     *
     * @param variable 要素实体
     * @return
     */
    public static double[] read1DNcDouble(Variable variable) {
        try {
            if (variable != null) {
                // 读取数据
                Array array = variable.read();
                Object objectArray = array.copyTo1DJavaArray();
                if (objectArray != null) {
                    if (objectArray instanceof double[]) {
                        return (double[]) objectArray;
                    } else if (objectArray instanceof float[]) {
                        return NumberUtils.transformFloatToDouble((float[]) objectArray);
                    } else if (objectArray instanceof char[]) {
                        return NumberUtils.transformCharToDouble((char[]) objectArray);
                    } else if (objectArray instanceof short[]) {
                        return NumberUtils.transformShortToDouble((short[]) objectArray);
                    } else if (objectArray instanceof int[]) {
                        return NumberUtils.transformIntToDouble((int[]) objectArray);
                    } else if (objectArray instanceof long[]) {
                        return NumberUtils.transformLongToDouble((long[]) objectArray);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[0];
    }

    /**
     * 读取文件下这个要素的指定范围的数据
     *
     * @param variableName
     * @param filePath
     * @param org
     * @param sha
     * @return
     * @throws Exception
     */
    public static double[] readNcData(String variableName, String filePath, int[] org, int[] sha) throws Exception {
        NetcdfFile dataSet = NetcdfFile.open(filePath);
        List<Variable> variables = dataSet.getVariables();
        Variable variable = null;
        for (Variable item : variables) {
            if (StringUtils.equals(item.getShortName(), variableName)) {
                variable = item;
                break;
            }
        }
        if (variable != null) {
            // 获取维度
            int rank = variable.getRank();
            Array array = variable.read(org, sha);
            Object objectArray = array.copyTo1DJavaArray();
            if (objectArray instanceof double[]) {
                return (double[]) objectArray;

            } else if (objectArray instanceof float[]) {
                return NumberUtils.transformFloatToDouble((float[]) objectArray);

            }
        }
        return null;
    }

    /**
     * 获取真实的数据 数据会经历 data * scaleFactor + addOffset
     *
     * @param variable
     * @param org
     * @param sha
     * @return
     */
    public static double[] readNcDoubleTrue(Variable variable, int[] org, int[] sha) {
        try {
            if (variable != null) {
                // 获取填充值
                List<Attribute> attributes = variable.getAttributes();
                // 读取数据
                double fillValue = Double.MAX_VALUE;
                // 获取填充值
                for (Attribute attribute : attributes) {
                    String shortName = attribute.getShortName();
                    if (StringUtils.equals(shortName, "_FillValue")) {
                        fillValue = getVariableAttributeValue(attribute, fillValue);
                    }
                }
                // 获取倍数
                double scaleFactor = 1.0;
                // 获取填充值
                for (Attribute attribute : attributes) {
                    String shortName = attribute.getShortName();
                    if (StringUtils.equals(shortName, "scale_factor")) {
                        scaleFactor = getVariableAttributeValue(attribute, scaleFactor);
                    }
                }
                // 获取偏移
                double addOffset = 0.0;
                // 获取填充值
                for (Attribute attribute : attributes) {
                    String shortName = attribute.getShortName();
                    if (StringUtils.equals(shortName, "add_offset")) {
                        addOffset = getVariableAttributeValue(attribute, addOffset);
                    }
                }
                // 获取维度
                int rank = variable.getRank();
                if (rank != org.length || rank != sha.length) {

                }
                // 起始维度数组 要素读取数据的大小
                Array array = variable.read(org, sha);
                Object objectArray = array.copyTo1DJavaArray();
                if (objectArray != null) {
                    double[] transform = NumberUtils.transformToDouble(objectArray);
                    double[] result = new double[transform.length];
                    for (int i = 0; i < transform.length; i++) {
                        if (transform[i] == (double) fillValue) {

                        } else {
                            result[i] = transform[i] * scaleFactor + addOffset;
                        }
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取描述的数据信息
     *
     * @param attribute
     * @param defaultValue
     * @return
     */
    public static double getVariableAttributeValue(Attribute attribute, double defaultValue) {
        double value = defaultValue;
        DataType dataType = attribute.getDataType();
        if (DataType.SHORT.equals(dataType)) {
            value = (short) attribute.getValue(0);
        }
        if (DataType.DOUBLE.equals(dataType)) {
            value = (double) attribute.getValue(0);
        }
        if (DataType.FLOAT.equals(dataType)) {
            value = (float) attribute.getValue(0);
        }
        return value;
    }

    /**
     * 读取short一维数据
     *
     * @param variableName
     * @param filePath
     * @return
     * @throws Exception
     */
    public static short[] readNcShort(String variableName, String filePath) throws Exception {
        NetcdfFile dataSet = NetcdfFile.open(filePath);
        List<Variable> variables = dataSet.getVariables();
        Variable variable = null;
        for (Variable item : variables) {
            if (StringUtils.equals(item.getShortName(), variableName)) {
                variable = item;
                break;
            }
        }
        if (variable != null) {
            Array array = variable.read();
            Object objectArray = array.copyTo1DJavaArray();
            if (objectArray != null) {
                if (objectArray instanceof short[]) {
                    return (short[]) objectArray;
                }
            }
        }
        return null;
    }

    /**
     * 读取1维数据 float数据
     *
     * @param variableName 要素名
     * @param filePath     文件路径
     * @return
     * @throws Exception
     */
    public static float[] readNcFloat(String variableName, String filePath) {
        NetcdfFile dataSet = null;
        try {
            dataSet = NetcdfFile.open(filePath);
            List<Variable> variables = dataSet.getVariables();
            Variable variable = null;
            for (Variable item : variables) {
                if (StringUtils.equals(item.getShortName(), variableName)) {
                    variable = item;
                    break;
                }
            }
            if (variable != null) {
                // 读取数据
                Array array = variable.read();
                Object objectArray = array.copyTo1DJavaArray();
                if (objectArray != null) {
                    if (objectArray instanceof float[]) {
                        return (float[]) objectArray;

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("操作nc文件{},要素{},失败:{}!", filePath, variableName, e);
        } finally {
            if (dataSet != null) {
                try {
                    dataSet.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 读取nc文件 获取数据兑现
     *
     * @param variableName
     * @param filePath
     * @return
     * @throws Exception
     */
    public static Object readNcData(String variableName, String filePath) throws Exception {
        try (NetcdfFile dataSet = NetcdfFile.open(filePath);) {
            List<Variable> variables = dataSet.getVariables();
            Variable variable = null;
            for (Variable item : variables) {
                if (StringUtils.equals(item.getShortName(), variableName)) {
                    variable = item;
                    break;
                }
            }
            if (variable != null) {
                Array array = variable.read();
                Object objectArray = array.copyTo1DJavaArray();
                return objectArray;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        readNcData("lat", "D:\\\\2195\\\\data\\\\2019\\\\CA20190101.nc");
    }

    /**
     * 读取3维数据中的第一层 -> 2维数据 返回double
     *
     * @param variableName
     * @param filePath
     * @param org
     * @param sha
     * @return
     * @throws Exception
     */
    public static double[][] read2DNcDataFrom3D(String variableName, String filePath, int[] org, int[] sha) throws Exception {
        try (NetcdfFile dataSet = NetcdfFile.open(filePath);) {
            List<Variable> variables = dataSet.getVariables();
            Variable variable = null;
            for (Variable item : variables) {
                if (StringUtils.equals(item.getShortName(), variableName)) {
                    variable = item;
                    break;
                }
            }
            if (variable != null) {
                // 获取维度
                int rank = variable.getRank();
                if (rank >= 3) {
                    Array array = variable.read(org, sha);
                    Object objectArray = array.copyToNDJavaArray();
                    if (objectArray instanceof double[][][]) {
                        return ((double[][][]) objectArray)[0];

                    }
                    if (objectArray instanceof float[][][]) {
                        return NumberUtils.transformFloatToDouble(((float[][][]) objectArray)[0]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 数据要素的单位
     *
     * @param variableName
     * @param filePath
     * @return
     */
    public static String getElementUnit(String variableName, String filePath) throws Exception {
        try (NetcdfFile dataSet = NetcdfFile.open(filePath);) {
            List<Variable> variables = dataSet.getVariables();
            Variable variable = null;
            for (Variable item : variables) {
                if (StringUtils.equals(item.getShortName(), variableName)) {
                    variable = item;
                    break;
                }
            }
            if (variable != null) {
                String result = variable.getUnitsString();

                if (result == null) {
                    result = "";
                }
                result = result.replace(" ", ".").replace("**", "");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


}