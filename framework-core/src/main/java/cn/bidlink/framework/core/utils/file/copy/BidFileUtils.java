package cn.bidlink.framework.core.utils.file.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.io.FileUtils;

/**
 * @description: 文件操作工具类
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-11-15 下午2:03:27
 */
public class BidFileUtils extends FileUtils {

	/**
	 * @description 如果文件小于4G
	 * @param sourceFile
	 *            源文件
	 * @param destFile
	 *            目的文件
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		FileChannel fcin = null;
		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel fcout = null;
		try {
			fin = new FileInputStream(sourceFile);
			fcin = fin.getChannel();
			fout = new FileOutputStream(destFile);
			fcout = fout.getChannel();
			fcin.transferTo(0, fcin.size(), fcout);
		} finally {
            if(fcin != null) {
            	fcin.close();
            }
            if(fin != null) {
            	fin.close();
            }
            if(fcout != null) {
            	fcout.close();
            }
            if(fout != null) {
            	fout.close();
            }
		}
	}
	
	/**
	 * @description 拷贝大文件
	 * @param sourceFile  源文件
	 * @param destFile   目的文件
	 * @param bufferSize 设置缓冲大小
	 * @throws IOException
	 */
	public static void copyBigFile(File sourceFile, File destFile,int bufferSize) throws IOException {
		FileChannel fcin = null;
		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel fcout = null;
		try {
			fin = new FileInputStream(sourceFile);
			fcin = fin.getChannel();
			fout = new FileOutputStream(destFile);
			fcout = fout.getChannel();
			ByteBuffer bb = ByteBuffer.allocate(bufferSize);
			while (fcin.read(bb)!=-1){ 
			    bb.flip(); 
			    fcout.write(bb); 
			    bb.clear(); 
			}
		} finally {
            if(fcin != null) {
            	fcin.close();
            }
            if(fin != null) {
            	fin.close();
            }
            
            if(fcout != null) {
            	fcout.close();
            }
            if(fout != null) {
            	fout.close();
            }
          
		}
	}

}
