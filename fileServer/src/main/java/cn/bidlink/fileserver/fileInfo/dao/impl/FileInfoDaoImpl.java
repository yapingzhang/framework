/*
 * TregFileDaoImpl.java 2012-09-03
 * Copyright  © 2001-2012 必联网
 * 京ICP备09004729号京公网安备110108008196号
 */
package cn.bidlink.fileserver.fileInfo.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import cn.bidlink.fileserver.db.IbatisSessionFactory;
import cn.bidlink.fileserver.fileInfo.model.FileInfo;

/**
 * 
 * @description:
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">SYSTEM</a>
 * @Date 2012 2012-9-4 下午6:45:14
 * 
 */

public class FileInfoDaoImpl {

	public void insert(FileInfo insertObj) {
		SqlSession sqlSession = IbatisSessionFactory.getSession();
		try {
			sqlSession.insert("fileInfoinsert", insertObj);
			sqlSession.commit();
		} finally {
			sqlSession.rollback();
			IbatisSessionFactory.closeSession();
		}

	}

	public List<FileInfo> selectByVO(FileInfo selectObj) {

		SqlSession sqlSession = IbatisSessionFactory.getSession();
		try {
			List<FileInfo> tfList = sqlSession.selectList("findFileInfos",
					selectObj);
			if (tfList != null && tfList.size() > 0) {
				return tfList;
			}
		} finally {
			IbatisSessionFactory.closeSession();
		}
		return null;

	}

}
