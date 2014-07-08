package cn.bidlink.framework.core.utils.file.split;

import java.io.File;

/**
 * @description: start <= x <= end
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-11-14 上午11:58:26
 */
public class Section {
	
	/**
	 * 文件开始值
	 */
	private long start;
	
	/**
	 * 文件结束位置
	 */
	private long end;
	
	/**
	 * 段名称
	 */
	private String sectionName;
	
	/**
	 * 待拆分的源文件路径
	 */
	private File sourceFile;
	
	/**
	 * 分割后的文件夹路径
	 */
	private File toDir;

	/**
	 * 每个段的大小
	 */
	private long sectionSize; 
	
	public Section() {
	}

	public Section(File sourceFile,File toDir,long start, long end, String sectionName) {
		super();
		this.start = start;
		this.end = end;
		this.sectionName = sectionName;
		this.sourceFile = sourceFile;
		this.toDir = toDir;
		this.getSectionSize();
	}


	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public long getSectionSize() {
		if(this.getStart() == 0) {
			return sectionSize = this.getEnd() - this.getStart();
		} else {
			return sectionSize = this.getEnd() - this.getStart() + 1;
		}
	}

	public void setSectionSize(long sectionSize) {
		this.sectionSize = sectionSize;
	}

	public File getToDir() {
		return toDir;
	}

	public void setToDir(File toDir) {
		this.toDir = toDir;
	}
	
	/**
	 * @description  取得目的段文件
	 * @return
	 */
	public File getDestSectionFile() {
		return new File(getToDir(), getSectionName());
	}
    

}
