package com.gionee.uaam2.service;

import java.util.List;

import com.gionee.uaam2.mode.ldap.Group;
import com.gionee.uaam2.util.LdapPageResult;

public interface GroupService {

	List<Group> getLdapGroupsByOrgDn(String orgDn);

	void addLdapGroupMember(String groupDn, String memberUid) throws Exception;
	
	void deleteLdapGroupMember(String groupDn, String memberUid) throws Exception;

	Group getLdapGroupByDn(String groupDn) throws Exception;

	void deleteLdapGroup(Group ldapGroup);

	void addLdapGroup(Group ldapGroup);

	List<Group> getOneLevelLdapGroupsByOrgDn(String orgDn);

	void ModifyLdapGroup(Group ldapGroup) throws IllegalArgumentException, IllegalAccessException;

	LdapPageResult<Group> getOneLevelPageLdapGroupsByOrgDn(String orgDn,Integer page, Integer rows);

	LdapPageResult<Group> getLdapGroupsPageLike(String groupName, String description, Integer page, Integer rows);

}
