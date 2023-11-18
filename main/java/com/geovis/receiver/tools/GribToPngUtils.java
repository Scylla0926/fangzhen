package com.geovis.receiver.tools;


import com.geovis.receiver.pojo.model.DecodeConstants;
import org.springframework.stereotype.Component;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 输出png
 */
@Component
public class GribToPngUtils {

    private static GribToPngUtils instance = new GribToPngUtils();

    public GribToPngUtils() {
    }

    public static GribToPngUtils getInstance() {
        return instance;
    }

    public List<Object> png(String outPath, double[] datas, int width, int height, double lonMin, double latMin, double lonMax, double latMax, double lonStep, double latStep, String unit) {
        List<Object> result = new ArrayList<Object>();
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//	    Graphics g = bufImg.getGraphics();
        Graphics2D g2d = bufImg.createGraphics();
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(0, 0, width, height);
        g2d.fill(r);
        String prefix = new File(outPath).getName();
        double min = 999999;
        double max = -999999;
        for (int i = 0, count = datas.length; i < count; i++) {
            if (datas[i] == DecodeConstants.UNDEF_INT_VALUE) {
                continue;
            }

            if (prefix.toLowerCase().startsWith("ew") || (datas[i] == -32692.0 || datas[i] == -327.68 || datas[i] == -9999 || datas[i] > 99999 || datas[i] == 9999 || datas[i] < -99999 || datas[i] == 16959 || Double.isNaN(datas[i]))) {
                continue;
            }
            if (min > datas[i]) {
                min = datas[i];
            }
            if (max < datas[i]) {
                max = datas[i];
            }
        }
        max = Double.parseDouble(NumberFormatUtil.scienceD(max));
        min = Double.parseDouble(NumberFormatUtil.scienceD(min));

        int a = 255;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (datas[i * width + j] <= -999999 || datas[i * width + j] == -327.68 || datas[i * width + j] == -9999 || datas[i * width + j] == 9999 || datas[i * width + j] == -32692.0 || datas[i * width + j] >= 99999 || datas[i * width + j] == 16959 || Double.isNaN(datas[i * width + j])) {
                    a = 0;
                } else {
                    a = 255;
                }
//	    		color = new Color(getRgbaValue(min, max, datas[i * width + j]), 0, 0, a);
//	    		int rgb = color.getRGB();
                int rgb = (a << 24) | (getRgbaValue(min, max, datas[i * width + j]) << 16) | (0 << 8) | 0;
                bufImg.setRGB(j, i, rgb);
            }
        }
        g2d.dispose();

        File file = new File(outPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(bufImg, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lonMin == 0 && lonMax == 360) {
            lonMin = -180;
            lonMax = 180;
        }
        String json = "{\"min\": " + min + ", \"max\": " + max + ", \"width\": " + width + ", \"height\": " + height + ", \"lonmin\": " + lonMin + ", \"latmin\": "
                + latMin + ", \"lonmax\": " + lonMax + ", \"latmax\": " + latMax + ", \"lonstep\": " + lonStep + ", \"latstep\": " + latStep + ", \"unit\": \"" + unit + "\"}";
        FileUtil.writeStrToFile(json, outPath.replace(".png", ".json"));

        return result;
    }

    public List<Object> png(String outPath, double[] datas, int width, int height, double lonMin, double latMin, double lonMax, double latMax, double lonStep, double latStep, String unit, double fileValue) {
        List<Object> result = new ArrayList<Object>();
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//	    Graphics g = bufImg.getGraphics();
        Graphics2D g2d = bufImg.createGraphics();
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(0, 0, width, height);
        g2d.fill(r);
        String prefix = new File(outPath).getName();
        double min = 999999;
        double max = -999999;
        for (int i = 0, count = datas.length; i < count; i++) {
            if (datas[i] == DecodeConstants.UNDEF_INT_VALUE) {
                continue;
            }

            if (prefix.toLowerCase().startsWith("ew") || (datas[i] == fileValue || datas[i] == -32692.0 || datas[i] == -327.68 || datas[i] == -9999 || datas[i] > 999999 || datas[i] == 9999 || datas[i] < -99999 || datas[i] == 16959 || Double.isNaN(datas[i]))) {
                continue;
            }
            if (min > datas[i]) {
                min = datas[i];
            }
            if (max < datas[i]) {
                max = datas[i];
            }
        }
        max = Double.parseDouble(NumberFormatUtil.scienceD(max));
        min = Double.parseDouble(NumberFormatUtil.scienceD(min));

        int a = 255;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (datas[i * width + j] <= -999999 || datas[i * width + j] == fileValue || datas[i * width + j] == -327.68 || datas[i * width + j] == -9999 || datas[i * width + j] == 9999 || datas[i * width + j] == -32692.0 || datas[i * width + j] >= 999999 || datas[i * width + j] == 16959 || Double.isNaN(datas[i * width + j])) {
                    a = 0;
                } else {
                    a = 255;
                }
//	    		color = new Color(getRgbaValue(min, max, datas[i * width + j]), 0, 0, a);
//	    		int rgb = color.getRGB();
                int rgb = (a << 24) | (getRgbaValue(min, max, datas[i * width + j], fileValue) << 16) | (0 << 8) | 0;
                bufImg.setRGB(j, i, rgb);
            }
        }
        g2d.dispose();

        File file = new File(outPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(bufImg, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lonMin == 0 && lonMax == 360) {
            lonMin = -180;
            lonMax = 180;
        }
        String json = "{\"min\": " + min + ", \"max\": " + max + ", \"width\": " + width + ", \"height\": " + height + ", \"lonmin\": " + lonMin + ", \"latmin\": "
                + latMin + ", \"lonmax\": " + lonMax + ", \"latmax\": " + latMax + ", \"lonstep\": " + lonStep + ", \"latstep\": " + latStep + ", \"unit\": \"" + unit + "\"}";
        FileUtil.writeStrToFile(json, outPath.replace(".png", ".json"));

        return result;
    }

    public List<Object> png(String outPath, double[][] datas, double lonMin, double latMin, double lonMax, double latMax, double lonStep, double latStep, String unit) {
        List<Object> result = new ArrayList<Object>();
        int height = datas.length;
        int width = datas[0].length;
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = bufImg.createGraphics();
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(0, 0, width, height);
        g2d.fill(r);
        String prefix = new File(outPath).getName();
        double min = 999999;
        double max = -999999;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (datas[i][j] == DecodeConstants.UNDEF_INT_VALUE) {
                    continue;
                }
                if (prefix.toLowerCase().startsWith("ew") && (datas[i][j] == -999 || datas[i][j] == -9999)) {
                    continue;
                }
                if (min > datas[i][j]) {
                    min = datas[i][j];
                }
                if (max < datas[i][j]) {
                    max = datas[i][j];
                }
            }
        }
        max = Double.parseDouble(NumberFormatUtil.scienceD(max));
        min = Double.parseDouble(NumberFormatUtil.scienceD(min));

        int a = 255;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (datas[i][j] == 999999 || datas[i][j] == -999 || datas[i][j] == -9999) {
                    a = 0;
                } else {
                    a = 255;
                }
                int rgb = (a << 24) | (getRgbaValue(min, max, datas[i][j]) << 16) | (0 << 8) | 0;
                bufImg.setRGB(j, i, rgb);
            }
        }
        g2d.dispose();

        File file = new File(outPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(bufImg, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lonMin == 0 && lonMax == 360) {
            lonMin = -180;
            lonMax = 180;
        }
        String json = "{\"min\": " + min + ", \"max\": " + max + ", \"width\": " + width + ", \"height\": " + height + ", \"lonmin\": " + lonMin + ", \"latmin\": "
                + latMin + ", \"lonmax\": " + lonMax + ", \"latmax\": " + latMax + ", \"lonstep\": " + lonStep + ", \"latstep\": " + latStep + ", \"unit\": \"" + unit + "\"}";
        FileUtil.writeStrToFile(json, outPath.replace(".png", ".json"));

        return result;
    }

    public List<Object> png(String outPath, double[] datas, double[] datas2, int width, int height, double lonMin, double latMin, double lonMax, double latMax, double lonStep, double latStep, String unit) {
        List<Object> result = new ArrayList<Object>();
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//	    Graphics g = bufImg.getGraphics();
        Graphics2D g2d = bufImg.createGraphics();
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(0, 0, width, height);
        g2d.fill(r);
        String prefix = new File(outPath).getName();
        double[] maxMin = maxMin(prefix, datas);
        double[] maxMin2 = maxMin(prefix, datas2);

        int a = 255;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
//				color = new Color(getRgbaValue(maxMin[1], maxMin[0], datas[i * width + j]), getRgbaValue(maxMin2[1], maxMin2[0], datas2[i * width + j]), 0, 255);
//				int rgb = color.getRGB();
                if (datas[i * width + j] == 999999 || datas[i * width + j] == -9.999999790214768E33 || datas[i * width + j] == -32692.0 || datas[i * width + j] == -9999 || datas2[i * width + j] == 999999 || datas2[i * width + j] == -999 || datas2[i * width + j] >= 9999 || Double.isNaN(datas2[i * width + j])) {
                    a = 0;
                } else {
                    a = 255;
                }
                int rgb = (a << 24) | (getRgbaValue(maxMin[1], maxMin[0], datas[i * width + j]) << 16) | (getRgbaValue(maxMin2[1], maxMin2[0], datas2[i * width + j]) << 8) | 0;
                bufImg.setRGB(j, i, rgb);
            }
        }
        g2d.dispose();

        File file = new File(outPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(bufImg, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lonMin == 0 && lonMax == 360) {
            lonMin = -180;
            lonMax = 180;
        }
        String json = "{\"minu\": " + maxMin[1] + ", \"maxu\": " + maxMin[0] + ", \"minv\": " + maxMin2[1] + ", \"maxv\": " + maxMin2[0] + ", \"width\": " + width + ", \"height\": "
                + height + ", \"lonmin\": " + lonMin + ", \"latmin\": " + latMin + ", \"lonmax\": " + lonMax + ", \"latmax\": " + latMax + ", \"lonstep\": " + lonStep + ", \"latstep\": "
                + latStep + ", \"unit\": \"" + unit + "\"}";
        FileUtil.writeStrToFile(json, outPath.replace(".png", ".json"));

        return result;
    }

    public List<Object> png(String outPath, double[] datas, double[] datas2, int width, int height, double lonMin, double latMin, double lonMax, double latMax, double lonStep, double latStep, String unit, double fileValueU, double fileValueV) {
        List<Object> result = new ArrayList<Object>();
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//	    Graphics g = bufImg.getGraphics();
        Graphics2D g2d = bufImg.createGraphics();
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(0, 0, width, height);
        g2d.fill(r);
        String prefix = new File(outPath).getName();
        double[] maxMin = maxMin(prefix, datas, fileValueU);
        double[] maxMin2 = maxMin(prefix, datas2, fileValueV);

        int a = 255;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
//				color = new Color(getRgbaValue(maxMin[1], maxMin[0], datas[i * width + j]), getRgbaValue(maxMin2[1], maxMin2[0], datas2[i * width + j]), 0, 255);
//				int rgb = color.getRGB();
                if (datas[i * width + j] == fileValueU || datas[i * width + j] == fileValueV || datas[i * width + j] == -32692.0 || datas[i * width + j] == -9999 || datas2[i * width + j] == 999999 || datas2[i * width + j] == -999 || datas2[i * width + j] >= 16959 || Double.isNaN(datas2[i * width + j])) {
                    a = 0;
                } else {
                    a = 255;
                }
                int rgb = (a << 24) | (getRgbaValue(maxMin[1], maxMin[0], datas[i * width + j], fileValueU) << 16) | (getRgbaValue(maxMin2[1], maxMin2[0], datas2[i * width + j], fileValueV) << 8) | 0;
                bufImg.setRGB(j, i, rgb);
            }
        }
        g2d.dispose();

        File file = new File(outPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(bufImg, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lonMin == 0 && lonMax == 360) {
            lonMin = -180;
            lonMax = 180;
        }
        String json = "{\"minu\": " + maxMin[1] + ", \"maxu\": " + maxMin[0] + ", \"minv\": " + maxMin2[1] + ", \"maxv\": " + maxMin2[0] + ", \"width\": " + width + ", \"height\": "
                + height + ", \"lonmin\": " + lonMin + ", \"latmin\": " + latMin + ", \"lonmax\": " + lonMax + ", \"latmax\": " + latMax + ", \"lonstep\": " + lonStep + ", \"latstep\": "
                + latStep + ", \"unit\": \"" + unit + "\"}";
        FileUtil.writeStrToFile(json, outPath.replace(".png", ".json"));

        return result;
    }

    public List<Object> png(String outPath, double[][] datas, double[][] datas2, double lonMin, double latMin, double lonMax, double latMax, double lonStep, double latStep, String unit) {
        List<Object> result = new ArrayList<Object>();
        int height = datas.length;
        int width = datas[0].length;
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//	    Graphics g = bufImg.getGraphics();
        Graphics2D g2d = bufImg.createGraphics();
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(0, 0, width, height);
        g2d.fill(r);
        double[] maxMin = maxMin(datas);
        double[] maxMin2 = maxMin(datas2);
//		for(int i = 0; i < height; i++)
//	    {
//	    	for(int j = 0; j < width; j++)
//	    	{
//	    		g.setColor(new Color(getRgbaValue(maxMin[1], maxMin[0], datas[i * width + j]), getRgbaValue(maxMin2[1], maxMin2[0], datas2[i * width + j]), 0, 255));
//	    		g.drawLine(j, i, j, i);
//	    	}
//	    }

//		Color color = null;
        int a = 255;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
//				color = new Color(getRgbaValue(maxMin[1], maxMin[0], datas[i * width + j]), getRgbaValue(maxMin2[1], maxMin2[0], datas2[i * width + j]), 0, 255);
//				int rgb = color.getRGB();
                if (datas[i][j] == 999999) {
                    a = 0;
                } else {
                    a = 255;
                }
                int rgb = (a << 24) | (getRgbaValue(maxMin[1], maxMin[0], datas[i][j]) << 16) | (getRgbaValue(maxMin2[1], maxMin2[0], datas2[i][j]) << 8) | 0;
                bufImg.setRGB(j, i, rgb);
            }
        }
        g2d.dispose();

        File file = new File(outPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(bufImg, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lonMin == 0 && lonMax == 360) {
            lonMin = -180;
            lonMax = 180;
        }
        String json = "{\"umin\": " + maxMin[1] + ", \"umax\": " + maxMin[0] + ", \"vmin\": " + maxMin2[1] + ", \"vmax\": " + maxMin2[0] + ", \"width\": " + width + ", \"height\": "
                + height + ", \"lonmin\": " + lonMin + ", \"latmin\": " + latMin + ", \"lonmax\": " + lonMax + ", \"latmax\": " + latMax + ", \"lonstep\": " + lonStep + ", \"latstep\": "
                + latStep + ", \"unit\": \"" + unit + "\"}";
        FileUtil.writeStrToFile(json, outPath.replace(".png", ".json"));

        return result;
    }

    private int getRgbaValue(double min, double max, double value) {
        if (value == DecodeConstants.UNDEF_INT_VALUE || value == -9999 || value == -327.68 || value == -9999 || value < -999999 || value == -32692.0 || value == 9999 || value > 99999 || value == 16959 || Double.isNaN(value)) {
            return 0;
        }
        return (int) (Math.abs((value - min) / (max - min)) * 255);
    }

    private int getRgbaValue(double min, double max, double value, double fileValue) {
        if (value == DecodeConstants.UNDEF_INT_VALUE || value == fileValue || value == -9999 || value == -327.68 || value > 999999 || value < -999999 || value == -32692.0 || value == -9.999999790214768E33 || value == 9999 || value > 999999 || value == 16959 || Double.isNaN(value)) {
            return 0;
        }
        return (int) (Math.abs((value - min) / (max - min)) * 255);
    }

    private double[] maxMin(String prefix, double[] datas) {
        double min = 999999;
        double max = -999999;
        for (int i = 0, count = datas.length; i < count; i++) {
            if (datas[i] == DecodeConstants.UNDEF_INT_VALUE) {
                continue;
            }
            if (prefix.toLowerCase().startsWith("ew") || (datas[i] == -999 || datas[i] == -9999 || datas[i] == -9.999999790214768E33 || datas[i] == -32692.0 || datas[i] == 9999 || datas[i] >= 9999 || Double.isNaN(datas[i]))) {
                continue;
            }
            if (min > datas[i]) {
                min = datas[i];
            }
            if (max < datas[i]) {
                max = datas[i];
            }
        }
        max = Double.parseDouble(NumberFormatUtil.scienceD(max));
        min = Double.parseDouble(NumberFormatUtil.scienceD(min));

        return new double[]{max, min};
    }

    private double[] maxMin(String prefix, double[] datas, double fileValue) {
        double min = 999999;
        double max = -999999;
        for (int i = 0, count = datas.length; i < count; i++) {
            if (datas[i] == DecodeConstants.UNDEF_INT_VALUE) {
                continue;
            }
            if (prefix.toLowerCase().startsWith("ew") || (datas[i] == -999 || datas[i] >= 16959 || datas[i] == -9999 || datas[i] == fileValue || datas[i] == -32692.0 || datas[i] == 9999 || Double.isNaN(datas[i]))) {
                continue;
            }
            if (min > datas[i]) {
                min = datas[i];
            }
            if (max < datas[i]) {
                max = datas[i];
            }
        }
        max = Double.parseDouble(NumberFormatUtil.scienceD(max));
        min = Double.parseDouble(NumberFormatUtil.scienceD(min));

        return new double[]{max, min};
    }

    private double[] maxMin(double[][] datas) {
        double min = 999999;
        double max = -999999;
        for (int i = 0, height = datas.length; i < height; i++) {
            for (int j = 0, width = datas[i].length; j < width; j++) {
                if (datas[i][j] == DecodeConstants.UNDEF_INT_VALUE) {
                    continue;
                }
                if (min > datas[i][j]) {
                    min = datas[i][j];
                }
                if (max < datas[i][j]) {
                    max = datas[i][j];
                }
            }
        }

        return new double[]{max, min};
    }
}
