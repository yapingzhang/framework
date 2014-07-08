/*
* Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
* All rights reserved.
* 必联（北京）电子商务科技有限公司 版权所有
*
* <p>Constant.java</p>
*   
*/

package cn.bidlink.framework.core.commons;

import cn.bidlink.framework.core.utils.ConfigUtils;

/**
 * 
 * @description:	引用以前Constants，再扩展
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012-9-7 上午11:09:55
 */
public final class Constants {
    
	public final static String SERVER_HOME = "server.home";
	
	public final static String SERVER_BASE = "server.base";
	
	public final static String SERVER_TOMCAT_BASE = "catalina.base";
	
	public final static String SERVER_JETTY_BASE = "jetty.home";
	
    public final static String FRAMEWORK_PREFIX = "framework";
    
    public final static String MESSAGE_SOURCE = "messageSource";
    
    public static final String PAGING_SCOPE_NAME = "paging";
    
    public static final String MESSAGE_SCOPE_NAME = "messages";
    
    public static final String PAGING_CURRENT_PAGE_NUM = "currentPageNum";
    
    public static final String PAGING_PAGE_SIZE = "pageSize";
    
    public static final String WEB_ACTION_REQUEST_METHOD_NAME = "WEB_ACTION_REQUEST_METHOD_NAME";
    
    public static final String WEB_ACTION_EXCEPTION_SCOPE_NAME = "exception";
    
    public static final String WEB_SUBMIT_TOKEN = "submitToken";
    
    public static final String WEB_VALIDATE_CODE = "validateCode";
    
    public static final String WEB_REQUEST_SCOPE_NAME = "request";
    
    public static final String WEB_SESSION_SCOPE_NAME = "session";
    
    public static final String WEB_APPLICATION_SCOPE_NAME = "application";
    
    public static final String WEB_CONTEXT_PATH = "contextPath";
    
    public static final String WEB_RESPONSE_SCOPE_NAME = "response";
    
    public static final String WEB_REQUEST_PARAMETER_MAP_NAME = "param";
    
    public static final String WEB_REQUEST_HEADER_MAP_NAME = "header";
    
    /**
     * 静态资源服务器地址
     */
    public static final String STATIC_HOST = "staticHost";
    
    /**
     * 工作流服务发布地址
     */
    public static final String BPM_SERVICE_HOST = "bpmServiceHost";
    
    /**
     * 与以前CXF集成
     */
    public static final String CXF_NAME_SPACE = "http://www.ebnew.com/";
    
    /**
     * 当前登陆用户
     */
    public static final String CURRENT_USER = "CURRENT_USER";
    
    /**
     * 当前登陆用户授权信息
     */
    public static final String CURRENT_USER_AUTH = "CURRENT_USER_AUTH";
    
	/**
	 * 排除字段
	 */
    public static final String EXECUTE_FIELDS = "EXECUTE_FIELDS";
	
	/**
	 * 包含字段
	 */
    public static final String INCLUDE_FIELDS = "INCLUDE_FIELDS";
    
    /**
     * 远程配置文件路径
     */
    public static final String REMOTE_CONF_PATH = "remote.conf.path";
    
    /**
     * 应用名称
     */
    public static final String APP_NAME = "app.name";
    
    /**
     * 配置文件远程zookeeper连接URL 
     */
    public static final String CONFIG_ZOO_CONNECT_URL = "config.zoo.connect.url";
    
	
	/**
	 * digest密钥
	 */
	public final static String ZOO_DIGEST_SECRET = "config:zooadmin";
	
	/**
	 * 配置文件(根路径)
	 */
	public static final String ZOO_CONF = "/bidconf";
	/**
	 * 所有目前正在使用的应用信息(根路径)
	 */
	public final static String ZOO_CONF_DATA = "/bidconfusers";
     /**
     * 统一编码
     */
    public static final String charset = "UTF-8";
    
    /**
     * @description  取得远程配置路径
     * @return
     */
    public static String getRemoteConfPath() {
    	return ConfigUtils.get(REMOTE_CONF_PATH);
    }
    
    /**
     * @description  配置文件远程zookeeper连接URL 
     * @return
     */
    public static String getConfigZooConnectUrl() {
    	return ConfigUtils.get(CONFIG_ZOO_CONNECT_URL);
    }
    
    /**
     * @description  取得应用名称
     * @return
     */
    public static String getAppName() {
    	return ConfigUtils.get(APP_NAME);
    }
    
	
}
