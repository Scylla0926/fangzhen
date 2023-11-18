package com.geovis.receiver.tools;

import com.geovis.receiver.receiver.ReceiverBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


/**
 * 扫描文件
 *

 * @author sunm
 *
 */

/***
 * 类描述:
 *  判断文件是否在阻塞队列中
 *  判断文件名称是否匹配正则表达式
 *  判断文件是否在有效的时间内
 *  判断文件是否已完成
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class FindFileVisitor extends SimpleFileVisitor<Path> {

	private ReceiverBase receiver;

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
//		if(receiver.getRecQueue().size()>200){
//			return FileVisitResult.TERMINATE;
//		}
//		File f = file.toFile();
//		if(!receiver.getRecQueue().contains(f) && receiver.patt(f)){
//			receiver.offer(f);
////			System.out.println(receiver.getRecQueue().size()+""+file.toString());
//		}
		System.out.println(file.toFile().getAbsolutePath());
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		System.out.println(file.toFile().getName()+"-扫描失败！\n"+exc.getMessage());
		return super.visitFileFailed(file, exc);
	}

}