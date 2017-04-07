package com.gionee.uaam2.serviceApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.uaam2.controller.LdapGroupController;
import com.gionee.uaam2.message.Message;
import com.gionee.uaam2.service.ServiceManager;

@Controller
@RequestMapping("/organzationService")
public class OrganizationServiceController {

	@Autowired
	private ServiceManager serviceManager;
	private Logger logger = LoggerFactory.getLogger(LdapGroupController.class);
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
		logger.info("");
	}
	
	
	@RequestMapping("/addOrganization")
	public @ResponseBody Message addOrganization(String orgName,String orgNo,String description){
		Message message = null;
		try {
			serviceManager.getOrganzationService().addOrganization(orgName,orgNo,description);
			message = new Message(true, orgName+" 部门操作成功");
		} catch (Exception e) {
			logger.error(e.toString());
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	
	@RequestMapping("/delOrganization")
	public @ResponseBody Message delOrganization(String orgName,String orgNo,String description){
		return new Message(false, "不能执行删除操作");
	}
	
	@RequestMapping("/modifyOrganization")
	public @ResponseBody Message modifyOrganization(String orgName,String orgNo,String description){
		return new Message(false, "不能执行修改操作");
	}
	
}
