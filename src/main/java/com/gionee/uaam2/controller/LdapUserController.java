package com.gionee.uaam2.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.uaam2.constant.Constant;
import com.gionee.uaam2.dto.LdapUserDto;
import com.gionee.uaam2.dto.PageResponse;
import com.gionee.uaam2.message.Message;
import com.gionee.uaam2.mode.ldap.Group;
import com.gionee.uaam2.mode.ldap.User;
import com.gionee.uaam2.service.ServiceManager;
import com.gionee.uaam2.util.DtoFactory;
import com.gionee.uaam2.util.LdapPageResult;

@Controller
@RequestMapping("/ldap/user")
public class LdapUserController {
	
	@Autowired
	private ServiceManager serviceManager;
	private Logger logger = LoggerFactory.getLogger(LdapUserController.class);
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
		logger.info("");
	}
	
	@RequestMapping("/getUsersByGroupDn")
	public @ResponseBody PageResponse<LdapUserDto> getUsersByGroupDn(String groupDn,Integer page, Integer rows){
		Integer total = 0;
		Integer startIndex = (page-1)*rows;
		Integer endIndex = (page-1)*rows+rows;
		Group ldapGroup = null;
		try {
			ldapGroup = serviceManager.getGroupService().getLdapGroupByDn(groupDn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<User> ldapUsers = new ArrayList<User>();
		if(ldapGroup!=null){
			List<String> memberUids = ldapGroup.getMemberUids();
			if(memberUids!=null&&memberUids.size()>0){
				total = memberUids.size();
				int maxLoopCount = memberUids.size()>endIndex?endIndex:memberUids.size();
				for(int i=startIndex;i<maxLoopCount;i++){
					User ldapUser = new User();
					ldapUser.setAccount(memberUids.get(i));
					List<User> newLdapUsers = serviceManager.getUserService().getLdapUser(ldapUser);
					if(newLdapUsers!=null&&newLdapUsers.size()==1){
						ldapUsers.add(newLdapUsers.get(0));
					}
				}
			}
		}
		List<LdapUserDto> ldapUserDtos = DtoFactory.convertLdapUsers(ldapUsers);
		return new PageResponse<LdapUserDto>(ldapUserDtos, total);
	}
	
	
	@RequestMapping("/findUser")
	public @ResponseBody List<LdapUserDto> getLdapUser(String login,Integer pageRow){
		List<LdapUserDto> ldapUserDtos = new ArrayList<LdapUserDto>();
		List<User> ldapUsers = serviceManager.getUserService().getLdapUsersLikeLogin(login);
		if(ldapUsers!=null&&ldapUsers.size()>0){
			pageRow = ldapUsers.size() > pageRow ? pageRow : ldapUsers.size();
			ldapUsers = ldapUsers.subList(0, pageRow);
			ldapUserDtos = DtoFactory.convertLdapUsers(ldapUsers);
		}
		return ldapUserDtos;
	}
	
	@RequestMapping("/searchUser")
	public @ResponseBody PageResponse<LdapUserDto> getLdapUser(String username, String mobile, String mail, String account, Integer page,Integer rows){
		LdapPageResult<User> ldapPageResult = serviceManager.getUserService().searchLdapUsersPageLike(username,mobile,mail,account,page,rows);
		List<LdapUserDto> ldapUserDtos = DtoFactory.convertLdapUsers(ldapPageResult.getResultList());
		PageResponse<LdapUserDto> pageResponse = new PageResponse<LdapUserDto>(ldapUserDtos,ldapPageResult.getIsEnd()?ldapPageResult.getCount():ldapPageResult.getCount()+1);
		return pageResponse;
	}
	
	@RequestMapping("/delUsersByGroupDn")
	public @ResponseBody Message delUsersByGroupDn(String groupDn,String[] memberUids){
		Message message = null;
		try {
			for(String memberUid : memberUids){
				serviceManager.getGroupService().deleteLdapGroupMember(groupDn, memberUid);
			}
			message = new Message(true, "删除组员成功");
		} catch (Exception e) {
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	
	@RequestMapping("/addUserToGroup")
	public @ResponseBody Message addUserToGroup(String groupDn,String memberUid){
		Message message = null;
		try {
			serviceManager.getGroupService().addLdapGroupMember(groupDn,memberUid);
			message = new Message(true, "添加组员成功");
		} catch (Exception e) {
			message = new Message(false, e.getMessage());
		}
		return message;
	}
	
	@RequestMapping("/getUsersByOrgDn")
	public @ResponseBody PageResponse<LdapUserDto> getUsersByOrgDn(String orgDn,Integer page,Integer rows){
		LdapPageResult<User> ldapPageResult = serviceManager.getUserService().getOneLevelPageLdapUsersByOrgDn(orgDn,page,rows);
		List<LdapUserDto> ldapUserDtos = DtoFactory.convertLdapUsers(ldapPageResult.getResultList());
		PageResponse<LdapUserDto> pageResponse = new PageResponse<LdapUserDto>(ldapUserDtos,ldapPageResult.getIsEnd()?ldapPageResult.getCount():ldapPageResult.getCount()+1);
		return pageResponse;
	}
	
	@RequestMapping("/addUserToOrgDn")
	public @ResponseBody Message addUserToOrgDn(String orgDn,String mail,String mobile,String username,String account,String pwd,Integer isInner,Integer status){
		try {
			if(orgDn.equals(Constant.LDAP_DN_ROOT)){
				throw new Exception("根目录不允许添加用户");
			}
			serviceManager.getUserService().addUser(orgDn,mail,mobile,username,account,pwd,isInner,status);
		} catch (Exception e) {
			logger.error(e.toString());
			return new Message(false, e.getMessage());
		}
		return new Message(true, "添加成功");
	}
	
	
	
	
	
	@RequestMapping("/modifyLdapUserPasswrod")
	public @ResponseBody Message modifyLdapUserPasswrod(String login,String pwd){
		try {
			serviceManager.getUserService().saveUser(null, null, null, null, login, pwd, null, null);
		} catch (Exception e) {
			logger.error(e.toString());
			return new Message(false, e.getMessage());
		}
		return new Message(true, "修改成功");
	}
	
	@RequestMapping("/modifyLdapUserOrgn")
	public @ResponseBody Message modifyLdapUserOrgn(String login,String orgDn){
		try {
			serviceManager.getUserService().saveUser(orgDn, null, null, null, login, null, null, null);
		} catch (Exception e) {
			logger.error(e.toString());
			return new Message(false, e.getMessage());
		}
		return new Message(true, "修改成功");
	}
	
	@RequestMapping("/modifyLdapUserInfo")
	public @ResponseBody Message modifyLdapUserInfo(String login,String orgDn,String mail,String mobile,String username,Integer isInner, Integer status){
		try {
			serviceManager.getUserService().saveUser(orgDn, mail, mobile, username, login, null, isInner, status);
		} catch (Exception e) {
			logger.error(e.toString());
			return new Message(false, e.getMessage());
		}
		return new Message(true, "修改成功");
	}
	
	
	@RequestMapping("/forbidLdapUser")
	public @ResponseBody Message forbidLdapUser(String[] logins){
		try {
			for(String login : logins){
				serviceManager.getUserService().saveUser(null, null, null, null, login, null, null, 0);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return new Message(false, e.getMessage());
		}
		return new Message(true, "修改成功");
	}
	
	@RequestMapping("/enableLdapUser")
	public @ResponseBody Message enableLdapUser(String[] logins){
		try {
			for(String login : logins){
				serviceManager.getUserService().saveUser(null, null, null, null, login, null, null, 1);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return new Message(false, e.getMessage());
		}
		return new Message(true, "修改成功");
	}
}
