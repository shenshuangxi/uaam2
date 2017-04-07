package com.gionee.uaam2.serviceApi;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.uaam2.controller.LdapGroupController;
import com.gionee.uaam2.dto.LdapUserDto;
import com.gionee.uaam2.dto.ServiceUserDto;
import com.gionee.uaam2.message.Message;
import com.gionee.uaam2.mode.ldap.User;
import com.gionee.uaam2.service.ServiceManager;

@Controller
@RequestMapping("/userService")
public class UserServiceController {

	@Autowired
	private ServiceManager serviceManager;
	private Logger logger = LoggerFactory.getLogger(LdapGroupController.class);
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
		logger.info("");
	}
	
	@RequestMapping("/saveUser")
	public @ResponseBody Message saveUser(String id,String account,String username,String password,String email,String mobile,String orgNo,Integer status){
		Message message = null;
		try {
//			serviceManager.getUserService().saveUser(id,account,username,password,email,mobile,orgNo,status);
			message = new Message(true, account+" 用户操作成功");
		} catch (Exception e) {
			logger.error(e.toString());
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	
	@RequestMapping("/changeUserStatus")
	public @ResponseBody Message changeUserStatus(String account,Integer status){
		Message message = null;
		try {
//			serviceManager.getUserService().changeUserStatus(account,status);
			message = new Message(true, account+" 用户操作成功");
		} catch (Exception e) {
			logger.error(e.toString());
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	
//	@RequestMapping("/findUser")
//	public ServiceUserDto findUser(String account,String email,String mobile){
////		LdapUser ldapUser = serviceManager.getUserService().findUser(account,email,mobile);
//	}
	
	
	
}
