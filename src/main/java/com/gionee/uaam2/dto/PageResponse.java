package com.gionee.uaam2.dto;

import java.util.List;

public class PageResponse<T> {

	private final List<T> rows;
	private final Integer total;
	public PageResponse(List<T> rows, Integer total) {
		this.rows = rows;
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public Integer getTotal() {
		return total;
	}
	
	
	
}
