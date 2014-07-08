package cn.bidlink.fileserver.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.bidlink.fileserver.bean.FileBean;
import cn.bidlink.fileserver.decrypt.CryptContext;
import cn.bidlink.fileserver.fileInfo.dao.impl.FileInfoDaoImpl;
import cn.bidlink.fileserver.fileInfo.model.FileInfo;
import cn.bidlink.fileserver.util.FileDownloadUtil;
import cn.bidlink.fileserver.util.MD5;
import cn.bidlink.fileserver.util.State;
import cn.bidlink.fileserver.util.ZipUtils;

/**
 * 
 * @author changzhiyuan
 * @date 2013-2-4
 * @desc 文件下载
 * 
 */
public class DownLoadFileServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(DownLoadFileServlet.class);

	private static final long serialVersionUID = 1L;
	private static final String zipName = "压缩.zip";

	// 文件保存到服务器的临时路径
	private String tempPath;

	/**
	 * 参数初始化
	 */
	public void init() throws ServletException {
		if (logger.isInfoEnabled()) {
			logger.info("init params.");
		}

		Properties props = new Properties();
		try {
			props.load(UploadFileServlet.class
					.getResourceAsStream("/application-common.properties"));
		} catch (IOException e1) {
			logger.error(e1);
			e1.printStackTrace();
		}
		this.tempPath = props.getProperty("cn.bidlink.file.basePath");

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 验证token 下載文件
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String token = request.getParameter("token");
		String code = request.getParameter("code");
		String code_list = request.getParameter("code_list");// 多个下载文件的code，分割

		if ((code_list == null || code_list.equals(""))
				&& (code == null || code.equals(""))) {
			invalidState(response, State.NULL.getState());
			return;
		}

		if (code_list != null) {
			code = code_list.split(",")[0];
		}

		code=transUUIDToCode(code);
		String[] values = code.split("_");

		if (logger.isInfoEnabled()) {
			logger.info("token=" + token + " code=" + code);
		}

		boolean valid = CryptContext.getInstance().decrypt(
				values[FileDownloadUtil.Application_id], token);
		if (!valid) {
			if (logger.isInfoEnabled()) {
				logger.info("valid fail,return!");
			}
			invalidState(response, State.NOPRMIN.getState());
			return;
		}

		// 多文件下载，进行zip压缩处理
		if (code_list != null && !code_list.equals("")) {

			mutipuleFileDownload(code_list, response);
			return;
		}

		FileDownloadUtil downloadUtil = new FileDownloadUtil();

		FileBean fileBean = downloadUtil.hdfsDownload(code);

		if (fileBean != null) {
			byte[] buffer = new byte[fileBean.getInputStream().available()];
			fileBean.getInputStream().read(buffer);
			fileBean.getInputStream().close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(fileBean.getFileName().getBytes("GBK"),
							"ISO-8859-1"));
			response.addHeader("Content-Length", "" + buffer.length);
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

		} else {
			invalidState(response, State.SYSERR.getState());
			return;
		}

	}

	/**
	 * mutipuleFileDownload:多文件下载，先生成zip包，在下载 <br/>
	 * 
	 * @param code_list
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void mutipuleFileDownload(String code_list,
			HttpServletResponse response) throws IOException, ServletException {
		String[] codes = code_list.split(",");

		// 生成存放临时文件的名称时间戳+code_list的MD5值，下载完成后删除。
		String tmpDir = System.currentTimeMillis() + MD5.encrypt(code_list);
		tmpDir = tempPath + tmpDir;

		// 生成文件夹,如果存在继续遍历生成新文件夹
		int i = 0;
		for (;;) {
			tmpDir += i;
			File fTmpDir = new File(tmpDir);
			if (!fTmpDir.exists()) {
				fTmpDir.mkdirs();
				break;
			} else {
				continue;
			}
		}

		// 保存文件到指定的文件夹
		for (String code : codes) {
			if (code == null) {
				continue;
			}
			
			code=transUUIDToCode(code);

			FileDownloadUtil downloadUtil = new FileDownloadUtil();
			FileBean fileBean = downloadUtil.hdfsDownload(code);

			File file = new File(tmpDir + File.separator
					+ fileBean.getFileName());
			FileOutputStream outStream = new FileOutputStream(file);
			if (fileBean != null) {

				byte[] buffer = new byte[fileBean.getInputStream().available()];

				fileBean.getInputStream().read(buffer);
				fileBean.getInputStream().close();
				outStream.write(buffer);
				outStream.close();
			}

		}

		try {
			ZipUtils.zipFile(tmpDir, tmpDir + File.separator + zipName);
		} catch (Exception e) {
			logger.error(e.getStackTrace());
			e.printStackTrace();
			invalidState(response, State.SYSERR.getState());

		}

		File zipFile = new File(tmpDir + File.separator + zipName);
		FileInputStream inStream = new FileInputStream(zipFile);
		byte[] zipBuffer = new byte[inStream.available()];
		inStream.read(zipBuffer);
		inStream.close();

		// 删除文件夹和文件
		deleteFolder(tmpDir);

		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String(zipName.getBytes("GBK"), "ISO-8859-1"));
		response.addHeader("Content-Length", "" + zipBuffer.length);
		OutputStream toClient = new BufferedOutputStream(
				response.getOutputStream());
		response.setContentType("application/octet-stream");
		toClient.write(zipBuffer);
		toClient.flush();
		toClient.close();
	}

	/**
	 * @方法说明： 删除文件夹
	 * @参数：
	 * @返回值：
	 * @异常：
	 **/
	private void deleteFolder(String folderPath) {
		try {
			File file = new File(folderPath);
			if (file.isDirectory()) {
				for (File child : file.listFiles()) {
					deleteFolder(child.getAbsolutePath());
				}
				file.delete();
			} else {
				file.delete();
			}
		} catch (Exception ex) {
			logger.error("文件夹【" + folderPath + "】删除失败！", ex);
		}
	}

	/**
	 * 无效状态返回
	 */
	private void invalidState(HttpServletResponse response, int state) {
		JSONObject json = new JSONObject();
		json.put("state", state);
		try {
			response.getOutputStream().write(json.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * transUUIDToCode:判断是否为uuid格式,从数据库中查询转换
	 * 
	 * @param uuid
	 * @return
	 */
	private String transUUIDToCode(String code) {
		Pattern pattern = Pattern
				.compile("[0-9a-f]{8}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{12}");

		Matcher matcher = pattern.matcher(code);
		if (matcher.matches()) {
			//数据库中查询对象
			FileInfoDaoImpl filedao = new FileInfoDaoImpl();
			FileInfo selectObj = new FileInfo();
			selectObj.setUuid(code);
			
			List<FileInfo> fiList = filedao.selectByVO(selectObj);
			if (fiList.size() > 0) {
				selectObj = fiList.get(0);
				return selectObj.getCode();
			}else
			{
				logger.error("error:code"+code);
				throw new RuntimeException("code:"+code);
			}

		} else {
			return code;
		}

	}
}
