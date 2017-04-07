package com.gionee.uaam2.dao;

import javax.annotation.Resource;

public class DaoManager {

	@Resource
	private LdapDao ldapDao;

	public LdapDao getLdapDao() {
		return ldapDao;
	}

	public void setLdapDao(LdapDao ldapDao) {
		this.ldapDao = ldapDao;
	}
	
	
	
	
	
}
