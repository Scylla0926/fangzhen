package com.geovis.receiver.readData.nc;

import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.tools.*;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理5维数据
 */
public class WriteFileJobF implements Runnable {
	private String outFilePath;
	private CopyOnWriteArrayList<String> outPathes;
	private GribToPngUtils png;
	private NcBeanPg pg = new NcBeanPg();
	private NcBeanPg pg_o = new NcBeanPg();
	private CountDownLatch latch;
	private String prefixFileName;
	private float[] ps;
	private Map<String, Object> datasMap;
	private String elName;
	private String element;
	private String layerName;
	private int i;
	private String outPath;
	private AtomicInteger total;
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	public WriteFileJobF(Map<String, Object> datasMap, float[] ps, String prefixFileName, String elName, String element, String layerName, String outPath, int i, String outFilePath, CopyOnWriteArrayList<String> outPathes, GribToPngUtils png, NcBeanPg pg, CountDownLatch latch) {
		this.datasMap = datasMap;
		this.ps = ps;
		this.prefixFileName = prefixFileName;
		this.elName = elName;
		this.element = element;
		this.outPath = outPath;
		this.layerName = layerName;
		this.i = i;
		this.outFilePath = outFilePath;
		this.outPathes = outPathes;
		this.png = png;
		this.pg_o = pg;
		copy(pg, this.pg);
		this.latch = latch;
		this.total = total;
		
		if(prefixFileName.toLowerCase().startsWith("ew"))
		{
			pg.lats = -10;
			pg.late = 55;
			pg.latstep = 0.05;
		}
	}
	
	private void copy(NcBeanPg from, NcBeanPg to)
	{
		TransObjUtil.transObj(from, to);
	}
	
	@Override
	public void run() {
//		System.out.println("start1 >>>>>>>>>>>>>>>> " + elName);
		Thread.currentThread().setName(prefixFileName);
		double[][] datas = null;
		
		String[] dimensions = NcReaderUtils.getDimensions(datasMap, elName);
		if(elName.startsWith("xxx"))
		{
			dimensions[2] = elName.substring(0, 4) + dimensions[2];
		}
		double[] valueByName = NcReaderUtils.readByName(datasMap, dimensions[2]);
		double[][][] ds = NcReaderUtils.readByNameLayerF(datasMap, elName, String.valueOf(i));
		
//		System.out.println("mid0 >>>>>>>>>>>>>>>> " + layerName);
		String prefix = "";
		double[] interval = NcReaderUtils.getGridInterval(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat,prefixFileName);
		
		
		int index = CalculateIndexUtils.getIndex(pg.lons, pg.lonstep);
		if(pg.lons == 0 && (pg.lone == 360 || pg.lone == Math.abs(360 - interval[0])))
		{
			datas = ArrayReverseUtils.reverseArray(datas, index);
			pg.lons = (pg.lons - 180);
			pg.lone = (pg.lone - 180);
			pg_o.lons = pg.lons;
			pg_o.lone = pg.lone;
		}
		else if(pg.lons >= 180)
		{
			pg.lons = (pg.lons - 360);
			pg.lone = (pg.lone - 360);
			pg_o.lons = pg.lons;
			pg_o.lone = pg.lone;
		}
		else if(pg.lons < 180 && pg.lone > 180)
		{
			pg.lone = (pg.lone - 360);
			pg_o.lone = pg.lone;
		}

		if(prefixFileName.contains(".wave.nc"))
		{
			layerName = (int) ((ps[i] - ps[0]) * 24) + "";
		}
		
		double[] lonWestLatSouth = NcReaderUtils.getLonWestLatSouth(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat);

//		System.out.println("mid1 >>>>>>>>>>>>>>>> " + layerName);
		
		String elementUnit = NcReaderUtils.getElementUnit(datasMap, element);
		String unit = null;
		if(pg.pressLayerUnit.toLowerCase().contains("pa") || pg.pressLayerUnit.toLowerCase().endsWith("m"))
		{
			unit = pg.pressLayerUnit;
		}
		else
		{
			unit ="";
		}

		String layer = "";
		for(int i = 0, num = ds.length; i < num; i++)
		{
			layer = NumberFormatUtil.scienceD(valueByName[i]);
//
			datas = ds[i];
			if(interval[1] > 0)
			{
				double[][] datas1 = new double[datas.length][];
				for(int m = 0, count = datas.length; m < count; m++)
				{
					datas1[m] = datas[count - 1 - m];
				}
				datas = datas1;
			}
			outFilePath = outPath + prefixFileName + "_" + element + "_" + this.i + "_" + layer + unit + "_" + pg.fileDate.substring(0, 8) + pg.fcTime;
			
//		System.out.println("数据准备耗时: " + (System.currentTimeMillis() - time));
			
//		time = System.currentTimeMillis();
			//******************************************
			FileUtil.writeBytesToFile(outFilePath + ".bin", datas);
			outPathes.add(outFilePath + ".bin");
			String enviTxt = EnviTxtUtil.getEnviTxt(datas[0].length, datas.length, 4, lonWestLatSouth[0], lonWestLatSouth[1], interval[0], interval[1], elementUnit);
			FileUtil.writeStrToFile(enviTxt, outFilePath + ".hdr");
			outPathes.add(outFilePath + ".hdr");
			png.png(outFilePath + ".png", datas, pg.lons, pg.lats, pg.lone, pg.late, pg.lonstep, pg.latstep, elementUnit);
			
			outPathes.add(outFilePath + ".png");
			outPathes.add(outFilePath + ".json");
//		System.out.println(elName + "_" + ps[i] + ":" + (DecodeGribFile.total.floatValue()) + "/" + Integer.parseInt(datasMap.get("pngCount").toString()));
//			System.out.println("total=" + total.floatValue());
			total.getAndIncrement();
			float p = (total.floatValue() / Integer.parseInt(datasMap.get("pngCount").toString())) * 100;
			System.out.printf("文件" + prefixFileName + " " + element + "已完成 %s%%\n", df.format(p));
			if(p == 100)
			{
				total.set(0);
			}
			latch.countDown();

		}
		

//		System.out.println("end >>>>>>>>>>>>>>>> " + layerName);
	}

}
