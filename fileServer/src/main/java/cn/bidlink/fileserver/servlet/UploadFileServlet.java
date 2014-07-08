package cn.bidlink.fileserver.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import cn.bidlink.fileserver.bean.FormBean;
import cn.bidlink.fileserver.bean.UploadFileBean;
import cn.bidlink.fileserver.decrypt.CryptContext;
import cn.bidlink.fileserver.fileInfo.dao.impl.FileInfoDaoImpl;
import cn.bidlink.fileserver.fileInfo.model.FileInfo;
import cn.bidlink.fileserver.util.Constants;
import cn.bidlink.fileserver.util.FileUploadUtil;
import cn.bidlink.fileserver.util.State;
import cn.bidlink.framework.filesystem.dfs.DistributedFileSystem;
import cn.bidlink.framework.filesystem.dfs.hdfs.HdFileSystemFactory;
import cn.bidlink.framework.log4j.level.ExtendedLevel;
import cn.bidlink.framework.log4j.utils.OperateMDC;

/**
 * 
 * @author changzhiyuan
 * @date 2013-2-4
 * @desc 文件上传action
 * 
 */
public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(UploadFileServlet.class);

	// 公司一段时间的上传次数
	private static ConcurrentHashMap<FormBean, Date> companyRecentUploads = new ConcurrentHashMap<FormBean, Date>();
	// 上传发生错误缓冲
	private static List<FormBean> errorCache = new ArrayList<FormBean>();
	// 文件上传工厂
	private DiskFileItemFactory fileFactory = new DiskFileItemFactory();
	// 扫描错误队列的时间间隔
	private static final int scanRecentInterval = 1000 * 60 * 60;

	// 文件保存到服务器的临时路径
	private String tempPath;
	// 每个公司一个小时上传次数限制
	private int uploadlimit;

	/**
	 * 参数初始化
	 */
	public void init() throws ServletException {
		if (logger.isInfoEnabled()) {
			logger.info("init params.");
		}

		Properties props = new Properties();
		try {
			//ServletContext application = this.getServletContext();
			props.load(UploadFileServlet.class.getResourceAsStream("/application-common.properties"));
		} catch (IOException e1) {
			logger.error(e1);
			e1.printStackTrace();
		}

		this.tempPath = props.getProperty("cn.bidlink.file.basePath");

		File tmpFile = new File(tempPath);
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		fileFactory.setRepository(tmpFile);

		this.uploadlimit = Integer.parseInt(props
				.getProperty("fileupload.number"));

		/**
		 * 后台任务，扫描发生上传失败的文件
		 */
		Thread t = new Thread() {
			public void run() {
				while (!Thread.interrupted()) {
					try {
						TimeUnit.MINUTES.sleep(1);
						errorCacheHandler();
						fileUploadTraceClear();
					} catch (Exception e) {
						logger.info("文件未上传成功！");
					}
				}
			}
		};
		t.setDaemon(true);
		t.start();

		if (logger.isInfoEnabled()) {
			logger.info("init params completes!.");
		}
	}

	/**
	 * 文件上传处理， 验证token, 验证文件大小 上传hadoop
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String applicationId = request.getParameter("applicationId");

		String prasethMethod = request.getParameter("praseth_Method");
		String companyId = request.getParameter("companyId") == null ? "0"
				: request.getParameter("companyId");
		if (hasExceedLimit(companyId)) {
			if (logger.isInfoEnabled()) {
				logger.info("companyId=" + companyId + " has exceeded limit");
			}
			invalidState(response, State.EXCEEDLIMIT.getState());
			return;
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");
		String dateStr = timeFormat.format(new Date());
		String fileFoldPath = applicationId + "/" + dateStr + "/" + companyId
				+ "/"; // 文件的基本命令
		// 添加日志
		OperateMDC mdc = new OperateMDC();
		mdc.setCompanyId(companyId);
		mdc.setFunctionName("文件上传");
		if (logger.isInfoEnabled()) {
			logger.log(ExtendedLevel.OPERATE_LOG_LEVEL,
					"UploadFileServlets文件上传");
		}

		// 文件上传
		FileUploadUtil fu = new FileUploadUtil(fileFactory);
		fu.setRelativePath(tempPath);
		UploadFileBean ub = null;
		try {
			ub = fu.doUpload(request, "UTF-8", FormBean.class, "Y");

			String token = ((FormBean) ub.getForm()).getToken();
			if (logger.isInfoEnabled()) {
				logger.info("token=" + token + " applicationId="
						+ applicationId);
			}
			boolean valid = CryptContext.getInstance().decrypt(applicationId,
					token);
			if (!valid) {
				if (logger.isInfoEnabled()) {
					logger.info("valid fail");
				}
				invalidState(response, State.NOPRMIN.getState());
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			invalidState(response, State.SYSERR.getState());
			return;
		}
		FormBean fileBean = (FormBean) ub.getForm();
		if (Float.parseFloat(fileBean.getFileSize()) > Constants.FILE_MAX_SIZE) {
			logger.log(ExtendedLevel.OPERATE_LOG_LEVEL, "UploadFileServlets");
		}
		// 文件名转为code
		fileBean.setCompanyId(companyId);
		fileBean.setPrasethMethod(prasethMethod);
		fileBean.setFileFoldPath(fileFoldPath);
		fileBean.setCurrentDate(dateStr);
		fileBean.setApplicationId(applicationId);
		

		File file = new File(fileBean.getSavePath());
		// MD5文件流加密所得字符串
		String fileCodeMD5 = fu.MD5(file);
		fileBean.setCode(fileCodeMD5);
		String code = fu.parseVoToName(fileBean);
		fileBean.setCode(code);
		
		

		FileInfo vo = new FileInfo();
		DistributedFileSystem dfSystem = HdFileSystemFactory.create();
		InputStream fileInputStream = new FileInputStream(file);
		Boolean isTrue = dfSystem.uploadFile(fileInputStream,
				fileBean.getFileFoldPath() + fileBean.getCode());
		fileInputStream.close();
		if (isTrue) {
			logger.info("hadoop上传成功！");
			// 删除临时目录的文件
			boolean del = FileUtils.deleteQuietly(file);

			companyRecentUploads.put(fileBean, new Date());

			logger.info("del pos=" + del);
		} else {
			// 文件上传Hadoop 失败 保存
			errorCache.add(fileBean);
		}

		// 文件上传完成了 生成当前文件对象
		vo.setApplicationId(fileBean.getApplicationId());
		vo.setCode(fileBean.getCode());
		vo.setUploadDate("" + fileBean.getCurrentDate());
		vo.setFileName(fileBean.getFilename());
		UUID uuid = UUID.randomUUID();
		vo.setUuid(uuid.toString());

		// 文件读取完成，开始调用action，保存到数据库中 生成vo

		FileInfoDaoImpl filedao = new FileInfoDaoImpl();
		filedao.insert(vo);

		JSONObject json = new JSONObject();
		json.put("state", State.SUCCESS.getState());
		json.put("fileName", vo.getFileName());
		json.put("code", vo.getUuid());

		JSONObject res = new JSONObject();
		res.put("codes", json);
		
		String utf8Data = URLEncoder.encode(res.toString(), "UTF-8");
		//response.addHeader("", "");
		response.setContentType("text/html;charset=UTF-8");
		response.getOutputStream().write(utf8Data.getBytes());
		if (logger.isInfoEnabled()) {
			logger.info("上传成功！");
		}
	}

	/**
	 * 判断是否超过指定时间上传文件的数量
	 */
	public boolean hasExceedLimit(String companyId) {
		int i = 0;
		for (Iterator<Map.Entry<FormBean, Date>> it = companyRecentUploads
				.entrySet().iterator(); it.hasNext();) {
			Map.Entry<FormBean, Date> entry = it.next();
			FormBean fb = entry.getKey();
			if (fb.getCompanyId().equals(companyId)) {
				i++;
			}
		}
		if (i > uploadlimit) {
			return true;
		}
		return false;
	}

	/**
	 * 1小时只能上传20次文件，定时清除1小时以前的记录 后台任务定时清除
	 */
	public void fileUploadTraceClear() {
		Date nowDate = new Date();
		for (Iterator<Map.Entry<FormBean, Date>> it = companyRecentUploads
				.entrySet().iterator(); it.hasNext();) {

			Map.Entry<FormBean, Date> entry = it.next();
			FormBean fb = entry.getKey();
			Date date = entry.getValue();
			Date clearDate = (Date) date.clone();
			clearDate.setTime(clearDate.getTime() + scanRecentInterval);

			if (logger.isInfoEnabled()) {
				logger.info("clear recent upload" + " date="
						+ clearDate.getTime() + " current=" + nowDate.getTime());
			}

			if (nowDate.after(clearDate)) {
				companyRecentUploads.remove(fb);
			}
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
	 * 重新上传保存到本地文件，没有上传hadoop的文件
	 */
	public void errorCacheHandler() {
		try {
			DistributedFileSystem dfSystem = HdFileSystemFactory.create();
			for (FormBean fb : errorCache) {
				String path = fb.getSavePath();
				File file = new File(path);
				Boolean isTrue = dfSystem.uploadFile(new FileInputStream(file),
						path + fb.getCode());
				if (isTrue) {
					errorCache.remove(fb);
					FileUtils.deleteQuietly(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("未上传成功的文件，没有处理成功！");
		}
	}

}
