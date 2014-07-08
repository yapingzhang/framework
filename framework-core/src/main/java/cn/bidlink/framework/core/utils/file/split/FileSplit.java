package cn.bidlink.framework.core.utils.file.split;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * @description: 文件分割接口
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-11-15 下午1:26:32
 */
public interface FileSplit {

	/**
	 * 段的分割符号
	 */
	public static String SECTION_SPLIT_STUFF = "_";

	/**
	 * @description  适当的设计该值 bufferSize 有助于提高性能
	 * @param bufferSize
	 */
	public void setBufferSize(int bufferSize);

	public int getBufferSize();

	/**
	 * @description 将文件分割位为指定份数，返回每份文件大小
	 * @param file
	 *            源文件
	 * @param toDir
	 *            分段后的文件夹路径
	 * 
	 * @param secStuff
	 *            段的分割后缀
	 * @param sectionNum
	 *            份数
	 * @return
	 */
	public Vector<Section> splitFileToSection(File file, File toDir, int sectionNum);
	
	/**
	 * @description 将文件按段数分割
	 * @param file 源文件
	 * @param toDir 产生的段到指定的文件夹下
	 * @param sectionNum 拆分成的段数
	 * @param isMarkSection  是否生成头标识(将不能文件名区分文件的顺序)
	 * @param threadNum 同时拆分的线程数(同一个磁盘单线程效率比多线程效率高很多)
	 * @return 拆分后的段文件
	 * @throws IOException
	 */
	public List<File> splitFileBySNum(File file, File toDir,int sectionNum, boolean isMarkSection,Integer threadNum) throws IOException;
	
	/**
	 * @description 将文件按段的大小分割
	 * @param file 源文件
	 * @param toDir 产生的段到指定的文件夹下
	 * @param sectionSize 按大小拆分文件
	 * @param isMarkSection  是否生成头标识(将不能文件名区分文件的顺序)
	 * @param threadNum 同时拆分的线程数(同一个磁盘单线程效率比多线程效率高很多)
	 * @return 拆分后的段文件
	 * @throws IOException
	 */
	public List<File> splitFileBySSize(File file, File toDir,long sectionSize, boolean isMarkSection,Integer threadNum) throws IOException;
	
 	
	/**
	 * @description  文件合并
	 * @param listFiles 待合并的文件
	 * @param isMarkSection 是否使用头标识区别文件
	 * @param fileDest 合并后的文件
	 * @throws IOException
	 */
	public  boolean fileCombine(List<File> listFiles,boolean isMarkSection,File fileDest);
	
	/**
	 * @description  文件合并
	 * @param sourceDir 待合并的文件夹路径
	 * @param isMarkSection 是否使用头标识区别文件
	 * @param fileDest 合并后的文件
	 * @throws IOException
	 */
	public boolean fileCombile(File sourceDir,boolean isMarkSection,File fileDest);
	
	
	
	

}
