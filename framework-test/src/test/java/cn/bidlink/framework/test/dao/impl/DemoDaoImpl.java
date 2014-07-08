package cn.bidlink.framework.test.dao.impl;

import cn.bidlink.framework.core.annotation.Dao;
import cn.bidlink.framework.dao.ibatis.impl.MyBatisBaseDaoImpl;
import cn.bidlink.framework.test.dao.DemoDao;
import cn.bidlink.framework.test.model.Demo;

/**
 * 
 * @description: TODO add description.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 上午11:50:55
 */
@Dao
public class DemoDaoImpl extends MyBatisBaseDaoImpl<Demo, java.lang.Long> implements DemoDao {

}
