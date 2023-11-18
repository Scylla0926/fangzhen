package com.geovis.receiver.tools;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.BUnzip2;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.GUnzip;
import org.apache.tools.ant.taskdefs.Untar;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressTools {
	public boolean unzip_one(File file){
		if (file.isFile()) {
			try {
				String targetDir = file.getParent();
				boolean md = new File(targetDir).mkdir();
				if (md) {
				}else{
					preformunzip(file.getAbsolutePath(), targetDir);
				}
			} catch (Exception e) {
				return false;
			}

		}
		return true;
	}

	/**
	 * 获取电脑IP地址
	 *
	 * @return
	 */
	public String getIP() {
		String ip = "";
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if(os == null){
			return "";
		}
		if (os.equalsIgnoreCase("Linux")) {
			ip = getLinuxLocalIp();
		} else {
			try {
				ip = getAllips();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return ip;

	}

	/**
	 * 获取Linux下的IP地址
	 */
	public String getLinuxLocalIp() {
		String ip = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo")) {
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ipaddress = inetAddress.getHostAddress()
									.toString();
							if (!ipaddress.contains("::")
									&& !ipaddress.contains("0:0:")
									&& !ipaddress.contains("fe80")) {
								ip = ipaddress;
							}
						}
					}
				}
			}
		} catch (SocketException ex) {
			System.out.println("获取ip地址异常");
			ip = "127.0.0.1";
			ex.printStackTrace();
		}
		return ip;
	}

	/**
	 * windows 获取ip（包括有虚拟网络的ip）
	 */
	public String getAllips() throws UnknownHostException {
		String hosna = InetAddress.getLocalHost().getHostName().toString();
		List<String> list = new ArrayList<String>();
		if (hosna.length() > 0) {
			InetAddress[] addr = InetAddress.getAllByName(hosna);
			if (addr.length > 0) {
				for (int i = 0; i < addr.length; i++) {
					InetAddress address = addr[i];
					if (address instanceof Inet4Address) {
						list.add(address.toString());
					}
				}
			}
		}
		return list.get(0).split("/")[1];
	}

	public void preformunzip(String pathname, String targetDir) {
		String checktype = pathname.toLowerCase();
		Project prj = new Project();

		if (checktype.endsWith(".zip") || checktype.endsWith(".jar")
				|| checktype.endsWith(".war") || checktype.endsWith(".rar")) {
			Expand expand = new Expand();
			expand.setProject(prj);
			expand.setSrc(new File(pathname));
			expand.setOverwrite(true);
			expand.setDest(new File(targetDir));
			expand.execute();
		} else if (checktype.endsWith(".tar")) {
			Untar untar = new Untar();
			untar.setProject(prj);
			untar.setSrc(new File(pathname));
			untar.setOverwrite(true);
			untar.setDest(new File(targetDir));
			untar.execute();
		} else if (checktype.endsWith(".gz")) {
			GUnzip untar = new GUnzip();
			untar.setProject(prj);
			untar.setSrc(new File(pathname));
			untar.setDest(new File(targetDir));
			untar.execute();
		} else if (checktype.endsWith(".bz") || checktype.endsWith(".bz2")) {
			BUnzip2 untar = new BUnzip2();
			untar.setProject(prj);
			untar.setSrc(new File(pathname));
			untar.setDest(new File(targetDir));
			untar.execute();
		} else if (checktype.endsWith(".tgz")) {
			Untar.UntarCompressionMethod gzip = new Untar.UntarCompressionMethod();
			gzip.setValue("gzip");
			Untar untar = new Untar();
			untar.setCompression(gzip);
			untar.setProject(prj);
			untar.setSrc(new File(pathname));
			untar.setDest(new File(targetDir));
			untar.execute();
		}

	}

	public static void main(String[] args) {
		File file = new File("D:/FTP");
		for(File f : file.listFiles()){
			System.out.println(f.getAbsolutePath());
		}
	}

	public boolean doZip(String srcFile, String zipFile){
		try {
			String temp = "";
			File src = new File(srcFile);
			File zip = new File(zipFile);
			//判断源文件是否存在
			if(!src.exists()){
				System.out.println("源文件不存在!");
				System.exit(1);
			}
			//判断压缩路径是否存在，不存在就创建
			if(!zip.getParentFile().exists()){
				zip.getParentFile().mkdirs();
			}

			//创建文件流
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zip));
			Charset charset = Charset.forName("UTF-8");
			ZipOutputStream zos = new ZipOutputStream(bos,charset);
			zip(src,zos,temp);
			zos.flush();
			zos.close();
			System.out.println("压缩完成!");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * 递归压缩文件
	 * @param file
	 * @param zos
	 * @param temp
	 */
	public void zip(File file,ZipOutputStream zos,String temp){
		try {
			if(file.isDirectory()){
				String str = temp+file.getName()+"/";
				zos.putNextEntry(new ZipEntry(str));
				File[] files = file.listFiles();
				for(File file2:files){
					zip(file2,zos,str);
				}
			}else{
				ZipFile(file,zos,temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ZipFile(File file,ZipOutputStream zos,String temp){
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			zos.putNextEntry(new ZipEntry(temp+file.getName()));

			byte[] bytes = new byte[1024];
			int len;
			while((len = bis.read(bytes)) != -1){
				zos.write(bytes, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String formatSigleDate(int n){
		if(n<10){
			return "0"+n;
		}else{
			return n+"";
		}
	}

}