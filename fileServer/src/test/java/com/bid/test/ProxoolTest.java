package com.bid.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.bidlink.fileserver.db.IbatisSessionFactory;

public class ProxoolTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		SqlSession sqlSession = IbatisSessionFactory.getSession();
		
	}

}
