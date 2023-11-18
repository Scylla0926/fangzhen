package com.geovis.receiver.tools;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件操作工具类
 */
public class FileUtil {

    public static byte[] getBytes(String filePath) {

        byte[] buffer = null;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                buffer = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return buffer;
    }


    public static void writeBytesToFile(String outFilePath, double[][] values) {

        File file = new File(outFilePath);
        File parent = file.getParentFile();
        if ((!parent.exists()) && !parent.mkdirs()) {
            System.out.println("文件不存在");
            return;
        }


        List<byte[]> list = new ArrayList<byte[]>();
        for (double[] value : values) {
            for (double v : value) {
                list.add(ByteUtils.getBytes(v));
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bi = new BufferedOutputStream(fos)) {
            for (byte[] value : list) {
                bi.write(value);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//			System.out.println(outFilePath + "  文件输出完成");
        }

    }

    public static void writeBytesToFile(String outFilePath, double[][] values, double[][] values1) {

        File file = new File(outFilePath);
        File parent = file.getParentFile();
        if ((!parent.exists()) && !parent.mkdirs()) {
            System.out.println("文件不存在");
            return;
        }


        List<byte[]> list = new ArrayList<>();
        for (double[] value : values) {
            for (double v : value) {
                list.add(ByteUtils.getBytes(v));
            }
        }
        for (double[] value : values1) {
            for (double v : value) {
                list.add(ByteUtils.getBytes(v));
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bi = new BufferedOutputStream(fos)) {
            for (byte[] value : list) {
                bi.write(value);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//			System.out.println(outFilePath + "  文件输出完成");
        }

    }

    public static void writeStrToFile(String value, String outPath) {
        File file = new File(outPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "utf-8");) {

            write.write(value);
            write.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取后缀
     *
     * @param filePath
     * @return
     */
    public static String getSuffix(String filePath) {
        String suffix = "";
        suffix = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        return suffix;
    }


    /**
     * 根据正则表达式获取 预报时效
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getFileTimer(String filePath, String regex) {
        String FileTimer = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("Timer")) {
                    FileTimer = matcher.group("Timer");
                } else {
                    FileTimer = "";
                }
            }
        } else {
            FileTimer = "";
        }
        return FileTimer;
    }

    /**
     * 根据正则表达式获取 算法类型
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getFileMethod(String filePath, String regex) {
        String FileTimer = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("Method")) {
                    FileTimer = matcher.group("Method");
                } else {
                    FileTimer = "";
                }
            }
        } else {
            FileTimer = "";
        }
        return FileTimer;
    }

    /**
     * 根据正则表达式获取 区域
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getFileRegion(String filePath, String regex) {
        String FileTimer = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("Region")) {
                    FileTimer = matcher.group("Region");
                } else {
                    FileTimer = "";
                }
            }
        } else {
            FileTimer = "";
        }
        return FileTimer;
    }

    /**
     * 根据正则表达式获取 预报时效
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getStatType(String filePath, String regex) {
        String FileTimer = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("StatType")) {
                    FileTimer = matcher.group("StatType");
                } else {
                    FileTimer = "";
                }
            }
        } else {
            FileTimer = "";
        }
        return FileTimer;
    }

    /**
     * 根据正则表达式获取 统计时间
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getStatTime(String filePath, String regex) {
        String statTime = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("StatTime")) {
                    statTime = matcher.group("StatTime");
                } else {
                    statTime = "";
                }
            }
        } else {
            statTime = "";
        }
        return statTime;
    }

    /**
     * 根据正则表达式获取 预报时效
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getFileForecast(String filePath, String regex) {
        String FileForecast = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("Forecast")) {
                    FileForecast = matcher.group("Forecast");
                } else {
                    FileForecast = "";
                }
            }
        } else {
            FileForecast = "";
        }
        return FileForecast;
    }

    /**
     * 根据正则表达式获取 argo站号 编号
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getFilePlatformNumber(String filePath, String regex) {
        String FileForecast = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("PlatformNumber")) {
                    FileForecast = matcher.group("PlatformNumber");
                } else {
                    FileForecast = "";
                }
            }
        } else {
            FileForecast = "";
        }
        return FileForecast;
    }

    /**
     * 根据正则表达式获取  argo 周期号
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getFileCycleNumber(String filePath, String regex) {
        String FileForecast = null;
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                if (regex.contains("CycleNumber")) {
                    FileForecast = matcher.group("CycleNumber");
                } else {
                    FileForecast = "";
                }
            }
        } else {
            FileForecast = "";
        }
        return FileForecast;
    }


    /**
     * 根据正则表达式获取文件数据时间
     *
     * @param filePath
     * @param regex
     * @return
     */
    public String getFileDate(String filePath, String regex) {
        String fileDate = "";
        File srcFile = new File(filePath);
        String fileName = srcFile.getName();
        if (null != regex && !regex.isEmpty() && !"".equals(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                //System.out.println("-------------");
                if (regex.contains("YEAR")) {
                    fileDate += matcher.group("YEAR");
                }

                if (regex.contains("MONTH")) {
                    fileDate += matcher.group("MONTH");
                }

                if (regex.contains("DAY")) {
                    fileDate += matcher.group("DAY");
                }

                if (regex.contains("HOUR")) {
                    fileDate += matcher.group("HOUR");
                }

                if (regex.contains("MINUTE")) {
                    fileDate += matcher.group("MINUTE");
                }

                if (regex.contains("SECOND")) {
                    fileDate += matcher.group("SECOND");
                }
            }
        } else {
            //System.out.println("-------------");
            fileDate = String.valueOf(srcFile.lastModified());
        }
        return fileDate;
    }
}
