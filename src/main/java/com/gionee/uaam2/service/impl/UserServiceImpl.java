package com.gionee.uaam2.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.InvalidNameException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.query.LdapQueryBuilder;

import com.gionee.uaam2.constant.Constant;
import com.gionee.uaam2.dao.DaoManager;
import com.gionee.uaam2.mode.ldap.IdObject;
import com.gionee.uaam2.mode.ldap.User;
import com.gionee.uaam2.service.UserService;
import com.gionee.uaam2.util.LdapPageResult;
import com.gionee.uaam2.util.Util;

public class UserServiceImpl implements UserService {

	@Resource
	private DaoManager daoManager;
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
		logger.debug("");
	}

	@Override
	public List<User> getLdapUsersByOrgDn(String dn) {
		return daoManager.getLdapDao().findByBaseDn(dn, User.class);
	}


	@Override
	public List<User> getLdapUsersLikeLogin(String login) {
		return daoManager.getLdapDao().find(LdapQueryBuilder.query().base(Util.getBase(User.class)).filter(new LikeFilter("uid", "*"+login+"*")), User.class);
	}


	@Override
	public List<User> getLdapUser(User ldapUser) {
		return daoManager.getLdapDao().find(ldapUser);
	}


	@Override
	public List<User> getOneLevelLdapUsersByOrgDn(String orgDn) {
		return daoManager.getLdapDao().findOneLevelByBaseDn(orgDn, User.class);
	}

	@Override
	public LdapPageResult<User> getOneLevelPageLdapUsersByOrgDn(String orgDn,Integer page, Integer rows) {
		return daoManager.getLdapDao().findOneLevelSubObjPage(User.class, orgDn, null,rows, page);
	}
	
	@Override
	public List<User> findUser(String account, String email, String mobile) {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		try {
			return daoManager.getLdapDao().findAll(new LdapName(Constant.LDAP_DN_ROOT), new OrFilter().append(new LikeFilter("uid", account)).append(new LikeFilter("mail", email)).append(new LikeFilter("mobile", mobile)), searchControls, User.class);
		} catch (InvalidNameException e) {
			logger.error(Constant.LDAP_DN_ROOT+" 根目录出错");
		}
		return new ArrayList<User>();
	}

	@Override
	public void addUser(String orgDn, String mail, String mobile,String username, String account, String pwd, Integer isInner, Integer status) throws Exception  {
		if(pwd==null||pwd.equals("")){
			pwd=Constant.LDAP_DEFAULT_PWD;
		}
		if(orgDn==null||orgDn.equals("")){
			throw new Exception("目录路径不能为空");
		}
		if(status==null){
			status = 1;
		}
		if(isInner==null){
			isInner = 1;
		}
		if(account==null||account.equals("")){
			IdObject idObject = new IdObject();
			idObject.setName(User.class.getSimpleName().toLowerCase());
			//同步防止添加用户出现uid相同
			synchronized (UserServiceImpl.class) {
				List<IdObject> idObjects = daoManager.getLdapDao().find(idObject);
				if(idObjects!=null&&idObjects.size()==1){
					idObject = idObjects.get(0);
					Long newidCount = idObject.getIdCount()+1;
					idObject.setIdCount(newidCount);
					User ldapUser = new User();
					ldapUser.setUsername(username);
					ldapUser.setMail(mail);
					ldapUser.setMobile(mobile);
					ldapUser.setStatus(status);
					ldapUser.setIsInner(isInner);
					ldapUser.setAccount(newidCount.toString());
					ldapUser.setUserPassword(Util.md5Base64ToLdap(pwd));
					ldapUser.setDn(new LdapName("uid="+newidCount+","+orgDn));
					daoManager.getLdapDao().create(ldapUser);
					daoManager.getLdapDao().update(idObject);
				}
			}
		}else{
			User ldapUser = new User();
			ldapUser.setUsername(username);
			ldapUser.setMail(mail);
			ldapUser.setMobile(mobile);
			ldapUser.setStatus(status);
			ldapUser.setIsInner(isInner);
			ldapUser.setAccount(account);
			ldapUser.setUserPassword(Util.md5Base64ToLdap(pwd));
			ldapUser.setDn(new LdapName("uid="+account+","+orgDn));
			daoManager.getLdapDao().create(ldapUser);
		}
	}

	
	@Override
	public void saveUser(String orgDn, String mail, String mobile,String username, String account, String password, Integer isInner, Integer status) throws Exception {
		if(account==null||account.equals("")){
			throw new Exception("用户账号不能为空");
		}
		Boolean needMove = false;
		User user = new User();
		user.setAccount(account);
		List<User> users = daoManager.getLdapDao().find(user);
		if(users.isEmpty()){
			throw new Exception(account+" 用户账号不存在");
		}
		user = users.get(0);
		if(orgDn!=null&&!orgDn.equals("")){
			String parentDn = user.getDn().getPrefix(user.getDn().size()-1).toString();
			if(!parentDn.equals(orgDn)){
				needMove = true;
			}
		}
		if(password!=null&&!password.equals("")){
			user.setUserPassword(Util.md5Base64ToLdap(password));
		}
		if(status!=null){
			user.setStatus(status);
		}
		if(isInner!=null){
			user.setIsInner(isInner);
		}
		if(username!=null&&!username.equals("")){
			user.setUsername(username);
		}
		if(mail!=null&&!mail.equals("")){
			user.setMail(mail);
		}
		if(mobile!=null&&!mobile.equals("")){
			user.setMobile(mobile);
		}
		daoManager.getLdapDao().update(user);
		if(needMove){
			daoManager.getLdapDao().move(user.getDn().toString(), orgDn);
		}
	}

	@Override
	public LdapPageResult<User> searchLdapUsersPageLike(String username, String mobile, String mail, String account, Integer page, Integer rows) {
		AndFilter filter = new AndFilter();
		if(username!=null&&!username.equals("")){
			filter.append(new LikeFilter("cn", "*"+username+"*"));
		}else if(mobile!=null&&!mobile.equals("")){
			filter.append(new LikeFilter("mobile", "*"+mobile+"*"));
		}else if(mail!=null&&!mail.equals("")){
			filter.append(new LikeFilter("mail", "*"+mail+"*"));
		}else if(account!=null&&!account.equals("")){
			filter.append(new LikeFilter("uid", "*"+account+"*"));
		}else{
			return new LdapPageResult<User>();
		}
		return daoManager.getLdapDao().findSubObjPage(User.class, Constant.LDAP_DN_ROOT, filter.toString(), rows, page);
	}

}
