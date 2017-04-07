package com.gionee.uaam2.dao.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapOperationsCallback;
import org.springframework.ldap.core.support.SingleContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;

import com.gionee.uaam2.dao.LdapDao;
import com.gionee.uaam2.mode.ldap.Organization;
import com.gionee.uaam2.util.LdapPageResult;
import com.gionee.uaam2.util.Util;

@SuppressWarnings("deprecation")
public class LdapDaoImpl implements LdapDao {

	@Resource
	private LdapTemplate signalLdapTemplate;
	@Resource
	private LdapTemplate poolLdapTemplate;
	 
	public void setSignalLdapTemplate(LdapTemplate signalLdapTemplate) {
		this.signalLdapTemplate = signalLdapTemplate;
	}


	public void setPoolLdapTemplate(LdapTemplate poolLdapTemplate) {
		this.poolLdapTemplate = poolLdapTemplate;
	}


	public <T> T findOne(LdapQuery query, Class<T> clazz) {
		List<T> list = poolLdapTemplate.find(query, clazz);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	

	public <T> List<T> find(LdapQuery query, Class<T> clazz) {
		return poolLdapTemplate.find(query, clazz);
	}

	public <T> List<T> findAll(Class<T> clazz){
		return poolLdapTemplate.find(LdapQueryBuilder.query().base(Util.getBase(clazz)).filter(""), clazz);
	}

	public <T> List<T> findAll(Name base, SearchControls searchControls, Class<T> clazz) {
		return poolLdapTemplate.findAll(base, searchControls, clazz);
	}

	public <T> List<T> findAll(Name base, Filter filter, SearchControls searchControls, Class<T> clazz) {
		return poolLdapTemplate.find(base, filter, searchControls, clazz);
	}

	public <T> void create(T t) {
		poolLdapTemplate.create(t);
	}

	public <T> void update(T t) {
		poolLdapTemplate.update(t);
	}

	public <T> void delete(T t) {
		poolLdapTemplate.delete(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(T t) {
//		Name dn = poolLdapTemplate.getObjectDirectoryMapper().getCalculatedId(t);
		return (List<T>) poolLdapTemplate.find(LdapQueryBuilder.query().base(Util.getBase(t.getClass())).filter(Util.getFilter(t)), t.getClass());
	}

	public <T> T findByDn(Name dn, Class<T> clazz) {
		return poolLdapTemplate.findByDn(dn, clazz);
	}
	
	@Override
	public <T> List<T> findByBaseDn(String dn, Class<T> clazz) {
		return (List<T>) poolLdapTemplate.find(LdapQueryBuilder.query().base(dn).filter(""), clazz);
	}
	
	@Override
	public <T> List<T> findOneLevelByBaseDn(String dn, Class<T> clazz) {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		return poolLdapTemplate.findAll(LdapQueryBuilder.query().base(dn).base(), searchControls, clazz);
	}
	
	public <T> List<T> findByBaseDnInAPage(String dn, Class<T> clazz) {
		return (List<T>) poolLdapTemplate.find(LdapQueryBuilder.query().base(dn).filter(""), clazz);
	}
	
	@Override
	public <T> PagedResult findOneLevelByBaseDnInAPage(String dn,  final T t, Integer pageSize, PagedResultsCookie cookie) throws InvalidNameException {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		PagedResultsDirContextProcessor pagedResultsDirContextProcessor = new PagedResultsDirContextProcessor(pageSize, cookie);
		
		List<T> resultList = poolLdapTemplate.search(new LdapName("ou=深圳市金立通信设备有限公司,dc=sundy,dc=com"),"(&(objectclass=organizationalUnit))",searchControls, new ContextMapper<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T mapFromContext(Object ctx) throws NamingException {
				return (T) poolLdapTemplate.getObjectDirectoryMapper().mapFromLdapDataEntry((DirContextOperations)ctx, t.getClass());
			}
		}, pagedResultsDirContextProcessor);
		PagedResult pagedResult = new PagedResult(resultList, pagedResultsDirContextProcessor.getCookie());
		return pagedResult;
	}
	
	
//	@Override
//	public <T> List<T> findObjPage(final SearchControls searchControls, final PagedResultsDirContextProcessor processor, Class clazz,String dn){
//		final String filter = poolLdapTemplate.getObjectDirectoryMapper().filterFor(clazz, new AndFilter()).toString();
//		final String name = dn;
//		return  SingleContextSource.doWithSingleContext(poolLdapTemplate.getContextSource(), new LdapOperationsCallback<List<T>>() {
//		      @Override
//		      public List<T> doWithLdapOperations(LdapOperations operations) {
//		          List<T> oneResult = operations.search(name,filter,searchControls,new ContextMapper() {
//		    			public T mapFromContext(Object ctx) throws NamingException {
//		    				return  (T) poolLdapTemplate.getObjectDirectoryMapper().mapFromLdapDataEntry((DirContextOperations)ctx, LdapOrganization.class);
//		    			}
//		    		},
//		            processor);
//		        return oneResult;
//		      }
//		  });
//	}
	
	@Override
	public List<Organization> getAllPersonNames() {
		  final SearchControls searchControls = new SearchControls();
		  searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

		  final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(2);

		  return SingleContextSource.doWithSingleContext(poolLdapTemplate.getContextSource(), new LdapOperationsCallback<List<Organization>>() {
		      @Override
		      public List<Organization> doWithLdapOperations(LdapOperations operations) {
		        List<Organization> result = new LinkedList<Organization>();
		        do {
		          List<Organization> oneResult = operations.search("ou=深圳市金立通信设备有限公司,dc=sundy,dc=com","(&(objectclass=organizationalUnit))",searchControls,new ContextMapper<Organization>() {
		    			public Organization mapFromContext(Object ctx) throws NamingException {
		    				return  poolLdapTemplate.getObjectDirectoryMapper().mapFromLdapDataEntry((DirContextOperations)ctx, Organization.class);
		    			}
		    		},
		            processor);
		          result.addAll(oneResult);
		        } while(processor.hasMore());
		        return result;
		      }
		  });
		}

	@Override
	public <T> LdapPageResult<T> findOneLevelSubObjPage(final Class<T> clazz, final String dn,final String customFilter, Integer PageSize, final Integer currPage) {
		 final SearchControls searchControls = new SearchControls();
		 searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		 final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(PageSize);
		 String generateFilter = signalLdapTemplate.getObjectDirectoryMapper().filterFor(clazz, new AndFilter()).toString();
		 final String filter = customFilter==null?generateFilter:customFilter;
		 return SingleContextSource.doWithSingleContext(signalLdapTemplate.getContextSource(), new LdapOperationsCallback<LdapPageResult<T>>() {
		      @Override
		      public LdapPageResult<T> doWithLdapOperations(LdapOperations operations) {
		        List<T> oneResult = null;
		        LdapPageResult<T> ldapPageResult = new LdapPageResult<T>();
		        Integer count = 0;
		        Integer size = 0;
		        do {
		          oneResult = operations.search(dn,filter,searchControls,new ContextMapper<T>() {
		    			public T mapFromContext(Object ctx) throws NamingException {
		    				return  signalLdapTemplate.getObjectDirectoryMapper().mapFromLdapDataEntry((DirContextOperations)ctx, clazz);
		    			}
		          },processor);
		          size += oneResult.size();
		          count ++;
		       } while(processor.hasMore()&&count<currPage);
		        ldapPageResult.setResultList(oneResult);
		        ldapPageResult.setCount(size);
		        ldapPageResult.setIsEnd(!processor.hasMore());
		       return ldapPageResult;
		     }
		 });
	}
	
	@Override
	public <T> LdapPageResult<T> findSubObjPage(final Class<T> clazz, final String dn,final String customFilter, Integer PageSize, final Integer currPage) {
		 final SearchControls searchControls = new SearchControls();
		 searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		 final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(PageSize);
		 String generateFilter = signalLdapTemplate.getObjectDirectoryMapper().filterFor(clazz, new AndFilter()).toString();
		 final String filter = customFilter==null?generateFilter:"(&("+generateFilter+customFilter+"))";
		 return SingleContextSource.doWithSingleContext(signalLdapTemplate.getContextSource(), new LdapOperationsCallback<LdapPageResult<T>>() {
		      @Override
		      public LdapPageResult<T> doWithLdapOperations(LdapOperations operations) {
		        List<T> oneResult = null;
		        LdapPageResult<T> ldapPageResult = new LdapPageResult<T>();
		        Integer count = 0;
		        Integer size = 0;
		        do {
		          oneResult = operations.search(dn,filter,searchControls,new ContextMapper<T>() {
		    			public T mapFromContext(Object ctx) throws NamingException {
		    				return  signalLdapTemplate.getObjectDirectoryMapper().mapFromLdapDataEntry((DirContextOperations)ctx, clazz);
		    			}
		          },processor);
		          size += oneResult.size();
		          count ++;
		       } while(processor.hasMore()&&count<currPage);
		        ldapPageResult.setResultList(oneResult);
		        ldapPageResult.setCount(size);
		        ldapPageResult.setIsEnd(!processor.hasMore());
		       return ldapPageResult;
		     }
		 });
	}


	@Override
	public void move(String sourceDn, String targetDn) {
		poolLdapTemplate.rename(sourceDn, targetDn);
	}
	
	
	
	

}
