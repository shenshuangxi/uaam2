package com.gionee.uaam2.service;

import java.util.List;

import javax.naming.InvalidNameException;

import com.gionee.uaam2.mode.ldap.Organization;

public interface OrganzationService {

	List<Organization> getSubOrganzationsByDn(String dn);

	Organization getLdapOrganzationsByDn(String dn) throws InvalidNameException;

	List<Organization> getSubOneLevelOrganzationsByDn(String dn) throws InvalidNameException;

	void saveLdapOrganization(Organization ldapOrganization);
	
	void updateLdapOrganization(Organization ldapOrganization);

	void removeLdapOrganzationByDn(Organization ldapOrganization);

	void addOrganization(String orgName, String orgNo, String description) throws Exception;

	void removeOrganzation(String orgDn) throws Exception;

	Boolean hasChild(String orgDn);
	
}
