package cn.bidlink.fileserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import cn.bidlink.fileserver.bean.Accessory;
import cn.bidlink.fileserver.bean.FieldBean;
import cn.bidlink.fileserver.bean.FormBaseBean;
import cn.bidlink.fileserver.bean.FormBean;
import cn.bidlink.fileserver.bean.UploadFileBean;

/**
 * @author changzhiyuan
 * @date 2012-2-4
 * @desc 文件上传工具类
 */
public class FileUploadUtil {

	private FileItemFactory factory;

	public FileUploadUtil(FileItemFactory factory) {
		this.factory = factory;
	}

	private static Logger logger = Logger.getLogger(FileUploadUtil.class);

	/**
	 * 相对路径
	 */
	private String relativePath = "";
	/**
	 * 绝对路径
	 */
	private String absolutePath = "";

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * 
	 * @param relativePath
	 *            相对于根目录下的那个路径
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	/**
	 * 处理表单
	 * 
	 * @param everyItem
	 * @param obj
	 * @param mapFiledBean
	 * @param charset
	 * @throws Exception
	 */

	private void doFormFieldItem(FileItem everyItem, Object obj,
			Map<String, FieldBean> mapFiledBean,
			Map<String, Object> mapOtherForm, String charset) throws Exception {
		String name = everyItem.getFieldName();
		String value = new String(everyItem.getString().getBytes("ISO8859-1"),
				charset);

		if (mapFiledBean.containsKey(name)) {
			((FieldBean) mapFiledBean.get(name)).getFieldSetMethod().invoke(
					obj, new Object[] { value });
		} else {
			mapOtherForm.put(name, value);
		}

	}

	/**
	 * 对文件全文生成MD5摘要
	 * 
	 * @param file
	 *            要加密的文件
	 * @return MD5摘要码
	 */
	public String MD5(File file) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[2048];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			byte[] b = md.digest();
			return byteToHexString(b);
			// 16位加密
			// return buf.toString().substring(8, 24);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/** */
	/**
	 * 把byte[]数组转换成十六进制字符串表示形式
	 * 
	 * @param tmp
	 *            要转换的byte[]
	 * @return 十六进制字符串表示形式
	 */
	private static String byteToHexString(byte[] tmp) {
		String s;
		// 用字节表示就是 16 个字节
		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
		// 所以表示成 16 进制需要 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
			// 转换成 16 进制字符的转换
			byte byte0 = tmp[i]; // 取第 i 个字节
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
			// >>> 为逻辑右移，将符号位一起右移
			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		s = new String(str); // 换后的结果转换为字符串
		return s;
	}

	/**
	 * 处理上传文件
	 * 
	 * @param request
	 *            request Object 将请求对象封装为bean
	 * @param everyItem
	 *            every FileItem
	 * @param accessorySet
	 *            accessory Set 上传附件
	 * @param status
	 *            status 是否写入磁盘
	 * @throws Exception
	 */
	private void doFormDataItem(HttpServletRequest request, Object obj,
			FileItem everyItem, Set<Accessory> accessorySet, String status)
			throws Exception {
		// 获得文件名及路径
		String fileName = everyItem.getName();
		String remoteIP = request.getRemoteHost();
		Accessory accessory = new Accessory();
		if (fileName != null) {
			File fullFile = new File(everyItem.getName());
			accessory.setRemoteIP(remoteIP);
			accessory.setOldFullPath(everyItem.getName());
			accessory.setFiledName(everyItem.getFieldName());
			String simpleName = fullFile.getName();
			// 当前应用id
			String applicationId = request.getParameter("applicationId");
			String prasethMethod = request.getParameter("praseth_Method");
			String companyId = request.getParameter("companyId") == null ? "0"
					: request.getParameter("companyId");
			Date date = new Date();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");
			String dateStr = timeFormat.format(date);
			String fileFoldPath = applicationId + "/" + dateStr + "/"
					+ companyId + "/"; // 文件的基本命令
			request.setAttribute("currentDate", dateStr);
			request.setAttribute("prasethMethod", prasethMethod);

			FormBean fb = new FormBean();

			fb.setApplicationId(applicationId);
			fb.setCompanyId(companyId);
			fb.setPrasethMethod(prasethMethod);
			fb.setFileFoldPath(fileFoldPath);
			fb.setCurrentDate(dateStr);
			fb.setFilename(simpleName);
			String prefixName = simpleName
					.substring(0, simpleName.indexOf("."));
			String newName = TimeUtil.getIdByTime()
					+ simpleName.substring(simpleName.indexOf("."),
							simpleName.length());
			accessory.setFileNewName(newName);
			accessory.setFileSize(everyItem.get().length);
			accessory.setAccessoryName(prefixName);
			File fileOnServerPath = null;

			// 用户传入的绝对路径
			if (obj instanceof FormBaseBean) {
				FormBaseBean ubb = (FormBaseBean) obj;
				logger.info("文件路径=" + getRelativePath() + newName);
				// 文件本地路径
				fileOnServerPath = new File(getRelativePath() + newName);

				ubb.setSavePath(getRelativePath() + newName);
			} else {
				logger.info("文件路径=" + getRelativePath() + newName);
				FormBaseBean ubb = (FormBaseBean) obj;
				// fileOnServerPath = new File(getRealPath(request), newName);
				fileOnServerPath = new File(getRelativePath() + newName);
				ubb.setSavePath(getRelativePath() + newName);
			}
			if (!fileOnServerPath.exists()) {
				fileOnServerPath.getParentFile().mkdirs();
			}
			if ("Y".equals(status)) {
				everyItem.write(fileOnServerPath);
				accessory.setAccessoryURL(fileOnServerPath.getPath());
			}
			accessory.setAccessoryId(System.nanoTime() + "");
			accessorySet.add(accessory);
		}
	}

	/**
	 * 
	 * @param request
	 *            request Object
	 * @param charset
	 *            charset
	 * @param cls
	 *            Class Object(需要封装的对象模板)
	 * @param status
	 *            是否写入磁盘文件 Y是 N否
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public synchronized UploadFileBean doUpload(HttpServletRequest request,
			String charset, Class<?> cls, String status) throws Exception {
		request.setCharacterEncoding(charset);
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		UploadFileBean ufb = new UploadFileBean();
		// 表单对象
		Object obj = null;
		Map<String, FieldBean> mapFieldBean = null;
		Map<String, Object> mapOtherForm = new HashMap<String, Object>();
		Set<Accessory> accessorySet = new HashSet<Accessory>();
		if (cls != null) {
			obj = cls.newInstance();
			mapFieldBean = PropertyUtil.getFiledMap(cls);
		}

		if (isMultipart == true) {
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(Constants.FILE_MAX_SIZE);

			List<FileItem> fileItems = upload.parseRequest(request);
			if (fileItems == null || fileItems.size() == 0) {
				throw new NullPointerException("fileItems is null");
			}
			// 文本域的操作
			for (Iterator<FileItem> it = fileItems.iterator(); it.hasNext();) {
				FileItem item = it.next();
				if (item.isFormField()) {
					doFormFieldItem(item, obj, mapFieldBean, mapOtherForm,
							charset);
				} else {
					doFormDataItem(request, obj, item, accessorySet, status);
				}
			}
			ufb.setForm(obj);
			ufb.setAccessorys(accessorySet);
			ufb.setMapOtherForm(mapOtherForm);
		} else {
			throw new IllegalStateException(
					"the enctype is not multipart/form-data");
		}
		return ufb;
	}

	static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 把文件信息对象 解析成一个字符串
	 * 
	 * @param fileInformationVO
	 * @return
	 */
	public String parseVoToName(FormBean formBean) {
		StringBuffer code = new StringBuffer();
		code.append(formBean.getPrasethMethod()); // 解析方法
		code.append("_" + formBean.getApplicationId()); // 以应用id开头
		code.append("_" + formBean.getCurrentDate());
		code.append("_"
				+ (formBean.getCompanyId() == null ? "0" : formBean
						.getCompanyId())); // 当前文件上传的公司id
		code.append("_" + formBean.getCode()); // 文件名base64编码
		code.append("_" + formBean.getExtend() == null ? "0" : formBean
				.getPrasethMethod()); // 扩展字段
		
		//2014-02-18生成uuid
		//UUID uuid = UUID.randomUUID();
		return code.toString();
	}

}
