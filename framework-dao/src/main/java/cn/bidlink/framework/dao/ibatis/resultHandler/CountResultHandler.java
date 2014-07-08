package cn.bidlink.framework.dao.ibatis.resultHandler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class CountResultHandler implements  ResultHandler{
	private int count;
	
	@Override
	public void handleResult(ResultContext context) {
		count = Integer.parseInt("" +  context.getResultObject());
	}
		
	public int getCount(){
		return count;
	}
}