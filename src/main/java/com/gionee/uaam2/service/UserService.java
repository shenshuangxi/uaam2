package com.gionee.uaam2.service;

import java.util.List;

import com.gionee.uaam2.mode.ldap.User;
import com.gionee.uaam2.util.LdapPageResult;

public interface UserService {


	List<User> getLdapUsersByOrgDn(String dn);

	List<User> getLdapUsersLikeLogin(String login);

	List<User> getLdapUser(User ldapUser);

	List<User> getOneLevelLdapUsersByOrgDn(String orgDn);

	List<User> findUser(String account, String email, String mobile);

	public void addUser(String orgDn, String mail, String mobile,String username, String account, String pwd, Integer isInner, Integer status)throws Exception;
	
	void saveUser(String orgDn, String mail, String mobile,String username, String account, String password, Integer isInner, Integer status) throws Exception;

	LdapPageResult<User> getOneLevelPageLdapUsersByOrgDn(String orgDn, Integer page, Integer rows);

	LdapPageResult<User> searchLdapUsersPageLike(String username, String mobile, String mail, String account, Integer page, Integer rows);

}
