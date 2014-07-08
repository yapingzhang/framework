package cn.bidlink.framework.core.support.jmx;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.exceptions.InnerErrorException;

/**
 * 
 * @description: jmxbean发布
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-1-7 下午4:33:26
 */
public class BidJmxExporter implements
		ApplicationListener<ContextRefreshedEvent>, Ordered,Serializable {
 
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(BidJmxExporter.class);

	private String basePack = "cn.bidlink";
	
	private String [] excludePacks;
	
	private boolean isEnabled = false;
	
	private String beanRoot = "BidBean";
	
	private final static String jmxBeanExporter = "JMX_BEAN_EXPORTER";
	
 
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!(event.getSource() instanceof XmlWebApplicationContext)) {
			return;
		}
		if (!isEnabled) {
			return;
		}
		if (StringUtils.isEmpty(getBasePack())) {
			throw new InnerErrorException("basePack can not null!");
		}
		logger.info("===================bean jmx register========================");

		Pattern includeP = Pattern.compile(getBasePack());
 		XmlWebApplicationContext context = (XmlWebApplicationContext) event.getSource();
		String [] beanNames = context.getBeanDefinitionNames();
		if (!context.containsBean(jmxBeanExporter)) {
			MBeanExporter mbean = new MBeanExporter();
			mbean.setAssembler(new SimpleReflectiveMBeanInfoAssembler());
			mbean.setBeans(new HashMap<String, Object>());
			context.getBeanFactory().registerSingleton(jmxBeanExporter, mbean);
		}
		MBeanExporter mbExporter = context.getBean(jmxBeanExporter,MBeanExporter.class);
		
		Field field = ReflectionUtils.findField(MBeanExporter.class, "beans");
		field.setAccessible(true);
		Object bMap = ReflectionUtils.getField(field, mbExporter);
		Map<String, Object> beansMap = (Map<String, Object>) bMap; 

		
		try {
			Method unRegMethod = ReflectionUtils.findMethod(MBeanExporter.class, "unregisterBeans");
			unRegMethod.setAccessible(true);
			unRegMethod.invoke(mbExporter);
		} catch (Exception e) {
			 logger.error(e.getMessage(),e);
		} 
		
		for (String beanName : beanNames) {
			Object obj = context.getBean(beanName);
			Class<?> cls = obj.getClass();
			if(AopUtils.isAopProxy(obj) || AopUtils.isCglibProxy(obj)) {
				cls = AopUtils.getTargetClass(obj);
			}
			String packName = cls.getName();
			if(includeP.matcher(packName).matches()) {
				if (getExcludePacks() != null && getExcludePacks().length > 0) {
					String [] exps = getExcludePacks();
					boolean tmpFlag = false;
					for (String exp : exps) {
						Pattern excludeP = Pattern.compile(exp);
						if (excludeP.matcher(packName).matches()) {
							tmpFlag = true;
							break;
						}
					}
					if (tmpFlag) {
						continue;
					}
				}
				beansMap.put("BidBean:name="+cls.getName(), obj);
//				logger.error("BidBean:name="+cls.getName());
			}
		}
		context.getBean(jmxBeanExporter,MBeanExporter.class).setBeans(beansMap);
		mbExporter.afterPropertiesSet();

	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	 

	public String getBasePack() {
		return basePack;
	}

	public void setBasePack(String basePack) {
		this.basePack = basePack;
	}
    
	public String[] getExcludePacks() {
		return excludePacks;
	}

	public void setExcludePacks(String[] excludePacks) {
		this.excludePacks = excludePacks;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getBeanRoot() {
		return beanRoot;
	}

	public void setBeanRoot(String beanRoot) {
		this.beanRoot = beanRoot;
	}

 
	
}
