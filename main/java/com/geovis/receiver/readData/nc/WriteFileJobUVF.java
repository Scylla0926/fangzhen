package com.geovis.receiver.readData.nc;

import com.geovis.receiver.pojo.model.NcBeanPg;
import com.geovis.receiver.tools.*;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理 五维 风产品
 */
public class WriteFileJobUVF implements Runnable {
	private String outFilePath;
	private CopyOnWriteArrayList<String> outPathes;
	private GribToPngUtils png;
	private NcBeanPg pg = new NcBeanPg();
	private CountDownLatch latch;
	private String prefixFileName;
	private float[] ps;
	private Map<String, Object> datasMap;
	private String elName;
	private String elName1;
	private String element;
	private String layerName;
	private int i;
	private String outPath;
	private AtomicInteger total;
	private static DecimalFormat df = new DecimalFormat("0.00");

	public WriteFileJobUVF(Map<String, Object> datasMap, float[] ps, String prefixFileName, String elName, String elName1,
						   String element, String layerName, String outPath, int i, String outFilePath,
						   CopyOnWriteArrayList<String> outPathes, GribToPngUtils png, NcBeanPg pg, CountDownLatch latch) {
		this.datasMap = datasMap;
		this.ps = ps;
		this.prefixFileName = prefixFileName;
		this.elName = elName;
		this.elName1 = elName1;
		this.element = element;
		this.outPath = outPath;
		this.layerName = layerName;
		this.i = i;
		this.outFilePath = outFilePath;
		this.outPathes = outPathes;
		this.png = png;
		copy(pg, this.pg);
		this.latch = latch;
	}
	
	private void copy(NcBeanPg from, NcBeanPg to)
	{
		TransObjUtil.transObj(from, to);
	}
	
	@Override
	public void run() {
//		double[][] datas = NcReader.readByNameLayer(datasMap, elName, layerName);
//		double[][] datas2 = NcReader.readByNameLayer(datasMap, elName1, layerName);
		Thread.currentThread().setName(prefixFileName);
		
		double[][] datas = null;
		double[][] datas2 = null;
		
		
		String[] dimensions = NcReaderUtils.getDimensions(datasMap, elName);
		if(elName.startsWith("xxx"))
		{
			dimensions[2] = elName.substring(0, 4) + dimensions[2];
		}
		double[] valueByName = NcReaderUtils.readByName(datasMap, dimensions[2]);
		double[][][] ds = NcReaderUtils.readByNameLayerF(datasMap, elName, String.valueOf(i));
		double[][][] ds2 = NcReaderUtils.readByNameLayerF(datasMap, elName1, String.valueOf(i));

		String prefix = "";

		double[] interval = NcReaderUtils.getGridInterval(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat,prefixFileName);
		
		
//		System.out.println("读取数据耗时: " + (System.currentTimeMillis() - time));
		String uUnit = NcReaderUtils.getElementUnit(datasMap, elName);
		String vUnit = NcReaderUtils.getElementUnit(datasMap, elName1);
		String elementUnit = "u=" + uUnit + ";" + "v=" + vUnit;
		
		/*if(prefixFileName.contains(GJ))
		{
			double temp = 0;
			for(int m = 0; m < pg.latnum / 2; m++)
			{
				for(int j = 0; j < pg.lonnum; j++)
				{
					temp = values[m * pg.lonnum + j];
					values[m * pg.lonnum + j] = values[(pg.latnum - 1 - m) * pg.lonnum + j];
					values[(pg.latnum - 1 - m) * pg.lonnum + j] = temp;

					temp = values[m * pg.lonnum + j];
					values2[m * pg.lonnum + j] = values2[(pg.latnum - 1 - m) * pg.lonnum + j];
					values2[(pg.latnum - 1 - m) * pg.lonnum + j] = temp;
				}
			}
		}*/
		
		if(prefixFileName.contains(".wave.nc"))
		{
			layerName = (int) ((ps[i] - ps[0]) * 24) + "";
		}
		
		double[] lonWestLatSouth = NcReaderUtils.getLonWestLatSouth(datasMap, prefix + NcReaderUtils.lon, prefix + NcReaderUtils.lat);

		element = element.replaceAll("u_|v_", "uv_");
		element = element.replaceAll("u-|v-", "uv-");

		String unit = null;
		if(pg.pressLayerUnit.toLowerCase().contains("pa") || pg.pressLayerUnit.toLowerCase().endsWith("m"))
		{
			
			unit = pg.pressLayerUnit;
		}
		else
		{
			unit = "";
		}
//		outFilePath = outPath + prefixFileName + "_" + element + "_" + NumberFormatUtil.science(ps[i]) + "_" + pg.fileDate.substring(0, 8) + pg.fcTime;
		String layer = "";
		String dataType = DataTypeUtils.getDataType(prefixFileName);
		String elem = element.toLowerCase().startsWith("xxx") ? element.substring(4) : element;
		for(int i = 0, num = ds.length; i < num; i++)
		{
			layer = NumberFormatUtil.scienceD(valueByName[i]);

			datas = ds[i];
			datas2 = ds2[i];
			if(interval[1] < 0)
			{
				double[][] datas11 = new double[datas.length][];
				for(int j = 0, count = datas.length; j < count; j++)
				{
					datas11[j] = datas[count - 1 - j];
				}
				datas = datas11;
				
				double[][] datas22 = new double[datas2.length][];
				for(int j = 0, count = datas2.length; j < count; j++)
				{
					datas22[j] = datas2[count - 1 - j];
				}
				datas2 = datas22;
			}
			
			double[] values = trans(datas);
			double[] values2 = trans(datas2);
			
			outFilePath = outPath + prefixFileName + "_" + element + "_" + this.i + "_" + NumberFormatUtil.scienceD(valueByName[i]) + "_" + layerName + unit + "_" + pg.fileDate.substring(0, 8) + pg.fcTime;
			
//		System.out.println("数据准备耗时: " + (System.currentTimeMillis() - time));
			
			FileUtil.writeBytesToFile(outFilePath + ".bin", datas, datas2);
			outPathes.add(outFilePath + ".bin");
			String enviTxt = EnviTxtUtil.getEnviTxt(datas[0].length, datas.length, 4, lonWestLatSouth[0], lonWestLatSouth[1], interval[0], interval[1], elementUnit);
			FileUtil.writeStrToFile(enviTxt, outFilePath + ".hdr");
			outPathes.add(outFilePath + ".hdr");
			
			png.png(outFilePath + ".png", values, values2,  pg.lonnum, pg.latnum, pg.lons, pg.lats, pg.lone, pg.late, pg.lonstep, pg.latstep, elementUnit);
			
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
	}

	public double[] trans(double[][] datas)
	{
		double[] values =  ArrayReverseUtils.trans(datas);
		int index = CalculateIndexUtils.getIndex(pg.lons, pg.lonstep);
		if(pg.lons == 0 && pg.lons == 360)
		{
			values = ArrayReverseUtils.trans_reverse(datas, index);
		}
		else if(pg.lons >= 180)
		{
			values = ArrayReverseUtils.trans(datas);
			pg.lons = (pg.lons - 360);
			pg.lone = (pg.lone - 360);
		}
		else if(pg.lons < 180 && pg.lone > 180)
		{
			values = ArrayReverseUtils.trans_reverse(datas, index);
			pg.lons = (pg.lons - 180);
			pg.lone = (pg.lone - 180);
		}

		return values;
	}
}
