package com.gionee.uaam2.controller;

import java.util.List;

import javax.naming.ldap.LdapName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.uaam2.constant.Constant;
import com.gionee.uaam2.dto.LdapGroupDto;
import com.gionee.uaam2.dto.PageResponse;
import com.gionee.uaam2.message.Message;
import com.gionee.uaam2.mode.ldap.Group;
import com.gionee.uaam2.service.ServiceManager;
import com.gionee.uaam2.util.DtoFactory;
import com.gionee.uaam2.util.LdapPageResult;

@Controller
@RequestMapping("/ldap/group")
public class LdapGroupController {

	@Autowired
	private ServiceManager serviceManager;
	private Logger logger = LoggerFactory.getLogger(LdapGroupController.class);
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
		logger.info("");
	}
	
	@RequestMapping("/addLdapGroup")
	public @ResponseBody Message addLdapGroup(String orgDn,String groupName,String description){
		Message message = null;
		try {
			if(orgDn.equals(Constant.LDAP_DN_ROOT)){
				throw new Exception("根目录不允许添加分组");
			}
			Group ldapGroup = new Group();
			ldapGroup.setGroupName(groupName);
			ldapGroup.setStatus(1);
			ldapGroup.setDescription(description);
			ldapGroup.setDn(new LdapName("cn="+groupName+","+orgDn));
			serviceManager.getGroupService().addLdapGroup(ldapGroup);
			message = new Message(true, groupName+"组添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	@RequestMapping("/searchGroup")
	public @ResponseBody PageResponse<LdapGroupDto> searchGroup(String groupName,String description,Integer page,Integer rows){
		LdapPageResult<Group> ldapPageResult = serviceManager.getGroupService().getLdapGroupsPageLike(groupName, description, page, rows);
		List<LdapGroupDto> ldapUserDtos = DtoFactory.convertLdapGroups(ldapPageResult.getResultList());
		PageResponse<LdapGroupDto> pageResponse = new PageResponse<LdapGroupDto>(ldapUserDtos,ldapPageResult.getIsEnd()?ldapPageResult.getCount():ldapPageResult.getCount()+1);
		return pageResponse;
	}
	
	@RequestMapping("/delLdapGroup")
	public @ResponseBody Message delLdapGroup(String groupDn){
		Message message = null;
		try {
			Group ldapGroup = new Group();
			ldapGroup.setDn(new LdapName(groupDn));
			serviceManager.getGroupService().deleteLdapGroup(ldapGroup);
			message = new Message(true, "删除分组成功");
		} catch (Exception e) {
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	@RequestMapping("/modifyLdapGroup")
	public @ResponseBody Message modifyLdapGroup(String orgDn,String groupName,String description){
		Message message = null;
		try {
			Group ldapGroup = new Group();
			ldapGroup.setGroupName(groupName);
			ldapGroup.setStatus(1);
			ldapGroup.setDescription(description);
			ldapGroup.setDn(new LdapName("cn="+groupName+","+orgDn));
			serviceManager.getGroupService().ModifyLdapGroup(ldapGroup);
			message = new Message(true, "删除分组成功");
		} catch (Exception e) {
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	
	
	@RequestMapping("/getGroupsByOrgDn")
	public @ResponseBody PageResponse<LdapGroupDto> getGroupsByOrgDn(String orgDn,Integer page,Integer rows){
		LdapPageResult<Group> ldapPageResult = serviceManager.getGroupService().getOneLevelPageLdapGroupsByOrgDn(orgDn,page,rows);
		List<LdapGroupDto> ldapGroupDtos = DtoFactory.convertLdapGroups(ldapPageResult.getResultList());
		PageResponse<LdapGroupDto> pageResponse = new PageResponse<LdapGroupDto>(ldapGroupDtos,ldapPageResult.getIsEnd()?ldapPageResult.getCount():ldapPageResult.getCount()+1);
		return pageResponse;
	}
	
	
	
	
}
