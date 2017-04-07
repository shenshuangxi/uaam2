package com.gionee.uaam2;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gionee.uaam2.dao.LdapDao;
import com.gionee.uaam2.mode.ldap.IdObject;
import com.gionee.uaam2.mode.ldap.Organization;
import com.gionee.uaam2.mode.ldap.Group;
import com.gionee.uaam2.mode.ldap.User;
import com.gionee.uaam2.util.Util;

@ContextConfiguration("/config/applicationContext.xml")
public class UserDaoTest extends AbstractJUnit4SpringContextTests {

	static{
		/*xml格式*/
//		URL url = UserDaoTest.class.getClassLoader().getResource("log4j.xml");
//		DOMConfigurator.configure(url);
		/*properties格式*/
		URL configURL = UserDaoTest.class.getClassLoader().getResource("log4j.properties");
		PropertyConfigurator.configure(configURL);
	}
	
	private static Logger logger = LoggerFactory.getLogger(UserDaoTest.class);
	
	@Resource
	private LdapDao ldapDao;

	public void setLdapDao(LdapDao ldapDao) {
		this.ldapDao = ldapDao;
	}
	
	
	
	@Test
	public void testSearchIdObject() throws InvalidNameException{
//		List<IdObject> idObjects = ldapDao.findAll(IdObject.class);
		IdObject idObject = new IdObject();
		idObject.setName("organization");
		List<IdObject> idObjects = ldapDao.find(idObject);
		System.out.println(idObjects);
	}
	
	@Test
	public void testSearch() throws InvalidNameException, InstantiationException, IllegalAccessException{
		List<Group> users = ldapDao.findAll(Group.class);
		logger.debug(users.toString());
	}
	
	@Test
	public void testLdapOrganization() throws InvalidNameException{
		Organization ldapOrganization = new Organization();
		ldapOrganization.setDn(new LdapName("ou=信息中心,ou=金立,dc=sundy,dc=com"));
		List<? extends Organization> ldapOrganizations = ldapDao.findByBaseDn("ou=财务部,ou=金立,dc=sundy,dc=com", ldapOrganization.getClass());
		for(Organization temp : ldapOrganizations){
			System.out.println(temp.getDn().toString());
		}
		System.out.println(ldapOrganizations);
		
		ldapOrganization = ldapDao.findByDn(new LdapName("ou=财务部,ou=金立,dc=sundy,dc=com"), ldapOrganization.getClass());
		System.out.println(ldapOrganization.getDn().toString());
	}
	
	
	@Test
	public void testLdapOrganization1() throws InvalidNameException{
		List<? extends Organization> ldapOrganizations = ldapDao.findByBaseDn("ou=深圳市金立通信设备有限公司,dc=sundy,dc=com", Organization.class);
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		ldapOrganizations = ldapDao.findAll(new LdapName("ou=深圳市金立通信设备有限公司,dc=sundy,dc=com"), new SearchControls(), Organization.class);
		for(Organization temp : ldapOrganizations){
			System.out.println(temp.getDn().toString());
		}
		System.out.println(ldapOrganizations);
		
//		ldapOrganization = ldapDao.findByDn(new LdapName("ou=财务部,ou=金立,dc=sundy,dc=com"), ldapOrganization.getClass());
//		System.out.println(ldapOrganization.getDn().toString());
	}
	
	@Test
	public void testDn(){
		try {
			Name name = new LdapName("ou=信息中心,ou=财务部,ou=金立集团");
			
			int size = name.size();
			System.out.println(name.toString());
			System.out.println(name.getPrefix(size-1));
			System.out.println(name.getSuffix(size-1));
			System.out.println(size);
			
			
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testLike(){
		List<Group> LdapGroups = ldapDao.find(LdapQueryBuilder.query().base(Util.getBase(Group.class)).filter(new OrFilter().append((new LikeFilter("uid", "*2*")))), Group.class);
		for(Group LdapGroup : LdapGroups){
			System.out.println(LdapGroup.getDn().toString());
		}
	}
	
	
	
	@Test
	public void decode(){
		try {
			String str = URLDecoder.decode("dn=ou=%E6%B5%B7%E5%A4%96%E4%BA%8B%E4%B8%9A%E9%83%A8,ou=%E6%B7%B1%E5%9C%B3%E5%B8%82%E9%87%91%E7%AB%8B%E9%80%9A%E4%BF%A1%E8%AE%BE%E5%A4%87%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8", "UTF-8");
			str = URLDecoder.decode(str, "UTF-8");
			System.out.println(str);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Test
	public void testPage(){
		try {
			Organization ldapOrganization = new Organization();
			PagedResultsCookie cookie = null;
			do{
				PagedResult pagedResult = ldapDao.findOneLevelByBaseDnInAPage("ou=深圳市金立通信设备有限公司,dc=sundy,dc=com", ldapOrganization, 2, cookie);
				List<Organization> ldapOrganizations =  (List<Organization>) pagedResult.getResultList();
				System.out.println(ldapOrganizations.size());
				for(Organization organization : ldapOrganizations){
					System.out.println(organization.getDn());
				}
				cookie = pagedResult.getCookie();
				System.out.println(cookie);
			}while(cookie!=null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testoPage(){
		 try {
			final SearchControls searchControls = new SearchControls();
//			 searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
//			 final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(2);
//			 findObjPage(searchControls, processor, LdapOrganization.class, "ou=深圳市金立通信设备有限公司,dc=sundy,dc=com");
			List<Organization> ldapOrganizationalls = new ArrayList<Organization>();
			List<Organization> newldapOrganizationalls = new ArrayList<Organization>();
			for(int i=1;i<10;i++){
				List<Organization> ldapOrganizations = ldapDao.findOneLevelSubObjPage(Organization.class, "ou=深圳市金立通信设备有限公司,dc=sundy,dc=com", null, 2, i).getResultList();
				ldapOrganizationalls.addAll(ldapOrganizations);
				
//				ldapOrganizations = ldapDao.findOneLevelSubObjPage(LdapOrganization.class, "ou=深圳市金立通信设备有限公司,dc=sundy,dc=com", null, 2, 4-i).getResultList();
				newldapOrganizationalls.addAll(ldapOrganizations);
			}
			for(Organization organization : ldapOrganizationalls){
				System.out.println(organization.getDn());
			}
			System.out.println(ldapOrganizationalls.size());
			
			for(Organization organization : newldapOrganizationalls){
				System.out.println(organization.getDn());
			}
			System.out.println(newldapOrganizationalls.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testosubPage(){
		 try {
			final SearchControls searchControls = new SearchControls();
//			 searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
//			 final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(2);
//			 findObjPage(searchControls, processor, LdapOrganization.class, "ou=深圳市金立通信设备有限公司,dc=sundy,dc=com");
			List<User> ldapOrganizationalls = new ArrayList<User>();
			List<User> newldapOrganizationalls = new ArrayList<User>();
			String generateFilter = "(&(&(objectclass=top)(objectclass=inetOrgPerson)(objectclass=posixAccount)))";
			String customFilter = "(|(uid=*7*)(cn=*王*))";
			String filter = "(&("+generateFilter+customFilter+"))";
			for(int i=1;i<10;i++){
				List<User> ldapOrganizations = ldapDao.findSubObjPage(User.class, "ou=深圳市金立通信设备有限公司,"+Util.getBase(User.class), filter, 2, i).getResultList();
				ldapOrganizationalls.addAll(ldapOrganizations);
				
//				ldapOrganizations = ldapDao.findOneLevelSubObjPage(LdapOrganization.class, "ou=深圳市金立通信设备有限公司,dc=sundy,dc=com", null, 2, 4-i).getResultList();
				newldapOrganizationalls.addAll(ldapOrganizations);
			}
			for(User organization : ldapOrganizationalls){
				System.out.println(organization.getDn());
			}
			System.out.println(ldapOrganizationalls.size());
			
			for(User organization : newldapOrganizationalls){
				System.out.println(organization.getDn());
			}
			System.out.println(newldapOrganizationalls.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFint(){
		try {
			Organization organization = new Organization();
			organization.setDn(new LdapName("ou=10000000005,ou=10000000003,ou=10000000002,ou=00000000,dc=sundy,dc=com"));
			List<Organization> ldapOrganizationalls = ldapDao.find(organization);
			System.out.println(ldapOrganizationalls);
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
