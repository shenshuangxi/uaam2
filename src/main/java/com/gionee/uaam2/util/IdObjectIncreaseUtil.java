package com.gionee.uaam2.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gionee.uaam2.dao.LdapDao;
import com.gionee.uaam2.mode.ldap.IdObject;

@Component
public class IdObjectIncreaseUtil implements ApplicationContextAware {

	public static final Long idSegment = 5l;
	public static Map<String,Map<Long,Long>> idMap = new HashMap<String, Map<Long,Long>>();
	private static ApplicationContext applicationContext;
	
	public static String increaseId(String objName){
		Long currentIdCount = 0l;
		Long maxIdCount = 5l;
		if(idMap.containsKey(objName)){
			Entry<Long, Long> idEntry = idMap.get(objName).entrySet().iterator().next();
			currentIdCount = idEntry.getKey();
			maxIdCount = idEntry.getValue();
			if(currentIdCount+1>=maxIdCount){
				currentIdCount = maxIdCount;
				maxIdCount = maxIdCount + idSegment;
				IdObject idObject = new IdObject();
				idObject.setName(objName.toLowerCase());
				idObject.setIdCount(maxIdCount);
				LdapDao ldapDao = (LdapDao) applicationContext.getBean("ldapDao");
				ldapDao.update(idObject);
				
			}else{
				currentIdCount++;
			}
		}else{
			IdObject idObject = new IdObject();
			idObject.setName(objName.toLowerCase());
			LdapDao ldapDao = (LdapDao) applicationContext.getBean("ldapDao");
			List<IdObject> idObjects = ldapDao.find(idObject);
			if(idObjects!=null&&idObjects.size()==1){
				currentIdCount = idObjects.get(0).getIdCount()+1+idSegment;
				maxIdCount = currentIdCount + idSegment;
				idObject.setIdCount(maxIdCount);
				ldapDao.update(idObject);
			}
		}
		Map<Long,Long> countMap = new HashMap<Long, Long>();
		countMap.put(currentIdCount, maxIdCount);
		idMap.put(objName, countMap);
		return currentIdCount+"";
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		IdObjectIncreaseUtil.applicationContext = applicationContext;
	}
	
}
