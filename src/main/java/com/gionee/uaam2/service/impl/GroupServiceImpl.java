package com.gionee.uaam2.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.LikeFilter;

import com.gionee.uaam2.constant.Constant;
import com.gionee.uaam2.dao.DaoManager;
import com.gionee.uaam2.mode.ldap.Group;
import com.gionee.uaam2.mode.ldap.User;
import com.gionee.uaam2.service.GroupService;
import com.gionee.uaam2.util.LdapPageResult;
import com.gionee.uaam2.util.Util;

public class GroupServiceImpl implements GroupService {

	@Resource
	private DaoManager daoManager;
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
	
	
	
	@Override
	public List<Group> getLdapGroupsByOrgDn(String orgDn) {
		return daoManager.getLdapDao().findByBaseDn(orgDn, Group.class);
	}



	@Override
	public void addLdapGroupMember(String groupDn,String memberUid) throws Exception {
		
		try {
			Group ldapGroup = daoManager.getLdapDao().findByDn(new LdapName(groupDn), Group.class);
			if(ldapGroup!=null){
				ldapGroup.getMemberUids().add(memberUid);
			}else{
				throw new Exception(groupDn+" 目录所在分组不存在");
			}
			daoManager.getLdapDao().update(ldapGroup);
		} catch (InvalidNameException e) {
			throw new Exception(groupDn+"目录名称非法");
		}
		
	}
	
	@Override
	public void deleteLdapGroupMember(String groupDn,String memberUid) throws Exception {
		try {
			Group ldapGroup = daoManager.getLdapDao().findByDn(new LdapName(groupDn), Group.class);
			if(ldapGroup!=null){
				if(ldapGroup.getMemberUids().contains(memberUid)){
					ldapGroup.getMemberUids().remove(memberUid);
				}
			}else{
				throw new Exception(groupDn+" 目录所在分组不存在");
			}
			daoManager.getLdapDao().update(ldapGroup);
		} catch (InvalidNameException e) {
			throw new Exception(groupDn+"目录名称非法");
		}
		
	}



	@Override
	public Group getLdapGroupByDn(String groupDn) throws Exception {
		try {
			return daoManager.getLdapDao().findByDn(new LdapName(groupDn), Group.class);
		} catch (InvalidNameException e) {
			throw new Exception(groupDn+" 目录不合法");
		}
	}



	@Override
	public void deleteLdapGroup(Group ldapGroup) {
		daoManager.getLdapDao().delete(ldapGroup);
	}



	@Override
	public void addLdapGroup(Group ldapGroup) {
		daoManager.getLdapDao().create(ldapGroup);
	}



	@Override
	public List<Group> getOneLevelLdapGroupsByOrgDn(String orgDn) {
		return daoManager.getLdapDao().findOneLevelByBaseDn(orgDn, Group.class);
	}



	@Override
	public void ModifyLdapGroup(Group ldapGroup) throws IllegalArgumentException, IllegalAccessException {
		Name dn = ldapGroup.getDn();
		Group orgionLdapGroup = daoManager.getLdapDao().findByDn(dn, Group.class);
		Util.UpdateTarget(ldapGroup,orgionLdapGroup);
		daoManager.getLdapDao().update(orgionLdapGroup);
	}



	@Override
	public LdapPageResult<Group> getOneLevelPageLdapGroupsByOrgDn(String orgDn, Integer page, Integer rows) {
		return daoManager.getLdapDao().findOneLevelSubObjPage(Group.class, orgDn, null, rows, page);
	}

	@Override
	public LdapPageResult<Group> getLdapGroupsPageLike(String groupName, String description, Integer page, Integer rows) {
		AndFilter filter = new AndFilter();
		if(groupName!=null&&!groupName.equals("")){
			filter.append(new LikeFilter("cn", "*"+groupName+"*"));
		}else if(description!=null&&!description.equals("")){
			filter.append(new LikeFilter("description", "*"+description+"*"));
		}else{
			return new LdapPageResult<Group>();
		}
		return daoManager.getLdapDao().findSubObjPage(Group.class,  Constant.LDAP_DN_ROOT, filter.toString(), rows, page);
	}

	
}
