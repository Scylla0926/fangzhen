package com.geovis.receiver.receiver;

import com.geovis.receiver.pojo.model.ConfigElement;
import com.geovis.receiver.pojo.model.ProInfo;
import com.geovis.receiver.pojo.model.Produce;
import com.geovis.receiver.tools.FileBackupInfoUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/***
 * 类描述: 引接任务基类 定义了所有数据种类处理的步骤、流程、处理方法
 * @author yxl
 * @Date: 10:01 2020/9/19
 */

@Data
@Slf4j
@Component
public abstract class ReceiverBase {

//    public RedisUtil redisUtil = new RedisUtil();

    private static String hisFileName = "";

    public ConfigElement configElement;

//    public int totalNum = redisUtil.totalNum(configElement);

    private ProInfo proInfo = new ProInfo();

    private Produce proReal;

    @Autowired
    public FileBackupInfoUtils fileBackupInfoUtils;

    //时间格式化yyyy-MM-dd HH:mm:ss
    protected SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //资料引接队列
    protected BlockingQueue<File> recQueue = new LinkedBlockingDeque<File>(400);

    //资料实体文件类型及格式判断-私用型
    public abstract boolean patt(File f);

    //将文件从专线中拷贝到文件缓存区
    public abstract boolean cacheFile(Produce pro);

    //将文件从文件缓存区归档到归档区
    public abstract boolean archiveFile(Produce pro);
    //将文件从文件缓存区归档到归档区
    public abstract boolean recordBackup(Produce pro);

    //将文件从文件缓存区归档到归档区
    public abstract boolean dataStore(Produce pro);

    //资料处理（解报/编目）
    public abstract boolean produce(Produce pro);

    //入库对象创建及存入内存交换区
//	public abstract boolean create(Produce pro,String key);

    //资料处理（解报/编目）
    public abstract boolean checkFile(Produce pro);

    /**
     * 引接生产
     *
     * @return
     */
    public void receiverProducer(){
        long startTime = System.currentTimeMillis();


        //文件扫描路径
        String path = configElement.getDataResourceDir();
        File dirFile = new File(path);
        //递归获取文件
        getFile(dirFile);
        long endTime = System.currentTimeMillis();
//        System.out.println("扫描文件总耗时:================================================================>"+(endTime - startTime ) / 1000);
//        Path start = dirFile.toPath();
//        System.out.println("扫描路径:"+path);
//        FindFileVisitor visitor = new FindFileVisitor(this);
//        try {
//            Files.walkFileTree(start,visitor);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 获取目录下的所有文件
     */
    public void getFile(File dirFile){
            try(Stream<Path> paths = Files.walk(dirFile.toPath())){
            List<Path> fileNames = new ArrayList<>();
            paths.forEach(item->{
                if(!new File(item.toString()).isDirectory()){
                    fileNames.add(item);
                }
            });
            fileNames.forEach(item -> {
                try{
                    if(this.getRecQueue().size()>200){
                        return;
                    }
                    File newFile = item.toFile();
                    if(!this.getRecQueue().contains(newFile) && this.patt(newFile)){
                        this.offer(newFile);
                    }
                }catch (Exception e){
                    log.error("错误异常信息:{}",e);
                }
            });
        }catch (Exception e){
            log.error("错误异常信息:{}",e);
        }
    }

    /**
     * 处理消费
     *
     * @return
     */
    public void receiverConsumer() {
        try {
//            System.out.println("消费数据:"+fileType.getType());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            if(!hisFileName.isEmpty()){
                proInfo.setFilename(hisFileName);
            }
            proInfo.setStatus("waiting");
            File f = recQueue.take();//队列为null时阻塞
            proInfo.setFilename(f.getName());
            hisFileName = f.getName();
//            System.out.println("********0:"+proInfo.getFilename());
            proInfo.setStatus("running");
            proInfo.setStime(sdf.format(new Date()));
            proReal = new Produce(f);
            proReal.setSrcFilePath(f.getAbsolutePath());
            proReal.setDataCachePath(configElement.getDataCacheDir());
            proReal.setRegx(configElement.getRegexList());
            executor(proReal);
            StringBuilder errorBuilder = proReal.getError();
            StringBuilder infoBuilder = proReal.getInfo();
            String errorString = errorBuilder.toString();
            String infoString = infoBuilder.toString();
            if (errorString != null && !errorString.isEmpty()) {
                log.error(errorString);
            }
            log.info(infoString);
            proInfo.setStatus("finish");
            proInfo.setEtime(sdf.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            proReal = null;
        }
    }

    /**
     * 资料引接/处理流程，执行入口
     *
     * @param pro	作业流水
     * @return
     */
    private boolean executor(Produce pro){

        try {
            File file = pro.getFile();

            //将文件从专线中拷贝到文件缓存区
            long sct = System.currentTimeMillis();
            if(cacheFile(pro)){
                long ect = System.currentTimeMillis();
//				System.out.println("引接耗时:"+(ect-sct));
//				pro.addInfo(file.getName()+"-引接成功！");
            }else{
                log.error(file.getName()+"-引接失败！");
                return false;
            }
            //质量文件检验
            if(checkFile(pro)){
//				pro.addInfo(file.getName()+"-质量检验成功！");
            }else{
                log.error(file.getName()+"-质量检验失败！");
                return false;
            }
            //将文件从文件缓存区拷贝到归档区
            long sat = System.currentTimeMillis();
            if(archiveFile(pro)){
                long eat = System.currentTimeMillis();
//				System.out.println("归档耗时:"+(eat-sat));
//				pro.addInfo(file.getName()+"-归档成功！");
            }else{
                log.error(file.getName()+"-归档失败！");
                return false;
            }

            //处理文件
            if(produce(pro) ){
				log.info(file.getName()+"-处理成功！");
            }else{
                log.error(file.getName()+"-处理失败！");
                return false;
            }
            //数据入库
            long st = System.currentTimeMillis();
            if(dataStore(pro)){
                long et = System.currentTimeMillis();
//				pro.addInfo(file.getName()+"-入库成功！");
                log.info(file.getName()+"-入库成功！\n"+pro.getFile().getName()+"入库耗时:"+(et-st)/1000+"\n当前队列容量："+
                        recQueue.size()+"\n");
            }else{
                long et = System.currentTimeMillis();
                log.error(file.getName()+"-入库失败！\n"+pro.getFile().getName()+"入库耗时:"+(et-st)/1000+"\n当前队列容量："+
                        recQueue.size()+"\n");
                return false;
            }

            long sbt = System.currentTimeMillis();
            if(recordBackup(pro)){
                long ebt = System.currentTimeMillis();
//				System.out.println("记录耗时:"+(ebt-sbt));
//				pro.addInfo(file.getName()+"-痕迹记录成功！");
            }else{
                log.error(file.getName()+"-痕迹记录失败！");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 资料实体文件类型及格式判断-通用型
     * @param receiver
     *
     * @param f
     * @return
     */
    public boolean PattCommon(File f, ReceiverBase receiver){

        //判断文件名称是否匹配正则表达式
        boolean reg = fileBackupInfoUtils.fileNamePattern(f,configElement.getRegexList());


        //判断文件是否在有效的时间内
        boolean usf = fileBackupInfoUtils.fileDatePattern(f,configElement.getRegexList(),configElement.getCycleType(),configElement.getCycleNum());

        //判断文件是否已完成
        boolean fin = fileBackupInfoUtils.fileCompletedPattern(configElement.getBackupFile(),f,configElement.getRegexList(),configElement.getEleName());

        //判断文件是否正在执行引接处理操作
        boolean doi = fileBackupInfoUtils.isFileExecuting(f,receiver);


//        System.out.println(f.getName() + reg + usf + fin + doi);//中心 把 网站运行起来
        if (reg && usf && !fin && !doi) {
            return true;
        }
        return false;
    }

    /**
     * 向资料引接的队列中存放文件
     *
     * @param file
     * @return
     */
    public boolean offer(File file){
        try {
            return recQueue.offer(file, 2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}