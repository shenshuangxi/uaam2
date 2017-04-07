package com.gionee.uaam2.mode.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entry(objectClasses={"top","organizationalUnit","IdManager"},base="dc=sundy,dc=com")
public class IdObject {

	@Id
	@JsonIgnore
	private Name dn;
	
	@Attribute(name="IdManager-name")
	private String name;
	
	@Attribute(name="ou")
	private String ou;
	
	@Attribute(name="IdManager-objectoid")
	private Long idCount;

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.ou = name;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public Long getIdCount() {
		return idCount;
	}

	public void setIdCount(Long idCount) {
		this.idCount = idCount;
	}
	
	
	
	
}
