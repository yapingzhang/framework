package cn.bidlink.demo.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.bidlink.demo.commons.Constants;
import cn.bidlink.demo.model.User;
import cn.bidlink.demo.service.UserService;

@Controller
@RequestMapping(value = Constants.USER_MODULE)
public class UserAction{

	@Autowired
	private UserService userService;
	
	/**
	 * 首页。
	 */
	@RequestMapping(value={"index"})
	public ModelAndView index(HttpServletRequest arg0,
			HttpServletResponse arg1, User arg2) {
		return new ModelAndView("user/index");
	}
	
	
	/**
	 * 首页。
	 */
	@RequestMapping(value={"list"})
	public ModelAndView list(HttpServletRequest arg0,
			HttpServletResponse arg1) {
		//用户列表。
		List<User> userList = userService.findAll();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userList", userList);
		return new ModelAndView("user/list", model);
	}
	
	/**
	 * 进入添加页面。
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	@RequestMapping(value={"add"})
	public ModelAndView add(HttpServletRequest arg0,
			HttpServletResponse arg1){
		return new ModelAndView("user/add");
	}
	
	/**
	 * 保存用户信息。
	 * @param arg0
	 * @param arg1
	 * @param user
	 * @return
	 */
	@RequestMapping(value={"save"})
	public ModelAndView save(HttpServletRequest arg0,
			HttpServletResponse arg1,@ModelAttribute(value="user") User user){
		this.userService.save(user);
		System.out.println(arg0.getParameter("username"));
		return new ModelAndView("redirect:" +arg1.encodeURL("/user/list.htm"));
	}
	
	/**
	 * 进入编辑页面。
	 * @param arg0
	 * @param arg1
	 * @param user
	 * @return
	 */
	@RequestMapping(value={"edit"})
	public ModelAndView edit(HttpServletRequest arg0,
			HttpServletResponse arg1, @RequestParam Long id){
		User user = this.userService.findByid(id);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);
		return new ModelAndView("user/edit", model);
	}
	
	/**
	 * 更新用户信息。
	 * @param arg0
	 * @param arg1
	 * @param user
	 * @return
	 */
	@RequestMapping(value={"update"})
	public ModelAndView update(HttpServletRequest arg0,
			HttpServletResponse arg1, @ModelAttribute(value="user") User user){
		this.userService.update(user);
		return new ModelAndView("redirect:" +arg1.encodeURL("/user/list.htm"));
	}
	
	
	/**
	 * 删除用户信息。
	 * @param arg0
	 * @param arg1
	 * @param user
	 * @return
	 */
	@RequestMapping(value={"delete"})
	public ModelAndView delete(HttpServletRequest arg0,
			HttpServletResponse arg1, @RequestParam Long id){
		this.userService.delete(id);
		return new ModelAndView("redirect:" +arg1.encodeURL("/user/list.htm"));
	}
	
	
}
