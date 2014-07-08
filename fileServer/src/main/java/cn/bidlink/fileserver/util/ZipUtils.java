 package cn.bidlink.fileserver.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


/**
 * @author Tiger
 *
 */
public class ZipUtils {

	/**
	 * 压缩文件(包括子目录)
	 * 
	 * @param baseDir
	 *            要压缩的文件夹(物理路径)
	 * @param fileName
	 *            保存的文件名称(物理文件路径)
	 * @throws Exception
	 */
	public static void zipFile(String baseDir, String fileName)throws Exception {
		List<File> fileList = getSubFiles(new File(baseDir));
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
		zos.setEncoding("GBK");//设置编码,解决中文乱码的问题
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		int readLen = 0;
		for (int i = 0; i < fileList.size(); i++) {
			File f = (File) fileList.get(i);
			ze = new ZipEntry(getAbsFileName(baseDir, f));
			ze.setSize(f.length());
			ze.setTime(f.lastModified());
			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(new FileInputStream(f));
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.closeEntry();
		zos.close();
	}
	
	/**
	 * 打包多个文件到一个压缩包里
	 * @param srcFileNames
	 * @param fileName
	 * @throws Exception
	 */
	public static void zipFile(List<String> srcFileNames, String fileName)throws Exception {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
		zos.setEncoding("GBK");//设置编码,解决中文乱码的问题
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		int readLen = 0;
		for(int i=0;i<srcFileNames.size();i++){
			File f = new File(srcFileNames.get(i));
			String tmp = srcFileNames.get(i).substring(srcFileNames.get(i).lastIndexOf("\\")+1);
			ze = new ZipEntry(tmp);
			ze.setSize(f.length());
			ze.setTime(f.lastModified());
			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(new FileInputStream(f));
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.closeEntry();
		zos.close();		
	}

	/**
	 * 给定根目录，返回另一个文件名的相对路径，用于zip文件中的路径.
	 * 
	 * @param baseDir
	 *            java.lang.String 根目录
	 * @param realFileName
	 *            java.io.File 实际的文件名
	 * @return 相对文件名
	 */
	private static String getAbsFileName(String baseDir, File realFileName) {
		File real = realFileName;
		File base = new File(baseDir);
		String ret = real.getName();
		while (true) {
			real = real.getParentFile();
			if (real == null)
				break;
			if (real.equals(base))
				break;
			else
				ret = real.getName() + "/" + ret;
		}
		return ret;
	}

	/**
	 * 取得指定目录下的所有文件列表，包括子目录.
	 * 
	 * @param baseDir
	 *            File 指定的目录
	 * @return 包含java.io.File的List
	 */
	private static List<File> getSubFiles(File baseDir) {
		List<File> ret = new ArrayList<File>();
		File[] tmp = baseDir.listFiles();
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].isFile()){
				ret.add(tmp[i]);
			}
			if (tmp[i].isDirectory()){
				ret.addAll(getSubFiles(tmp[i]));
			}
		}
		return ret;
	}

	/**
	 * 解压缩功能. 将ZIP_FILENAME文件解压到ZIP_DIR目录下.
	 * @param zipFileName ZIP文件物理路径
	 * @param zipDir
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void unZipFile(String zipFileName, String zipDir)throws Exception {
		ZipFile zip = new ZipFile(zipFileName);
		Enumeration<ZipEntry> en = zip.getEntries();
		ZipEntry entry = null;
		byte[] buffer = new byte[1024];
		int length = -1;
		InputStream input = null;
		BufferedOutputStream bos = null;
		File file = null;

		while (en.hasMoreElements()) {
			entry = (ZipEntry) en.nextElement();
			if (entry.isDirectory()) {
				file = new File(zipDir, entry.getName());
				if (!file.exists()) {
					file.mkdir();
				}
				continue;
			}

			input = zip.getInputStream(entry);
			file = new File(zipDir, entry.getName());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			bos = new BufferedOutputStream(new FileOutputStream(file));

			while (true) {
				length = input.read(buffer);
				if (length == -1){
					break;
				}
				bos.write(buffer, 0, length);
			}
			bos.close();
			input.close();
		}
		zip.close();
	}


	public static void main(String[] args) {
		try {
			//ZipUtils.zipFile("E:\\tmp", "E:\\tmp.zip");
			//ZipUtils.unZipFile("E:\\tmp.zip", "E:\\tmp2");
			List<String> list = new ArrayList<String>();
			list.add("d:\\串口调试助手.exe");
			list.add("d:\\ftptrace.txt");
			ZipUtils.zipFile(list,"d:\\test.zip");
			
			System.out.println("执行完成！！！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
