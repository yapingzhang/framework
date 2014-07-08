package cn.bidlink.framework.dao.ibatis;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class MybatisConfig extends SqlSessionFactoryBean {
	//加载所有module下的vo类为默认别名类
	
	Resource[]  aliasModuleClass ;
	
	public void setAliasModuleClass(Resource[] aliasModuleClass) {
		this.aliasModuleClass = aliasModuleClass;
	}

	/**
	 * 加载所有module下的vo类为默认别名类
	 */
    public void afterPropertiesSet() throws Exception {
    	//先计算出当前class路径是生么
//    	String localPathName = this.getClass().getClassLoader().getResource("").getPath();
//    	if(localPathName!=null && localPathName.contains("%20")){
//    		localPathName = localPathName.replaceAll("%20", " ");
//		}
    	
    	//然后计算出该路径下的所有类    	
//    	Class[] classType = new Class[aliasModuleClass.length];
//    	if(aliasModuleClass!=null){
//	    	for(int i=0;i<aliasModuleClass.length;i++){	
//	    		Resource re = aliasModuleClass[i];
//	    		String classPath = re.getURI().getPath();
//	    		if(classPath!=null && classPath.contains("%20")){
//	    			classPath = classPath.replaceAll("%20", " ");
//	    		}	    		
//	    		classPath = classPath.replace(localPathName, "");
//	    		classPath = classPath.replaceAll("/", ".");
//	    		classPath = classPath.substring(0,classPath.lastIndexOf(".class"));
//	    		//最后生成类名放入数组中
//	    		classType[i] = Class.forName(classPath);
//	    	}
//    	}
    	//把类名生成到mybatis的配置中 通过调用super这个对象方法
   // 	super.setTypeAliases(classType);
    	//setTypeAliasesPackage("cn.bidlink.baseInfo.user.model");
    	
        super.afterPropertiesSet();
    }
	
}




























