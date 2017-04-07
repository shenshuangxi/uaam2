package com.gionee.uaam2.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.uaam2.constant.Constant;
import com.gionee.uaam2.dao.DaoManager;
import com.gionee.uaam2.mode.ldap.Group;
import com.gionee.uaam2.mode.ldap.Organization;
import com.gionee.uaam2.mode.ldap.User;
import com.gionee.uaam2.service.OrganzationService;

public class OrganzationServiceImpl implements OrganzationService {

	@Autowired
	private DaoManager daoManager;
	private Logger logger = LoggerFactory.getLogger(OrganzationServiceImpl.class);
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}


	@Override
	public List<Organization> getSubOrganzationsByDn(String dn) {
		List<Organization> ldapOrganizations = daoManager.getLdapDao().findByBaseDn(dn, Organization.class);
		return ldapOrganizations;
	}
	
	@Override
	public List<Organization> getSubOneLevelOrganzationsByDn(String dn) throws InvalidNameException {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		List<Organization> ldapOrganizations = daoManager.getLdapDao().findAll(new LdapName(dn), searchControls, Organization.class);
		return ldapOrganizations;
	}


	@Override
	public Organization getLdapOrganzationsByDn(String dn) throws InvalidNameException {
		Organization ldapOrganization = new Organization();
		ldapOrganization.setDn(new LdapName(dn));
		return	daoManager.getLdapDao().findByDn(new LdapName(dn), Organization.class);
	}


	@Override
	public void updateLdapOrganization(Organization ldapOrganization) {
		daoManager.getLdapDao().update(ldapOrganization);
	}
	
	@Override
	public void saveLdapOrganization(Organization ldapOrganization) {
		daoManager.getLdapDao().create(ldapOrganization);
	}


	@Override
	public void removeLdapOrganzationByDn(Organization ldapOrganization) {
		daoManager.getLdapDao().delete(ldapOrganization);
	}


	@Override
	public void addOrganization(String orgName, String orgNo, String description) throws Exception {
		Organization ldapOrganization = new Organization();
		List<Organization> ldapOrganizations = daoManager.getLdapDao().find(ldapOrganization);
		if(ldapOrganizations!=null&&ldapOrganizations.size()!=0){
			throw new Exception(orgNo+" 部门已存在存在");
		}
		ldapOrganization.setOrgName(orgName);
		ldapOrganization.setDescription(description);
		daoManager.getLdapDao().create(ldapOrganization);
	}


	@Override
	public void removeOrganzation(String orgDn) throws Exception {
		if(!orgDn.equals(Constant.LDAP_DN_ROOT)){
			Organization organization;
			try {
				organization = daoManager.getLdapDao().findByDn(new LdapName(orgDn), Organization.class);
			} catch (InvalidNameException e) {
				e.printStackTrace();
				logger.error(e.toString());
				throw new Exception(e.toString());
			}
			
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			
			List<User> users = daoManager.getLdapDao().findAll(new LdapName(orgDn), searchControls, User.class);
			for(User user : users){
				daoManager.getLdapDao().delete(user);
			}
			
			List<Group> groups = daoManager.getLdapDao().findAll(new LdapName(orgDn), searchControls, Group.class);
			for(Group group : groups){
				daoManager.getLdapDao().delete(group);
			}
			
			List<Organization> organizations = daoManager.getLdapDao().findAll(new LdapName(orgDn), searchControls, Organization.class);
			Collections.sort(organizations, new Comparator<Organization>(){

				@Override
				public int compare(Organization o1, Organization o2) {
					return o1.getDn().size()>o2.getDn().size()?1:-1;
				}
				
			});
			for(Organization temp : organizations){
				daoManager.getLdapDao().delete(temp);
			}
			
			daoManager.getLdapDao().delete(organization);
		}else{
			throw new Exception("根节点不能被删除");
		}
		
	}


	@Override
	public Boolean hasChild(String orgDn) {
		SearchControls searchControls = new SearchControls();
		searchControls.setCountLimit(1);
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		try {
			if(!daoManager.getLdapDao().findAll(new LdapName(orgDn), searchControls, Organization.class).isEmpty()){
				return true;
			}
			if(!daoManager.getLdapDao().findAll(new LdapName(orgDn), searchControls, Group.class).isEmpty()){
				return true;
			}
			if(!daoManager.getLdapDao().findAll(new LdapName(orgDn), searchControls, User.class).isEmpty()){
				return true;
			}
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
		return true;
	}


	

}
