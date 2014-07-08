package cn.bidlink.framework.core.utils.file.split;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Semaphore;

/**
 * @description:文件段的处理
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-11-14 下午1:25:52
 */
public class FileSectionHandler implements Runnable {

	private int bufferSize;

	private Section section;

    private Semaphore semp;
	
	/**
	 * 是否标识内容段的范围(section.getStart()---section.getEnd())
	 */
	private boolean isMarkSection = false;
	
	public FileSectionHandler( int bufferSize,Section section,boolean isMarkSection,Semaphore semp) {
        this.bufferSize = bufferSize;
        this.section = section;
        this.isMarkSection = isMarkSection;
        this.semp = semp;
	}
	

	@Override
	public void run() {
	    try {
	    	// 获取许可
			semp.acquire();
	    	splitFileMaxSection(section);
	    }catch (Exception e) {
		   throw new RuntimeException(e.getMessage());
		} finally {
			semp.release();
		}
	}

	/**
	 * @description 分割一个文件 (startSeek < x <= endSeek)【不限制每个段的大小，可以超过2G】
	 * @param file
	 *            源文件
	 * @param startSeek
	 *            开始位置(byte)
	 * @param endSeek
	 *            结束位置(byte)
	 * @param toDir
	 *            产生的段文件夹路径
	 * @throws IOException
	 */
	private void splitFileMaxSection(Section section) throws IOException {

		long sectionTotalSize = section.getSectionSize();
		RandomAccessFile rafOut = null;
		RandomAccessFile rafIn = null;
		ByteBuffer bb = null;
		try {
			File toDir = section.getToDir();
			if(toDir == null) {
				throw new RuntimeException(" section toDir can't null!");
			}
			if(!toDir.exists()) {
				toDir.mkdirs();
			}
			// 输出的段文件路径
			File outFile = new File(toDir, section.getSectionName());
			rafIn = new RandomAccessFile(section.getSourceFile(), "r");
			if (section.getStart() == 0) {
				rafIn.seek(section.getStart());
			} else {
				rafIn.seek(section.getStart() - 1);
			}

			FileChannel fcin = rafIn.getChannel();

			rafOut = new RandomAccessFile(outFile, "rw");
			
			if(isMarkSection) {
				rafOut.writeLong(section.getStart()); //在文件的头信息中写入内容开始位置
				rafOut.writeLong(section.getEnd());//在文件的头信息中写入内容的结束位置
				rafOut.seek(16);
			}
			
			FileChannel fcout = rafOut.getChannel();

			if (sectionTotalSize < bufferSize) {
				bufferSize = Integer.valueOf(String.valueOf(sectionTotalSize)); // 如果指定的拆分文件大小
																				// 还小于缓冲大小，则指定该值为最小缓冲大小
			}
			bb = ByteBuffer.allocate(bufferSize);

			int everyReadSize = 0;

			long childSecCount = sectionTotalSize / bufferSize;

			// 平均每个字段最后剩余的大小
			long leftSize = sectionTotalSize - childSecCount * bufferSize;

			long sumReadSize = 0;

			int index = 0;

			while ((everyReadSize = fcin.read(bb)) != -1) {
				// 使缓冲区为一系列新的通道写入或相对获取 操作做好准备：它将限制设置为当前位置，然后将位置设置为 0。
				bb.flip();
				fcout.write(bb);
				bb.clear();

				fcout.force(true);
				sumReadSize += everyReadSize;
				// 最后一笔子字段
				if (index == childSecCount - 1) {
					bb = ByteBuffer.allocate(Integer.valueOf(String.valueOf(leftSize)));
				}

				// 读取完成
				if (index == childSecCount || sumReadSize == sectionTotalSize) {
					break;
				}
				index++;
			}

		} finally {
			if (rafIn != null) {
				rafIn.close();
			}
			if (rafOut != null) {
				rafOut.close();
			}
			if (bb != null) {
				bb.clear();
			}
		}

	}

}
