package cn.bidlink.fileserver.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;

import cn.bidlink.fileserver.bean.FileBean;
import cn.bidlink.fileserver.fileInfo.dao.impl.FileInfoDaoImpl;
import cn.bidlink.fileserver.fileInfo.model.FileInfo;
import cn.bidlink.framework.filesystem.dfs.DistributedFileSystem;
import cn.bidlink.framework.filesystem.dfs.hdfs.HdFileSystemFactory;
import cn.bidlink.framework.log4j.level.ExtendedLevel;
import cn.bidlink.framework.log4j.utils.OperateMDC;

/**
 * @date 2013-2-5
 * @author changzhiyuan
 * @desc 文件下载util
 *
 */
public class FileDownloadUtil {
	// 解析方法
	public final static int prasethMethod = 0;
	// 应用Id 或类型ID
	public final static int Application_id = 1;
	// 日期ID
	public final static int data = 2;
	// 公司ID
	public final static int companyId = 3;
	// 文件名
	public final static int fileName = 4;

	private static Logger logger = Logger.getLogger(FileDownloadUtil.class);

	public FileBean hdfsDownload(String code) {
		FileBean fileBean = new FileBean();
		// 分析文件流
		String[] values = code.split("_");
		
		FileInfoDaoImpl filedao = new FileInfoDaoImpl();
		
		FileInfo selectObj = new FileInfo();
		selectObj.setCode(code);
		
		List<FileInfo> fiList = filedao.selectByVO(selectObj);
		if (fiList.size() > 0) {
			selectObj = fiList.get(0);
		}

		StringBuffer path = new StringBuffer();
		path.append(values[Application_id]).append("/");
		path.append(values[data]).append("/");
		path.append(values[companyId]).append("/");
		path.append(code);
		String fileName = selectObj.getFileName();
		DistributedFileSystem dfSystem = HdFileSystemFactory.create();
		fileBean.setFileName(fileName);
		InputStream fis = null;
		try {
			fis = dfSystem.open(path.toString());
			// 添加日志
			OperateMDC mdc = new OperateMDC();
			mdc.setCompanyId(String.valueOf(companyId));
			mdc.setFunctionName("文件下载");
			logger.log(ExtendedLevel.OPERATE_LOG_LEVEL, companyId + "文件下载");
			fileBean.setInputStream(fis);
			return fileBean;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}

		return null;
	}
}
