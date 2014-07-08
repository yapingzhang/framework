package cn.bidlink.framework.test.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bidlink.framework.test.service.ServiceExample1;

/**
 * @description: action例子.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午05:50:22
 */
@Controller
@RequestMapping("/demo")
public class DemoAction {

	@Autowired
	private ServiceExample1 serviceExample1;

	@RequestMapping(value = "/send", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> send(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("test1", request.getParameter("test"));
		result.put("test2", serviceExample1.getLong());
		return result;
	}
}
