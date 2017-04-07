package com.gionee.uaam2.mode.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gionee.uaam2.constant.Constant;

@Entry(objectClasses = {"top","inetOrgPerson","posixAccount"},base=Constant.LDAP_DN_ROOT)
public class User {

	@Id
	@JsonIgnore
	private Name dn;
	
	@Attribute(name="uid")
	private String account;
	
	@Attribute(name="cn")
	private String username;
	
	@Attribute(name="sn")
	private String lastName;
	
	@Attribute(name="userPassword")
	private String userPassword;
	
	@Attribute(name="gidNumber")
	private Integer status;
	
	@Attribute(name="uidNumber")
	private Integer isInner;
	
	@Attribute(name="mobile")
	private String mobile;
	
	@Attribute(name="mail")
	private String mail;
	
	@Attribute(name="homeDirectory")
	private String homeDirectory = "/";

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.lastName = username;
	}

	public Integer getIsInner() {
		return isInner;
	}

	public void setIsInner(Integer isInner) {
		this.isInner = isInner;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	
	
	
	
	
	
	

	
}
