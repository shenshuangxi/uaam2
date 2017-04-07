package com.gionee.uaam2.dao;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.SearchControls;

import org.springframework.ldap.control.PagedResult;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQuery;

import com.gionee.uaam2.mode.ldap.Organization;
import com.gionee.uaam2.util.LdapPageResult;


@SuppressWarnings({ "deprecation", "unused" })
public interface LdapDao {

    <T> T findByDn(Name dn, Class<T> clazz);

    <T> T findOne(LdapQuery query, Class<T> clazz);
    
    <T> List<T> find(T t);

    <T> List<T> find(LdapQuery query, Class<T> clazz);

    <T> List<T> findAll(Class<T> clazz) ;

    <T> List<T> findAll(Name base, SearchControls searchControls, Class<T> clazz);

    <T> List<T> findAll(Name base, Filter filter, SearchControls searchControls, Class<T> clazz);

    <T> void create(T t);

    <T> void update(T t);

    <T> void delete(T t);
    
    void move(String sourceDn,String targetDn);

    <T> List<T> findByBaseDn(String dn, Class<T> t);
    
    <T> List<T> findOneLevelByBaseDn(String dn, Class<T> clazz);
    
    <T> PagedResult findOneLevelByBaseDnInAPage(String string, T t, Integer pageSize, PagedResultsCookie cookie) throws InvalidNameException;

	List<Organization> getAllPersonNames();

	<T> LdapPageResult<T> findOneLevelSubObjPage(Class<T> clazz, String dn, String filter, Integer PageSize, Integer currPage);

	<T> LdapPageResult<T> findSubObjPage(Class<T> clazz, String dn, String customFilter, Integer PageSize, Integer currPage);

}
