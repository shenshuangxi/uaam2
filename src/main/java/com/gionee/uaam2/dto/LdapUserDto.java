package com.gionee.uaam2.dto;

import javax.naming.Name;

import com.gionee.uaam2.mode.ldap.User;

public class LdapUserDto {
	 
	 private final String username;
	 private final String login;
	 private final String email;
	 private final String mobile;
	 private final Integer status;
	 private final Integer isInner;
	 
	 
	 
	 private final String dn;
	 private final String baseDn;
	 private final String orgDn;
	 private final String orgName;
	 
	 private String message;
	 private Boolean success;
	 
	 private final String text;
	 private final String value;
	
	public LdapUserDto(User ldapUser) {
		this.isInner = ldapUser.getIsInner();
		this.username = ldapUser.getUsername();
		this.login = ldapUser.getAccount();
		this.status = ldapUser.getStatus();
		this.email = ldapUser.getMail();
		this.mobile = ldapUser.getMobile();
		Name nameDn = ldapUser.getDn();
		this.dn = nameDn.toString();
		this.baseDn = nameDn.getPrefix(nameDn.size()-1).toString();
		this.orgDn = nameDn.getPrefix(nameDn.size()-1).toString();
		this.orgName = nameDn.get(nameDn.size()-2).toString().split("=")[1];
		this.success = true;
		this.message = "操作成功";
		this.text = login+" , "+username;;
		this.value = login;
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


	public Integer getIsInner() {
		return isInner;
	}


	public String getUsername() {
		return username;
	}


	public String getLogin() {
		return login;
	}


	public String getEmail() {
		return email;
	}


	public String getMobile() {
		return mobile;
	}


	public Integer getStatus() {
		return status;
	}


	public String getDn() {
		return dn;
	}


	public String getBaseDn() {
		return baseDn;
	}


	public String getOrgDn() {
		return orgDn;
	}


	public String getOrgName() {
		return orgName;
	}


	public String getText() {
		return text;
	}


	public String getValue() {
		return value;
	}
	
	
	
	
	

}
