package com.gionee.uaam2.mode.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gionee.uaam2.constant.Constant;

@Entry(objectClasses={"top","organizationalUnit"},base=Constant.LDAP_DN_ROOT)
public class Organization {

	@Id
	@JsonIgnore
	private Name dn;
	
	@Attribute(name="ou")
	private String orgName;
	
	@Attribute(name="description")
	private String description;
	
	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	
	
}
