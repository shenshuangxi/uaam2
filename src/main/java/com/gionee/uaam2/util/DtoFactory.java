package com.gionee.uaam2.util;

import java.util.ArrayList;
import java.util.List;

import com.gionee.uaam2.dto.LdapGroupDto;
import com.gionee.uaam2.dto.LdapOrganizationDto;
import com.gionee.uaam2.dto.LdapUserDto;
import com.gionee.uaam2.mode.ldap.Group;
import com.gionee.uaam2.mode.ldap.Organization;
import com.gionee.uaam2.mode.ldap.User;



public class DtoFactory {

	public static List<LdapOrganizationDto> convertLdapOrganzation(List<Organization> ldapOrganizations) {
		List<LdapOrganizationDto> ldapOrganizationDtos = new ArrayList<LdapOrganizationDto>();
		for(Organization ldapOrganization: ldapOrganizations){
			ldapOrganizationDtos.add(new LdapOrganizationDto(ldapOrganization));
		}
		return ldapOrganizationDtos;
	}

	public static List<LdapUserDto> convertLdapUsers(List<User> ldapUsers) {
		List<LdapUserDto> ldapUserDtos = new ArrayList<LdapUserDto>();
		if(ldapUsers!=null&&ldapUsers.size()>0){
			for(User ldapUser: ldapUsers){
				ldapUserDtos.add(new LdapUserDto(ldapUser));
			}
		}
		return ldapUserDtos;
	}

	public static List<LdapGroupDto> convertLdapGroups(List<Group> ldapGroups) {
		List<LdapGroupDto> ldapGroupDtos = new ArrayList<LdapGroupDto>();
		if(ldapGroups!=null&&ldapGroups.size()>0){
			for(Group ldapGroup : ldapGroups){
				ldapGroupDtos.add(new LdapGroupDto(ldapGroup));
			}
		}
		return ldapGroupDtos;
	}

}
