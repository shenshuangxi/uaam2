package com.gionee.uaam2.controller;

import java.util.ArrayList;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.uaam2.constant.Constant;
import com.gionee.uaam2.dto.LdapOrganizationDto;
import com.gionee.uaam2.message.Message;
import com.gionee.uaam2.mode.ldap.Organization;
import com.gionee.uaam2.service.ServiceManager;
import com.gionee.uaam2.util.DtoFactory;

@Controller
@RequestMapping("/ldap/organization")
public class LdapOrganizationController {

	@Autowired
	private ServiceManager serviceManager;
	private Logger logger = LoggerFactory.getLogger(LdapOrganizationController.class);
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
		logger.info("");
	}
	
	
	@RequestMapping("/getChildLdapOrganzationsByDn")
	public @ResponseBody List<LdapOrganizationDto> getChildLdapOrganzationsByDn(String id,String dn){
		if(dn==null){
			dn = id;
		}
		List<LdapOrganizationDto> ldapOrganizationDtos = new ArrayList<LdapOrganizationDto>();
		try {
			if(dn==null){
				Organization ldapOrganization;
				ldapOrganization = serviceManager.getOrganzationService().getLdapOrganzationsByDn(Constant.LDAP_DN_ROOT);
				LdapOrganizationDto ldapOrganizationDto = new LdapOrganizationDto(ldapOrganization);
				List<Organization> ldapOrganizations = serviceManager.getOrganzationService().getSubOneLevelOrganzationsByDn(ldapOrganizationDto.getDn());
				ldapOrganizationDto.setChildren(DtoFactory.convertLdapOrganzation(ldapOrganizations));
				ldapOrganizationDtos.add(ldapOrganizationDto);
//				LdapOrganization tempOrganization = new LdapOrganization();
//				tempOrganization.setDn(new LdapName("ou=组织架构,dc=sundy,dc=com"));
//				tempOrganization.setOrgName("组织架构");
//				LdapOrganizationDto ldapOrganizationDto = new LdapOrganizationDto(tempOrganization);
//				LdapOrganization ldapOrganization = serviceManager.getOrganzationService().getLdapOrganzationsByDn(dn);
//				List<LdapOrganization> ldapOrganizations = new ArrayList<LdapOrganization>();
//				ldapOrganizations.add(ldapOrganization);
//				ldapOrganizationDto.setChildren(DtoFactory.convertLdapOrganzation(ldapOrganizations));
//				ldapOrganizationDtos.add(ldapOrganizationDto);
			}else{
				List<Organization> ldapOrganizations = serviceManager.getOrganzationService().getSubOneLevelOrganzationsByDn(dn);
				for(Organization ldapOrganization: ldapOrganizations){
					ldapOrganizationDtos.add(new LdapOrganizationDto(ldapOrganization));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LdapOrganizationDto ldapOrganizationDto = new LdapOrganizationDto(new Organization());
			ldapOrganizationDto.setMessage(e.getMessage());
			ldapOrganizationDto.setSuccess(false);
			ldapOrganizationDtos.add(ldapOrganizationDto);
		}
		return ldapOrganizationDtos;
	}
	
	@RequestMapping("/orgInfo")
	public @ResponseBody LdapOrganizationDto orgInfo(String dn){
		Organization ldapOrganization = null;
		try {
			ldapOrganization = serviceManager.getOrganzationService().getLdapOrganzationsByDn(dn);
		} catch (InvalidNameException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return new LdapOrganizationDto(ldapOrganization);
	}
	
	@RequestMapping("/hasChild")
	public @ResponseBody Message hasChild(String dn){
		Boolean hasChild = serviceManager.getOrganzationService().hasChild(dn);
		return new Message(true, "操作成功",hasChild);
	}
	
	
	@RequestMapping("/saveLdapOrganzation")
	public @ResponseBody Message saveLdapOrganzation(String baseDn,String dn,String orgName,String description){
		try {
			if(dn!=null&&!dn.equals("")){
				Organization ldapOrganization = serviceManager.getOrganzationService().getLdapOrganzationsByDn(dn);
				if(ldapOrganization!=null){
					ldapOrganization.setDescription(description);
				}else{
					throw new Exception(orgName+"部门不存在");
				}
				serviceManager.getOrganzationService().updateLdapOrganization(ldapOrganization);
				return new Message(true, "操作成功");
			}else{
				String newdn = "ou="+orgName+","+baseDn;
				Organization ldapOrganization = null;
				try {
					ldapOrganization = serviceManager.getOrganzationService().getLdapOrganzationsByDn(newdn);
				} catch (Exception e) {
					if(e instanceof NameNotFoundException){
						ldapOrganization = new Organization();
						ldapOrganization.setDn(new LdapName(newdn));
						ldapOrganization.setOrgName(orgName);
						serviceManager.getOrganzationService().saveLdapOrganization(ldapOrganization);
						return new Message(true, "操作成功");
					}else{
						throw e;
					}
				}
				throw new Exception(orgName+"部门已存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new Message(false, e.toString());
		}
	}
	
	@RequestMapping("/removeLdapOrganzation")
	public @ResponseBody Message removeLdapOrganzation(String dn){
		try {
			if(!dn.equals(Constant.LDAP_DN_ROOT)){
				serviceManager.getOrganzationService().removeOrganzation(dn);
			}else{
				throw new Exception("根节点不能被删除");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			return new Message(false, e.toString());
		}
		return new Message(true, "操作成功");
	}
	
}
