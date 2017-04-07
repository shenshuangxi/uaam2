package com.gionee.uaam2.mode.ldap;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.gionee.uaam2.constant.Constant;

@Entry(objectClasses={"top","posixGroup"},base=Constant.LDAP_DN_ROOT)
public class Group {

	@Id
	private Name dn;
	
	@Attribute(name="cn")
	private String groupName;
	
	@Attribute(name="gidNumber")
	private Integer status;
	
	@Attribute(name="memberUid")
	private List<String> memberUids;
	
	@Attribute(name="description")
	private String description;

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getMemberUids() {
		return memberUids;
	}

	public void setMemberUids(List<String> memberUids) {
		this.memberUids = memberUids;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	
	
}
