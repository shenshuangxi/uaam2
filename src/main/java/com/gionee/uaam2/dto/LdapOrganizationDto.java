package com.gionee.uaam2.dto;

import java.util.List;

import javax.naming.Name;

import com.gionee.uaam2.mode.ldap.Organization;

public class LdapOrganizationDto {

	private final String orgName;
	private final String dn;
	private final String baseDn;
	private final String description;
	private List<LdapOrganizationDto> children;
	
	private String message;
	private Boolean success;
	private String state;
	
	public LdapOrganizationDto(Organization ldapOrganization) {
		Name nameDn = ldapOrganization.getDn();
		if(nameDn==null){
			this.dn = null;
			this.baseDn = null;
		}else{
			this.dn = nameDn.toString();
			this.baseDn = nameDn.getPrefix(nameDn.size()-1).toString();
		}
		this.orgName = ldapOrganization.getOrgName();
		this.description = ldapOrganization.getDescription();
		this.success = true;
		this.message = "操作成功";
		this.state = "closed";
	}
	

	public List<LdapOrganizationDto> getChildren() {
		return children;
	}

	public void setChildren(List<LdapOrganizationDto> children) {
		this.children = children;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getDn() {
		return dn;
	}

	public String getBaseDn() {
		return baseDn;
	}


	public String getDescription() {
		return description;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}


	
}
