package cn.bidlink.fileserver.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.bidlink.fileserver.decrypt.CryptContext;

/**
 * @date 2013-2-17
 * @author changzhiyuan
 * @desc 应用获取token
 * 
 */
public class GetTokenServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(DownLoadFileServlet.class);

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.reset();

		String appid = request.getParameter("appid");
		String msg = request.getParameter("msg");
		String key = CryptContext.getInstance().encrypt(appid, msg);
		if (logger.isDebugEnabled()) {
			logger.debug("key=" + key);
		}

		response.getOutputStream().write(key.getBytes());
	}
}
