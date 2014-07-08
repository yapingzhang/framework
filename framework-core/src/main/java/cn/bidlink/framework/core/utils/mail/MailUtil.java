package cn.bidlink.framework.core.utils.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

/**
 * @description:	邮件发送工具类
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2007-10-30 上午10:45:37
 */
public class MailUtil {

	private static Logger logger = Logger.getLogger(MailUtil.class);
	
	private final static String contentType = "text/html;charset=UTF-8";

	private String userName = "";

	private String password = "";

	private String host = "";

	private int port = 25;

	/**
	 * Message.RecipientType.TO(发送)/CC(抄送)/BCC(密送)
	 */
	private Map<Message.RecipientType, String[]> toAddrMap = new HashMap<RecipientType, String[]>();

	public Map<Message.RecipientType, String[]> getToAddrMap() {
		return toAddrMap;
	}

	public void setToAddrMap(Map<Message.RecipientType, String[]> toAddrMap) {
		this.toAddrMap = toAddrMap;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public MailUtil() {
	}

	public MailUtil(String userName, String password, String host) {
		this.userName = userName;
		this.password = password;
		this.host = host;
	}

	public MailUtil(String userName, String password, String host, int port) {
		this.userName = userName;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	/**
	 * 初始邮件发送
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param fromAddr
	 *            邮件发送者（如:xxx@163.com）
	 * @param host
	 *            邮件服务器地址 (如:smtp.163.com)
	 * @param port
	 *            端口25 Gmail不是这个端口
	 * @return
	 */
	private Session initMailSend(String userName, String password,
			String fromAddr, String host, int port) {
		Properties props = new Properties();
		props.put("mail.smtp.port", String.valueOf(port));
		props.put("mail.smtp.host", host);
	    props.put("mail.transport.protocol", "smtp"); //邮件发送协议
		props.put("mail.smtp.localhost", host);
		 props.put("mail.debug", "true");
		props.put("mail.smtp.from", fromAddr);
		props.put("mail.smtp.auth", "true");
		PopupAuthenticator pa = new PopupAuthenticator(userName, password);
		return Session.getDefaultInstance(props, pa);
	}

	/**
	 * 带附件的邮件(收邮件人全是发送，不包括抄送/密送)
	 * 
	 * @param fromAddr
	 *            发件人
	 * @param toAddrMap
	 *            收邮件人
	 * @param subject
	 *            主题
	 * @param context
	 *            内容
	 * @return true is success,false is failure
	 */
	public boolean sendMail(String fromAddr, String[] toAddr, String subject,
			String context, Attachment att) {
		toAddrMap.put(Message.RecipientType.TO, toAddr);
		return sendMail(fromAddr, toAddrMap, subject, context, att);
	}

	/**
	 * 不带附件的邮件(收邮件人全是发送，不包括抄送/密送)
	 * 
	 * @param fromAddr
	 *            发件人
	 * @param toAddrMap
	 *            收邮件人
	 * @param subject
	 *            主题
	 * @param context
	 *            内容
	 * @return true is success,false is failure
	 */
	public boolean sendMail(String fromAddr, String[] toAddr, String subject,
			String context) {
		toAddrMap.put(Message.RecipientType.TO, toAddr);
		return sendMail(fromAddr, toAddrMap, subject, context, null);
	}

	/**
	 * 不带附件的邮件
	 * 
	 * @param fromAddr
	 *            发件人
	 * @param toAddrMap
	 *            收邮件人
	 * @param subject
	 *            主题
	 * @param context
	 *            内容
	 * @return true is success,false is failure
	 */
	public boolean sendMail(String fromAddr,
			Map<Message.RecipientType, String[]> toAddrMap, String subject,
			String context) {
		return sendMail(fromAddr, toAddrMap, subject, context, null);
	}

	/**
	 * 邮件发送
	 * 
	 * @param fromAddrr
	 *            发邮人
	 * @param toAddrMap
	 *            收件人
	 * @param subject
	 *            主题
	 * @param context
	 *            内容
	 * @param attachment
	 *            附件
	 * @return true is success,false is failure
	 * @throws Exception
	 */
	public boolean sendMail(String fromAddr,
			Map<Message.RecipientType, String[]> toAddrMap, String subject,
			String context, Attachment attachment) {
		try {
			Session session = initMailSend(userName, password, fromAddr, host,
					port);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromAddr)); // 设置发件人
			msg.setSubject(subject); // 设置主题
			msg.setSentDate(new Date()); // 设置发送日期

			/**
			 * 收件人检查
			 */
			if (toAddrMap == null || toAddrMap.size() == 0) {
				throw new RuntimeException("收件人列表为空!");
			}
			// 设置收件人
			for (Iterator<Map.Entry<Message.RecipientType, String[]>> it = toAddrMap
					.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Message.RecipientType, String[]> entry = it.next();
				msg.addRecipients(entry.getKey(), getEmailAddress(entry
						.getValue()));
			}
			MimeMultipart mm = new MimeMultipart("related");
			// 新建一个存放信件内容的BodyPart对象
			BodyPart mdp = new MimeBodyPart();
			// 设置内容
			mdp.setContent(context, contentType);
			// 添加内容
			mm.addBodyPart(mdp);
			/** ********************添加附件************************** */
			if (attachment != null && attachment.getMapAttachMent().size() != 0) {
				Map<String, byte[]> att = attachment.getMapAttachMent();
				for (Iterator<Map.Entry<String, byte[]>> it = att.entrySet()
						.iterator(); it.hasNext();) {
					Map.Entry<String, byte[]> entry = it.next();
					mdp = new MimeBodyPart();
					mdp.setFileName(new String(
							entry.getKey().getBytes("UTF-8"), "ISO8859-1"));
					DataHandler dh = new DataHandler(new ByteArrayDataSource(
							entry.getValue(), "application/octet-stream"));
					mdp.setDataHandler(dh);
					// 将含有附件的BodyPart加入到MimeMultipart对象中
					mm.addBodyPart(mdp);
				}
			}
			// 把mm作为消息对象的内容
			msg.setContent(mm);
			msg.saveChanges();
			javax.mail.Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}

	}

	/**
	 * 取得Email Address
	 * 
	 * @param toAddr
	 * @return
	 * @throws AddressException
	 */

	private InternetAddress[] getEmailAddress(String[] toAddr)
			throws AddressException {
		InternetAddress[] tos = new InternetAddress[toAddr.length];
		int i = 0;
		for (String addr : toAddr) {
			tos[i++] = new InternetAddress(addr);
		}
		return tos;
	}

	/**
	 * 发送文本邮件
	 * 
	 * @param fromAddr
	 *            发件人(如:xxx@163.com)
	 * @param toAddr
	 *            收件人
	 * @param subject
	 *            标题
	 * @param message
	 *            内容
	 * @param rt
	 *            发送邮件类型(TO/发送,CC/抄送,BCC/密码送)(Message.RecipientType.TO)
	 * @return
	 */

	/**
	 * Authenticator
	 */
	static class PopupAuthenticator extends Authenticator {
		private String user;

		private String pass;

		public PopupAuthenticator(String user, String pass) {
			this.user = user;
			this.pass = pass;
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, pass);
		}
	}
}
