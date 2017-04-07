package com.gionee.uaam2.dto;

import java.util.List;
import javax.naming.Name;
import com.gionee.uaam2.mode.ldap.Group;
public class LdapGroupDto {
	
	private final String dn;
	private final String baseDn;
	private final String orgDn;
	private final String orgName;
	private final String groupName;
	private final String description;
	private final Integer memberCount;
	private final List<String> memberUids;
	
	private String message;
	private Boolean success;
	
	public LdapGroupDto(Group ldapGroup) {
		this.groupName = ldapGroup.getGroupName();
		this.description = ldapGroup.getDescription();
		this.memberUids = ldapGroup.getMemberUids();
		this.memberCount = memberUids.size();
		Name nameDn = ldapGroup.getDn();
		this.dn = nameDn.toString();
		this.baseDn = nameDn.getPrefix(nameDn.size()-1).toString();
		this.orgDn = nameDn.getPrefix(nameDn.size()-1).toString();
		this.orgName = nameDn.get(nameDn.size()-2).toString().split("=")[1];
		this.success = true;
		this.message = "操作成功";
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

	public String getGroupName() {
		return groupName;
	}

	public String getDescription() {
		return description;
	}

	public Integer getMemberCount() {
		return memberCount;
	}

	public List<String> getMemberUids() {
		return memberUids;
	}

}
