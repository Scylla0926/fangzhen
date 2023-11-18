package com.geovis.receiver.tools;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

import static org.apache.commons.io.IOUtils.closeQuietly;

@Component
@Slf4j
public class UnZipFileUtil {

    /**
     * 构建目录
     *
     * @param outputDir 输出目录
     * @param subDir    子目录
     */
    public String createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + File.separator + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 解压缩tar文件
     *
     * @param file       压缩包文件
     * @param outputDir 目标文件夹
     */
    public void decompressTar(File file, String outputDir) {
        TarInputStream tarIn = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            tarIn = new TarInputStream(fileInputStream);
            createDirectory(outputDir, null);// 创建输出目录
            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null && !"".equals(entry.getName())) {
                if (entry.isDirectory()) {// 是目录
                    createDirectory(outputDir, entry.getName());// 创建空目录
                } else {// 是文件
                    byte[] tmp = new byte[entry.getName().length()];
                    for (int i = 0; i < entry.getName().length(); i++) {
                        char c = entry.getName().charAt(i);
                        byte b = (byte) (0xFF & c);
                        tmp[i] = b;
                    }
                    String fName = new String(tmp, "UTF-8");
                    File tmpFile = new File(outputDir + "/" + fName);

                    createDirectory(tmpFile.getParent() + "/", null);// 创建输出目录
                    OutputStream out = new FileOutputStream(tmpFile);
                    tarIn.copyEntryContents(out);
                    closeQuietly(out);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeQuietly(fileInputStream);
            closeQuietly(tarIn);
        }
    }

    /**
     * 解压缩tar.gz文件
     *
     * @param file       压缩包文件
     * @param targetPath 目标文件夹
     */
    public void decompressTarGz(File file, String targetPath) {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        GZIPInputStream gzipIn = null;
        TarInputStream tarIn = null;
        OutputStream out = null;
        try {
            System.out.println(file.getAbsolutePath());
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            gzipIn = new GZIPInputStream(bufferedInputStream);
            tarIn = new TarInputStream(gzipIn, 1024 * 2);

            // 创建输出目录
//            String filePath = createDirectory(targetPath, file.getName().split("\\.")[0]);
            String filePath = createDirectory(targetPath, file.getName().split("_")[4].substring(0, 8));
            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {
                if (entry.isDirectory()) { // 是目录
                    filePath = createDirectory(filePath, entry.getName()); // 创建子目录
                } else { // 是文件
                    File tempFIle = new File(filePath + File.separator + entry.getName());
                    out = new FileOutputStream(tempFIle);
                    int len = 0;
                    byte[] b = new byte[2048];

                    while ((len = tarIn.read(b)) != -1) {
                        out.write(b, 0, len);
                    }
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (tarIn != null) {
                    tarIn.close();
                }
                if (gzipIn != null) {
                    gzipIn.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 解压缩gz文件
     *
     * @param file       压缩包文件
     * @param targetPath 目标文件夹
     */
    public void decompressGz(File file, String targetPath) {
        FileInputStream fileInputStream = null;
        GZIPInputStream gzipIn = null;
        OutputStream out = null;
        String suffix = ".gz";
        try {
            fileInputStream = new FileInputStream(file);
            gzipIn = new GZIPInputStream(fileInputStream);
            // 创建输出目录
            createDirectory(targetPath, null);

            File tempFile = new File(targetPath + File.separator + file.getName().replace(suffix, ""));
            out = new FileOutputStream(tempFile);
            int count;
            byte data[] = new byte[2048];
            while ((count = gzipIn.read(data)) != -1) {
                out.write(data, 0, count);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (gzipIn != null) {
                    gzipIn.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩7z文件
     *
     * @param file       压缩包文件
     * @param targetPath 目标文件夹
     */
    public void decompress7Z(File file, String targetPath) {
        SevenZFile sevenZFile = null;
        OutputStream outputStream = null;
        try {
            sevenZFile = new SevenZFile(file);
            // 创建输出目录
            createDirectory(targetPath, null);
            SevenZArchiveEntry entry;

            while ((entry = sevenZFile.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    createDirectory(targetPath, entry.getName()); // 创建子目录
                } else {
                    outputStream = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int len = 0;
                    byte[] b = new byte[2048];
                    while ((len = sevenZFile.read(b)) != -1) {
                        outputStream.write(b, 0, len);
                    }
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sevenZFile != null) {
                    sevenZFile.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 解压缩RAR文件
     *
     * @param file       压缩包文件
     * @param targetPath 目标文件夹
     */
    public void decompressRAR(File file, String targetPath) {
        Archive archive = null;
        OutputStream outputStream = null;
        try {
            try {
                archive = new Archive(file);
            } catch (RarException e) {
                throw new RuntimeException(e);
            }
            FileHeader fileHeader;
            // 创建输出目录
            createDirectory(targetPath, null);
            while ((fileHeader = archive.nextFileHeader()) != null) {
                if (fileHeader.isDirectory()) {
                    createDirectory(targetPath, fileHeader.getFileNameString().trim()); // 创建子目录
                } else {
                    outputStream = new FileOutputStream(new File(targetPath + File.separator + fileHeader.getFileNameString().trim()));
                    archive.extractFile(fileHeader, outputStream);
                }
            }
        } catch (RarException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (archive != null) {
                    archive.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * zip文件解压
     *
     * @param destPath 解压文件路径
     * @param zipFile  压缩文件
     * @param password 解压密码(如果有)
     */
    public void unPackZip(File zipFile, String password, String destPath) {
        try {
            ZipFile zip = new ZipFile(zipFile);
            /*zip4j默认用GBK编码去解压,这里设置编码为GBK的*/
            zip.setFileNameCharset("GBK");
            log.info("begin unpack zip file....");
            zip.extractAll(destPath);
            // 如果解压需要密码
            if (password != null) {
                if (zip.isEncrypted()) {
                    zip.setPassword(password);
                }
            }
        } catch (Exception e) {
            log.error("解压失败：", e.getMessage(), e);
        }
    }


    /**
     * 删除压缩文件
     */
    public Boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
}
