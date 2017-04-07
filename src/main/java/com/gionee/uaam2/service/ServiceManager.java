package com.gionee.uaam2.service;

import javax.annotation.Resource;


public class ServiceManager {

	@Resource
	private OrganzationService organzationService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private GroupService groupService;

	public OrganzationService getOrganzationService() {
		return organzationService;
	}

	public void setOrganzationService(OrganzationService organzationService) {
		this.organzationService = organzationService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	
	
	
	
}
