package com.gionee.uaam2.util;

import java.util.List;

public class LdapPageResult<T> {

	private List<T> resultList;
	private Integer count;
	private Boolean isEnd;

	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Boolean getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Boolean isEnd) {
		this.isEnd = isEnd;
	}
	
	
	
}
